package edu.rutgers.hpc;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.DbPath;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class HPC implements HPCInterface {

	// here is an overview of how the system functions:
	//
	// The user makes a HPC call to the couchdb database.
	// This call writes to the broker db as a document of type
	// 'WorkRequest'.
	// Broker process is listening to the database constantly to
	// check if new WorkRequest is created.
	// When broker reads this document, it chooses a matching worker based on
	// skills mentioned in the WorkRequest. This creates a new corresponding
	// document of type "WorkAssignment" where it writes WorkRequest ID, user id
	// and worker
	// id. THe IDs of all of the documents are created using Java's UUID class.
	// Please note we are not using couchdb's _id,_rev here.
	// The worker is also constantly checking if a new assignment doc with his
	// worker id is created. Based on the job details available in the
	// WorkRequest,
	// he provides work estimate (time/Cost) and updates WorkAssignment Document
	// with the
	// work estimate details. Please note user is yet to provide the actual
	// input data
	// at this point. (in the WorkAssignment, workInput ID is null).
	//
	// The user meanwhile is listening to check if a new WorkAssignment type
	// document is created for his WorkRequest ID. When he sees this document in
	// broker
	// db, the user reads the workestimate details from the WorkAssignment
	// and either approves or rejects by updating the WorkEstimateStatus in the
	// WorkAssignment.
	// If he approves the estimate, he also creates WorkInput Document in the
	// iodb and updates
	// the WorkAssignment Document with the WorkInputID.
	//
	// The worker who is listening for the document changes on the specific
	// WorkAssignment Document
	// that is assigned to him, sees the WorkInput ID and retrieves the
	// WorkInput document
	// from iodb. On completion of his job, the worker writes results in
	// WorkOutput Document
	// in iodb and updates the WorkAssignment document with the WorkOutputID.
	// User retrieves the WorkOutput Document by listening on the changes to the
	// WorkAssignment Document.
	// and changes the status to closed.

	// Additional Attributes in the WorkAssignment document
	//
	// 1. WorkAssignmentStatus - assigned, estimated, inputgiven, outputgiven,
	// closed.
	// 2. WorkAssignemntStatus_TimeStamp
	// 3. WorkEstimateStatus - waiting, approved, rejected,
	// 4. WorkEstimateStatus_TimeStamp
	// 5. WorkEstimate_Time
	// 6. WorkEstimate_Cost

	// Broker listens for WorkRequest
	// Worker listens for WorkAssignment
	// Worker listens for WorkInput for the specific WorkAssignment
	// User listens for WorkAssignment for his workRequest
	// User listens for WorkOuput for his WorkRequest + Work Assignment

	// Only authorized users and workers are with valid credentials are allowed
	// to connect to BrokerDB to create workrequest and read workrequests.
	// Broker is not allowed to access IODB.
	//

	//
	// what each user/worker could do (read/update) on individual documents will
	// be controlled by Document Validation Functions defined in the _design
	// document validate_doc_update
	
	// Each Document has an additional attribute called authors which lists the user id who
	// are allowed to update a specific document.

	// private static String DB_URL = "http://127.0.0.1:5984/";

	private static String DB_URL = "https://127.0.0.1:6984/";
	// private static String DB_URL = "https://hpc.iriscouch.com:6984/";

	// Change the value below either true or false to approve or reject work
	// estimate
	// Only for demo
	private boolean APPROVE_ESTIMATE = true;
	private boolean REJECT_ESTIMATE = false;

	private static String BROKER_DB = "brokerdb";
	private static String IO_DB = "iodb";
	private User user = null;
	private HttpClient httpClient = null;
	private CouchDbInstance dbInstance = null;
	private WorkRequest workRequest = null;
	private UUID requestID = null;
	private CouchDbConnector brokerdb = null;
	private CouchDbConnector iodb = null;
	long dbSequenceNumber;
	// private static String DB_USER="vpathak";
	// private static String DB_PWD="vpathak123";
	private static String DB_USER = "parveen";
	private static String DB_PWD = "hpc1234";

	public HPC(User user) throws Exception {
		this.user = user;

	}

	@Override
	public HashMap<String, String> invoke(String taskDescription,
			HashMap<String, String> input, long deadLine,
			List<String> skillsNeeded, Cost cost) throws Exception {

		if (isDataBaseAlive()) {
			workRequest = new WorkRequest();

			requestID = UUID.randomUUID(); // Using Java's UUID class to create
											// Unique ID for the request
											// This is different than _id from
											// couchdb.

			workRequest.setRequestID(requestID.toString());
			workRequest.setRequestorID(user.getUserID());
			workRequest.setSkillsNeeded(skillsNeeded);

			WorkRequestRepository workRequestRepository = new WorkRequestRepository(
					brokerdb);

			ArrayList authors = new ArrayList();
			authors.add(user.getUserID());

			workRequest.setAuthors(authors);

			workRequestRepository.add(workRequest);
			dbSequenceNumber = brokerdb.getDbInfo().getUpdateSeq();

			ArrayList<WorkRequest> retworkRequests = workRequestRepository
					.findByRequestID(requestID.toString());

			System.out.println("Work Request ID : "
					+ ((WorkRequest) retworkRequests.get(0)).getRequestID()
					+ " is created.");

			HashMap<String, String> workerOutput = waitForResponse(input);
			workRequest = null;
			disConnectDB();
			return workerOutput;
		} else {
			System.out.println("Database is down");
			return null;

		}

	}

	private boolean isDataBaseAlive() {
		try {

			// httpClient = new StdHttpClient.Builder().url(DB_URL).build();

			httpClient = new StdHttpClient.Builder().url(DB_URL)
					.username(DB_USER).password(DB_PWD).enableSSL(true)
					.relaxedSSLSettings(true).build();

			dbInstance = new StdCouchDbInstance(httpClient);
			brokerdb = dbInstance.createConnector(BROKER_DB, true);
			iodb = dbInstance.createConnector(IO_DB, true);
			if (!dbInstance.checkIfDbExists(new DbPath(BROKER_DB))) {
				return false;
			}
			if (!dbInstance.checkIfDbExists(new DbPath(IO_DB))) {
				return false;
			}
		} catch (DbAccessException connectionException) {
			return false;
		} catch (MalformedURLException urlException) {
			return false;
		}
		return true;
	}

	private HashMap<String, String> waitForResponse(
			HashMap<String, String> input) {

		while (true) {

			ChangesCommand cmd = new ChangesCommand.Builder()
					.since(dbSequenceNumber).includeDocs(true)
					.filter("Worker/assignments_by_request_id")
					.param("requestID", requestID.toString()).build();

			List<DocumentChange> changes = brokerdb.changes(cmd);
			for (DocumentChange change : changes) {

				System.out.println("doc =" + change.getDoc());
				String workAssignmentID = change.getDocAsNode()
						.get("workAssignmentID").getTextValue();
				System.out.println("Broker has created a Work Assignment ID="
						+ workAssignmentID);

				WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(
						brokerdb);

				List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository
						.findByWorkRequestID(requestID.toString());

				WorkAssignment retassignment = (WorkAssignment) assignments
						.get(0);

				if (retassignment.getWorkInputID() == null
						&& retassignment.getAssignmentStatus() != null
						&& retassignment.getAssignmentStatus().equals(
								"estimated")) {

					System.out.println("Work Assignmenment Status = "
							+ retassignment.getAssignmentStatus());
					System.out.println("Work Estimate Status = "
							+ retassignment.getEstimateStatus());
					System.out.println("Work Estimate Cost = "
							+ retassignment.getEstimateCost());
					System.out.println("Work Estimate Time = "
							+ retassignment.getEstimateTime());

					if (APPROVE_ESTIMATE) // only for demo..in real life, there
											// should be an interaction.
					{
						System.out.println("Approving Work Estimate...");
						retassignment.setEstimateStatus("approved");
						retassignment.setAssignmentStatus("inputgiven");
						System.out.println("Work is assigned to "
								+ retassignment.getWorkerID());
						System.out.println("WorkRequest  ID =  "
								+ retassignment.getWorkRequestID());

						WorkInputRepository workInputRepository = new WorkInputRepository(
								iodb);
						WorkInput workinput = new WorkInput();
						workinput.setWorkAssignmentID(retassignment
								.getWorkAssignmentID());

						workinput.setInput(input);

						ArrayList authors = new ArrayList();
						authors.add(user.getUserID());

						workinput.setAuthors(authors);

						workInputRepository.add(workinput);
						retassignment
								.setWorkInputID(workinput.getWorkInputID());
						workassignmentRespository.update(retassignment);
						System.out
								.println("Input URL is created and work assignment is updated.");
						System.out.println("Waiting for worker response.");

					} else if (REJECT_ESTIMATE) {
						System.out.println("Rejecting Work Estimate...");
						retassignment.setEstimateStatus("rejected");
						System.out.println("Work is assigned to "
								+ retassignment.getWorkerID());
						System.out.println("WorkRequest  ID =  "
								+ retassignment.getWorkRequestID());
						retassignment.setAssignmentStatus("closed");
						workassignmentRespository.update(retassignment);
						return null;

					}
					dbSequenceNumber = brokerdb.getDbInfo().getUpdateSeq();

				}
				if (retassignment.getWorkOutputID() != null) {
					WorkOutputRepository workOutputRespository = new WorkOutputRepository(
							iodb);

					List<WorkOutput> outputs = (List<WorkOutput>) workOutputRespository
							.findByWorkAssignmentID(workAssignmentID);

					WorkOutput output = (WorkOutput) outputs.get(0);

					System.out.println("WorkOutput ID =  "
							+ output.getWorkOutputID());

					System.out.println("Got response from Worker.");
					HashMap<String, String> results = new HashMap<String, String>();
					results = output.getResults();
					retassignment.setAssignmentStatus("closed");
					workassignmentRespository.update(retassignment);

					return results;

				}
			}
		}

	}

	private void disConnectDB() throws Exception {
		if (dbInstance != null && httpClient != null) {
			httpClient.shutdown();

		}

	}

}

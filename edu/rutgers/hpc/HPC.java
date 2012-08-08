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
	// The user makes a HPC call ( command line) JAVA call to the broker which
	// is a database. This call writes to the broker db as a document of type
	// 'request'.
	// A process /service(on broker) is listening to the database constantly to
	// check if new request is created.
	// When broker reads this document, it chooses a matching worker based on
	// skills mentioned in the request. This creates a new corresponding
	// document of type "assignment" where it writes task id, user id and worker
	// id.
	// The user meanwhile is listening to check if a new assignment type
	// document is created for his task id. When he sees this document in broker
	// db, the user writes his input URL to the io database and updates this
	// assignment document with the link to his input URL and output URL(which
	// at this point is empty).
	//
	// The worker is also constantly checking if a new assignment doc with his
	// worker id is created.
	// When he sees this document ( which now the user has updated with input
	// URL) worker reads from the input URL. Upon completion of his task, he
	// writes to the output URL and updates the status field in the assignment
	// document as complete. On seeing that the status field is completed, the
	// user reads the output URL and gets his result. The broker on seeing
	// status completed, updates worker as 'available'.

	// Broker listens for WorkRequest
	// Worker listens for WorkAssignment
	// Worker listens for WorkInput for the specific WorkAssignment
	// User listens for WorkAssignment for his workRequest
	// User listens for WorkOuput for his WorkRequest + Work Assignment

	private static String DB_URL = "http://127.0.0.1:5984/";
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

	public HPC(User user) throws Exception {
		this.user = user;

	}
 
	
	@Override
	public HashMap<String, String> invoke(String taskDescription,
			HashMap<String, String> input, long deadLine,
			List<String> skillsNeeded, Cost cost) throws Exception {

		if (isDataBaseAlive()) {
		    workRequest = new WorkRequest();

			requestID = UUID.randomUUID();

			workRequest.setRequestID(requestID.toString());
			workRequest.setRequestorID(user.getUserID());
			workRequest.setSkillsNeeded(skillsNeeded);

			WorkRequestRepository workRequestRepository = new WorkRequestRepository(
					brokerdb);

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

			httpClient = new StdHttpClient.Builder().url(DB_URL).build();

			dbInstance = new StdCouchDbInstance(httpClient);
			brokerdb = dbInstance.createConnector(BROKER_DB, true);
			iodb = dbInstance.createConnector(IO_DB, true);
			if (!dbInstance.checkIfDbExists(new DbPath(BROKER_DB)))
			{
				return false;
			}
			if (!dbInstance.checkIfDbExists(new DbPath(IO_DB)))
			{
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
					.since(dbSequenceNumber)
					.includeDocs(true)
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

				if (retassignment.getWorkInputID() == null) {

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
					workInputRepository.add(workinput);
					retassignment.setWorkInputID(workinput.getWorkInputID());
					workassignmentRespository.update(retassignment);
					System.out
							.println("Input URL is created and work assignment is updated.");
					System.out.println("Waiting for worker response.");
					
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

					
					return results;

				}
			}
		}

	}
	
	private void disConnectDB() throws Exception
	{
		if(dbInstance != null  && httpClient != null)
		{
			httpClient.shutdown();
		
	}
	
	}

}

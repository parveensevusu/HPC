package edu.rutgers.hpc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.*;

public class WorkerClient {

	
	public static void main(String[] args) throws Exception
	{
		
		



	//	HttpClient httpClient = new StdHttpClient.Builder().url("http://127.0.0.1:5984/").build();

	//	HttpClient httpClient = new StdHttpClient.Builder().url("http://hpc.iriscouch.com:5984/").build();
	
	//	HttpClient httpClient = new StdHttpClient.Builder().url("https://hpc.iriscouch.com:6984/").username("vpathak").password("vpathak123").enableSSL(true).relaxedSSLSettings(true).build();
	
		HttpClient httpClient = new StdHttpClient.Builder().url("https://127.0.0.1:6984/").username("joe").password("hpc1234").enableSSL(true).relaxedSSLSettings(true).build();
		
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		

	//	HttpClient httpClient2 = new StdHttpClient.Builder().url("http://127.0.0.1:5984/").build();
	
	//	HttpClient httpClient2 = new StdHttpClient.Builder().url("http://hpc.iriscouch.com:5984/").build();
		//HttpClient httpClient2 = new StdHttpClient.Builder().url("https://hpc.iriscouch.com:6984/").username("vpathak").password("vpathak123").enableSSL(true).relaxedSSLSettings(true).build();
		
		HttpClient httpClient2 = new StdHttpClient.Builder().url("https://127.0.0.1:6984/").username("joe").password("hpc1234").enableSSL(true).relaxedSSLSettings(true).build();
		
		CouchDbInstance dbInstance2 = new StdCouchDbInstance(httpClient2);
		

		
		CouchDbConnector db = null;
		CouchDbConnector iodb = null;
				// if the second parameter is true, the database will be created if it doesn't exists
		try{
		  db = dbInstance.createConnector("brokerdb", true);
		  iodb = dbInstance2.createConnector("iodb", true);
		}catch(DbAccessException connectionException)
		{
			System.out.println("CouchDB is down");
		}
		
		
		

		ChangesCommand chgcmd = new ChangesCommand.Builder().continuous(true).heartbeat(10).includeDocs(true).filter("Worker/assignments_by_worker_id").param("workerID", "joe").build();
		ChangesFeed feed = db.changesFeed(chgcmd);
		

		String workAssignmentID = null;
		while (feed.isAlive()) {
		    DocumentChange change = feed.next();
		    String docId = change.getId();
		    System.out.println("docid = "+ docId);
		    System.out.println("doc =" + change.getDoc());
		    
		    String requestID = change.getDocAsNode().get("workRequestID").getTextValue();
		    
		    
		    workAssignmentID = change.getDocAsNode().get("workAssignmentID").getTextValue();
		    System.out.println("work Request ID=" + requestID);
		    
		    
		    WorkRequestRepository workRequestRepository = new WorkRequestRepository(db);
		    
		      
		    
		    
		    
		   

		    List retworkRequests =    workRequestRepository.findByRequestID(requestID);
		    
		    WorkRequest workRequest = (WorkRequest)retworkRequests.get(0);
		    System.out.println("Work Request ID = "+ workRequest.getRequestID());
		 //   workerRepository.remove((Worker) returnedUsers1.get(0));
		  //  userRepository.remove((User) returnedUsers.get(0));
		    

		    
		    WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(db);
		    
		
		    
		 
		    List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository.findByWorkRequestID(requestID);
		    
		    WorkAssignment retassignment = (WorkAssignment) assignments.get(0);
		    
		    System.out.println("Work is assigned to " + retassignment.getWorkerID());
		    System.out.println("Work is assigned to " + retassignment.getWorkAssignmentID());
		    
		    if(retassignment.getAssignmentStatus() != null && retassignment.getAssignmentStatus().equals("assigned") )
		    {
		    	retassignment.setAssignmentStatus("estimated");
		    	java.util.Date date = new java.util.Date();
				java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
				retassignment.setAssignmentStatusTimeStamp(ts.toString());
				retassignment.setEstimateStatusTimeStamp(ts.toString());
				retassignment.setEstimateStatus("waiting");
				retassignment.setEstimateCost("$10");
				retassignment.setEstimateTime("5 min");
				workassignmentRespository.update(retassignment);
				
				System.out.println("WorkAssignment Status = " + retassignment.getAssignmentStatus());
				System.out.println("Work Estimate Status = " + retassignment.getEstimateStatus());
				System.out.println("Work Estimate Cost = " + retassignment.getEstimateCost());
				System.out.println("Work Estimate Time = " + retassignment.getEstimateTime());
			
			
		    }
		    if( retassignment.getAssignmentStatus() != null && 
		    		retassignment.getEstimateStatus().equals("approved") && retassignment.getWorkInputID() != null && retassignment.getWorkOutputID() == null)
		    {
		    	  WorkOutputRepository workOutputRespository = new WorkOutputRepository(iodb);
				    
				   WorkOutput output = new WorkOutput();
				    output.setWorkAssignmentID( workAssignmentID);
				    HashMap<String,String> outputVals = new HashMap<String, String>();
				    outputVals.put("Drive Step 1", "Take I-78 West");
				    outputVals.put("Drive Step 2", "Take I-476 South" );
				    outputVals.put("Drive Step 3", "Take I-95 South");
				    output.setResults(outputVals);
				    
				    ArrayList authors = new ArrayList();
					authors.add(retassignment.getWorkerID());
					
					output.setAuthors(authors);
				    workOutputRespository.add(output);
				    retassignment.setWorkOutputID(output.getWorkOutputID());
				    retassignment.setAssignmentStatus("outputgiven");
				    workassignmentRespository.update(retassignment);
		    }
		    
		    if( retassignment.getAssignmentStatus() != null && 
		    		retassignment.getEstimateStatus().equals("rejected") )
		    {
		    	System.out.println("Work Estimate is Rejected by the user");
		    }
		    
		  //  feed.cancel();
		    
		   
		    
		   
		
		}
		
//		

//		if(workAssignmentID != null)
//		{
//	  //  ChangesCommand chgcmdworkinput = new ChangesCommand.Builder().includeDocs(true).filter("WorkInput/input_by_assignment_id").param("workAssignmentID", workAssignmentID).build();
//			  ChangesCommand chgcmdworkinput = new ChangesCommand.Builder().includeDocs(true).build();
//				
//			ChangesFeed workinputfeed = iodb.changesFeed(chgcmdworkinput);
//		
//		while ( workinputfeed.isAlive()) {
//		    DocumentChange inputchange = workinputfeed.next();
//		    String inputdocId = inputchange.getId();
//		    System.out.println("docid = "+ inputdocId);
//		    System.out.println("doc =" + inputchange.getDoc());
//		    
//		    String inputworkAssignmentID = inputchange.getDocAsNode().get("workAssignmentID").getTextValue();
//		    System.out.println("input work Assignment iD=" + inputworkAssignmentID);
//		    
//		    
//		   
//		    
//
//		    
//		    WorkInputRepository workInputRespository = new WorkInputRepository(iodb);
//		    
//		
//		    
//		 
//		    List<WorkInput> inputs = (List<WorkInput>) workInputRespository.findByWorkAssignmentID(inputworkAssignmentID);
//		    
//		    WorkInput input1 = (WorkInput) inputs.get(0);
//		    
//		   
//		    System.out.println("WorkAssignment ID =  " + input1.getWorkAssignmentID());
//		    
//		    HashMap<String,String> inputVals = new HashMap<String,String>();
//		    inputVals = input1.getInput();
//		    
//		    for (Map.Entry<String, String> entry : inputVals.entrySet()) { 
//		        String key = entry.getKey(); 
//		        String value = entry.getValue(); 
//		        System.out.println("input Key = " + key);
//		    	System.out.println("input Value = " + value);
//		    }
//		    		    WorkOutputRepository workOutputRespository = new WorkOutputRepository(iodb);
//		    
//			   WorkOutput output = new WorkOutput();
//			    output.setWorkAssignmentID( workAssignmentID);
//			    HashMap<String,String> outputVals = new HashMap<String, String>();
//			    outputVals.put("Drive Step 1", "Take I-78 West");
//			    outputVals.put("Drive Step 2", "Take I-476 South" );
//			    outputVals.put("Drive Step 3", "Take I-95 South");
//			    output.setResults(outputVals);
//			    
//			    workOutputRespository.add(output);
//			  //  workinputfeed.cancel();
//			    break;
//		   
//		    
//		} //End of While
//		} //End of if
	    

	  
	    
	}
}

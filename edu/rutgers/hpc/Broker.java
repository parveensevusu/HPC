package edu.rutgers.hpc;

import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
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

public class Broker {

	
	public static void main(String[] args) throws Exception
	{
		
		



	//	HttpClient httpClient = new StdHttpClient.Builder().url("http://127.0.0.1:5984/").build();
	
	//	HttpClient httpClient = new StdHttpClient.Builder().url("http://hpc.iriscouch.com:5984/").build();
		HttpClient httpClient = new StdHttpClient.Builder().url("https://hpc.iriscouch.com:6984/").username("vpathak").password("vpathak123").enableSSL(true).relaxedSSLSettings(true).build();
		
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector db = null;
				// if the second parameter is true, the database will be created if it doesn't exists
		try{
		  db = dbInstance.createConnector("brokerdb", true);
		}catch(DbAccessException connectionException)
		{
			System.out.println("CouchDB is down");
		}
		ChangesCommand chgcmd = new ChangesCommand.Builder().continuous(true).heartbeat(10).includeDocs(true).filter("User/by_type").param("type", "WorkRequest").build();
		ChangesFeed feed = db.changesFeed(chgcmd);

		while (feed.isAlive()) {
			 DocumentChange change = null;
			
		     change = feed.next();
			
			
		    String docId = change.getId();
		    System.out.println("docid = "+ docId);
		    System.out.println("doc =" + change.getDoc());
		    
		    String requestID = change.getDocAsNode().get("requestID").getTextValue();
		    System.out.println("work Request ID=" + requestID);
		    
		    
		    WorkRequestRepository workRequestRepository = new WorkRequestRepository(db);
		    
		      
		    
		    
		    
		   

		    List retworkRequests =    workRequestRepository.findByRequestID(requestID);
		    
		    WorkRequest workRequest = (WorkRequest)retworkRequests.get(0);
		    List<String> skillsNeeded = workRequest.getSkillsNeeded();
		    
		    WorkerRepository workerRespository = new WorkerRepository(db);
		    
		    List<Worker> workersAvailable = workerRespository.findBySkill(skillsNeeded.get(0));
		    if(workersAvailable == null || workersAvailable.size() == 0)
		    {
		    	System.out.println("No worker match");
		    	continue;
		    }
		    
		    System.out.println("Work Request ID = "+ workRequest.getRequestID());
		 //   workerRepository.remove((Worker) returnedUsers1.get(0));
		  //  userRepository.remove((User) returnedUsers.get(0));
		    
		    WorkAssignment workAssignment = new WorkAssignment();
		    workAssignment.setRequestorID(workRequest.getRequestorID());
		    workAssignment.setWorkerID(workersAvailable.get(0).getUserID());
		    workAssignment.setWorkRequestID(requestID);
		    
		    WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(db);
		    
		    workassignmentRespository.add(workAssignment);
		    
		 
		    List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository.findByWorkRequestID(requestID.toString());
		    
		    WorkAssignment retassignment = (WorkAssignment) assignments.get(0);
		    
		    System.out.println("Work is assigned to " + retassignment.getWorkerID());
		    System.out.println("WorkAssignment ID = " + retassignment.getWorkAssignmentID());
			
		    
		}

	    
	    
	    
	    
	  
	    
	}
}
package edu.rutgers.hpc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.http.*;

public class UserClient {

	
	public static void main(String[] args) throws Exception
	{
		
		



		HttpClient httpClient = new StdHttpClient.Builder().url("http://127.0.0.1:5984/").build();
	
		
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector db = null;
				// if the second parameter is true, the database will be created if it doesn't exists
		try{
		  db = dbInstance.createConnector("brokerdb", true);
		}catch(DbAccessException connectionException)
		{
			System.out.println("CouchDB is down");
		}
		User user = new User();
		user.setUserID("parveen");
		user.setPassword("abcd1234");
		user.setAddress("7, Main street");
		user.setCity("Woodbridge");
		user.setPostalCode("070955");
		user.setState("NJ");
		user.setEmail("parveen@rutgers.edu");
		user.setPhoneNo("732-123-2222");
		user.setType("user");
	    UserRepository userRepository = new UserRepository(db);
	    userRepository.add(user);
	    
	    ArrayList returnedUsers = (ArrayList) userRepository.findByUserID("parveen");
	    
	    System.out.println("Returned User _id " + ((User) returnedUsers.get(0)).getId());

	 //   User storedUser = userRepository.findByUserID(userID)
	    
	    Worker worker = new Worker();
	    worker.setUserID("joe");
	    worker.setPassword("joe1234");
	    worker.setAddress("30, river rd");
	    worker.setCity("piscatway");
	    worker.setPostalCode("07309");
	    worker.setState("NJ");
	    worker.setEmail("joe@rutgers.edu");
	    worker.setPhoneNo("732-911-2222");
	    worker.setType("worker");
	    List<String> skills = new ArrayList<String>();
	    
	    
	    skills.add("Driving NJ");
	    skills.add("Driving PA");
	    skills.add("Driving OH");
	 
	    
	   
	    skills.add("Teaching Algebra");
	    skills.add("Teaching Calculus");
	    skills.add("Teaching Discrete Mathematics");
	  
	    
	    
	   
	    worker.setSkills(skills);
	    
	    WorkerRepository workerRepository = new WorkerRepository(db);
	    workerRepository.add(worker);
	    
        ArrayList returnedUsers1 = (ArrayList) workerRepository.findByUserID("joe");
	    
	    System.out.println("Returned worker _id " + ((Worker) returnedUsers1.get(0)).getId());
	    System.out.println("Returned worker name " + ((Worker) returnedUsers1.get(0)).getUserID());
	    System.out.println("Returned worker _rev " + ((Worker) returnedUsers1.get(0)).getRevision());
	    
	    
	    WorkRequest workRequest = new WorkRequest();
	    
	    UUID requestID = UUID.randomUUID();
	    
	    workRequest.setRequestID(requestID.toString());
	    workRequest.setRequestorID(user.getUserID());
	    
	    List<String> skillsNeeded = new ArrayList<String>();
	   
	    
	    skillsNeeded.add("Driving NJ");
	    skillsNeeded.add("Driving OH");
	    
	    workRequest.setSkillsNeeded(skillsNeeded);
	   
	    WorkRequestRepository workRequestRepository = new WorkRequestRepository(db);
	    
	    workRequestRepository.add(workRequest);
	    
	    
	    
	    
	   

	    List retworkRequests =    workRequestRepository.findByRequestID(requestID.toString());
	    
	    System.out.println("Work Request ID = "+ ((WorkRequest)retworkRequests.get(0)).getRequestID());
	 //   workerRepository.remove((Worker) returnedUsers1.get(0));
	  //  userRepository.remove((User) returnedUsers.get(0));
	    
	    WorkAssignment workAssignment = new WorkAssignment();
	    workAssignment.setRequestorID("parveen");
	    workAssignment.setWorkerID("joe");
	    workAssignment.setWorkRequestID(requestID.toString());
	    
	    WorkAssignmentRepository workassignmentRespository = new WorkAssignmentRepository(db);
	    
	    workassignmentRespository.add(workAssignment);
	    
	    List<WorkAssignment> assignments = (List<WorkAssignment>) workassignmentRespository.findByWorkRequestID(requestID.toString());
	    
	    WorkAssignment retassignment = (WorkAssignment) assignments.get(0);
	    
	    System.out.println("Work is assigned to " + retassignment.getWorkerID());
	    
	    
	    
	    
	    
	  
	    
	}
}

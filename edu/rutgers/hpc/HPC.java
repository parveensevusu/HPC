package edu.rutgers.hpc;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HPC implements HPCInterface{
	
//	here is an overview of how the system functions:
//
//		The user makes a HPC call ( command line) JAVA call to  the broker which is a database. This call writes to the broker db as a document of type 'request'.
//		A process /service(on broker) is listening to the database constantly to check if new request is created. 
//		When broker reads this document, it chooses a matching worker based on skills mentioned in the request. This creates a new corresponding document of type "assignment" where it writes task id, user id and worker id. 
//		The user meanwhile is listening to check if a new assignment type document is created for his task id. When he sees this document in broker db, the user writes his input URL to the io database and updates this assignment document with the link to his input URL and output URL(which at this point is empty).
//
//		The worker is also constantly checking if a new assignment doc with his worker id is created.
//		When he sees this document ( which now the user has updated with input URL) worker reads from the input URL. Upon completion of his task, he writes to the output URL and updates the status field in the assignment document as complete. On seeing that the status field is completed, the user reads the output URL and gets his result. The broker on seeing status completed, updates worker as 'available'.

//      Broker listens for WorkRequest
//      Worker listens for WorkAssignment
//      Worker listens for WorkInput for the specific WorkAssignment
//      User listens for WorkAssignment for his workRequest
//      User listens for WorkOuput for his WorkRequest + Work Assignment
	
	@Override
	public Map<String, Object> invoke(String taskDescription,
			Map<String, Object> input, Timestamp deadLine, List<String> skills,
			Cost cost) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isBrokerAlive()
	{
		return false;
		
	}
	
	private boolean isDataBaseAlive()
	{
		return false;
	}
	
	private URL createInputURL(Map<String, Object> input)
	{
		return null;
		
	}
	private URL createOutputURL()
	{
		return null;
	}
	
	private String sendRequest(String taskDescription, List<String> skills, Timestamp deadLine, Cost cost, URL input, URL ouput) throws Exception
	{
	   return null;
	}
	
	private void waitForResponse()
	{
		
	}


}


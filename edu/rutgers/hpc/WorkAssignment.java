package edu.rutgers.hpc;


import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkAssignment extends CouchDbDocument {
	
	private String workerID;
	private String requestorID;
	private String workRequestID;
	private String type;
	private String workAssignmentID;
	private String status;
	
	
	public WorkAssignment()
	{
		setType("WorkAssignment");
	}
	public String getWorkerID() {
		return workerID;
	}
	public void setWorkerID(String workerID) {
		this.workerID = workerID;
	}
	public String getRequestorID() {
		return requestorID;
	}
	public void setRequestorID(String requestorID) {
		this.requestorID = requestorID;
	}
	public String getWorkRequestID() {
		return workRequestID;
	}
	public void setWorkRequestID(String workRequestID) {
		this.workRequestID = workRequestID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWorkAssignmentID() {
		return workAssignmentID;
	}
	public void setWorkAssignmentID(String workAssignmentID) {
		this.workAssignmentID = workAssignmentID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}

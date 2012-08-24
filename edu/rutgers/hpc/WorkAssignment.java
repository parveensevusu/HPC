package edu.rutgers.hpc;


import java.util.ArrayList;
import java.util.UUID;

import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkAssignment extends CouchDbDocument {
	
	private String workerID;
	private String requestorID;
	private String workRequestID;
	private String type;
	private String workAssignmentID;
	private String status;
	private String workInputID;
	private String workOutputID;
	private String assignmentStatus;
	
	private String estimateStatus;
	private String assignmentStatusTimeStamp;
	private String estimateStatusTimeStamp;
	private String estimateTime;
	private String estimateCost;
	private ArrayList authors;
	
	public WorkAssignment()
	{
		setType("WorkAssignment");
		  UUID assignmentID = UUID.randomUUID();
		setWorkAssignmentID(assignmentID.toString());
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
	public String getWorkInputID() {
		return workInputID;
	}
	public void setWorkInputID(String workInputID) {
		this.workInputID = workInputID;
	}
	public String getWorkOutputID() {
		return workOutputID;
	}
	public void setWorkOutputID(String workOutputID) {
		this.workOutputID = workOutputID;
	}
	public String getAssignmentStatus() {
		return assignmentStatus;
	}
	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}
	
	public String getEstimateStatus() {
		return estimateStatus;
	}
	public void setEstimateStatus(String estimateStatus) {
		this.estimateStatus = estimateStatus;
	}
	public String getAssignmentStatusTimeStamp() {
		return assignmentStatusTimeStamp;
	}
	public void setAssignmentStatusTimeStamp(String assignmentStatusTimeStamp) {
		this.assignmentStatusTimeStamp = assignmentStatusTimeStamp;
	}
	public String getEstimateStatusTimeStamp() {
		return estimateStatusTimeStamp;
	}
	public void setEstimateStatusTimeStamp(String estimateStatusTimeStamp) {
		this.estimateStatusTimeStamp = estimateStatusTimeStamp;
	}
	public String getEstimateTime() {
		return estimateTime;
	}
	public void setEstimateTime(String estimateTime) {
		this.estimateTime = estimateTime;
	}
	public String getEstimateCost() {
		return estimateCost;
	}
	public void setEstimateCost(String estimateCost) {
		this.estimateCost = estimateCost;
	}
	public ArrayList getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList authors) {
		this.authors = authors;
	}
	

}

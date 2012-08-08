package edu.rutgers.hpc;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkRequest extends CouchDbDocument{
	
	private String requestorID;
	private List<String> skillsNeeded;
	private Cost costEstimate;
	private String type;
	private String requestID;
	private Timestamp deadLine;
	private String taskDescription;
	
	public WorkRequest()
	{
		setType("WorkRequest");
	}
	public String getRequestorID() {
		return requestorID;
	}
	public void setRequestorID(String requestorID) {
		this.requestorID = requestorID;
	}
	public List<String> getSkillsNeeded() {
		return skillsNeeded;
	}
	public void setSkillsNeeded(List<String> skillsNeeded) {
		this.skillsNeeded = skillsNeeded;
	}
	public Cost getCostEstimate() {
		return costEstimate;
	}
	public void setCostEstimate(Cost costEstimate) {
		this.costEstimate = costEstimate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public Timestamp getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(Timestamp deadLine) {
		this.deadLine = deadLine;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	

}

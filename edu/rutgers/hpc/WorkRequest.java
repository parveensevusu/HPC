package edu.rutgers.hpc;

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
	

}

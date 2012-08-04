package edu.rutgers.hpc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkOutput  extends CouchDbDocument 
{
	private String workAssignmentID;
	private HashMap<String,ArrayList> results;
	private Cost actualCost;
	private String type;
	
	
	public WorkOutput()
	{
		setType("WorkOutput");
	}
	public String getWorkAssignmentID() {
		return workAssignmentID;
	}
	public void setWorkAssignmentID(String workAssignmentID) {
		this.workAssignmentID = workAssignmentID;
	}
	public HashMap<String,ArrayList> getResults() {
		return results;
	}
	public void setResults(HashMap<String,ArrayList> results) {
		this.results = results;
	}
	public Cost getActualCost() {
		return actualCost;
	}
	public void setActualCost(Cost actualCost) {
		this.actualCost = actualCost;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

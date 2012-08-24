package edu.rutgers.hpc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkOutput  extends CouchDbDocument 
{
	private String workAssignmentID;
	private HashMap<String,String> results;
	private Cost actualCost;
	private String type;
	private String workOutputID;
	private ArrayList authors;
	
	
	public WorkOutput()
	{
		setType("WorkOutput");
		UUID workOutputUUID = UUID.randomUUID();
		setWorkOutputID(workOutputUUID.toString());
	}
	public String getWorkAssignmentID() {
		return workAssignmentID;
	}
	public void setWorkAssignmentID(String workAssignmentID) {
		this.workAssignmentID = workAssignmentID;
	}
	public HashMap<String,String> getResults() {
		return results;
	}
	public void setResults(HashMap<String,String> results) {
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
	public String getWorkOutputID() {
		return workOutputID;
	}
	public void setWorkOutputID(String workOutputID) {
		this.workOutputID = workOutputID;
	}
	public ArrayList getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList authors) {
		this.authors = authors;
	}
	
}

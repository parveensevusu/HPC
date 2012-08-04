package edu.rutgers.hpc;


import java.util.ArrayList;
import java.util.HashMap;


import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkInput  extends CouchDbDocument 
{
	private String workAssignmentID;
	private HashMap<String,ArrayList> input;
	private String type;
	
	
	public WorkInput()
	{
		setType("WorkInput");
	}
	public String getWorkAssignmentID() {
		return workAssignmentID;
	}
	public void setWorkAssignmentID(String workAssignmentID) {
		this.workAssignmentID = workAssignmentID;
	}
	public HashMap<String, ArrayList> getInput() {
		return input;
	}
	public void setInput(HashMap<String, ArrayList> input) {
		this.input = input;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

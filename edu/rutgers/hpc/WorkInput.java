package edu.rutgers.hpc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;


public class WorkInput  extends CouchDbDocument 
{
	private String workAssignmentID;
	private HashMap<String,String> input;
	private String type;
	private String workInputID;
	
	
	
	public WorkInput()
	{
		setType("WorkInput");
		UUID workInputUUID = UUID.randomUUID();
		setWorkInputID(workInputUUID.toString());
	}
	public String getWorkAssignmentID() {
		return workAssignmentID;
	}
	public void setWorkAssignmentID(String workAssignmentID) {
		this.workAssignmentID = workAssignmentID;
	}
	public HashMap<String, String> getInput() {
		return input;
	}
	public void setInput(HashMap<String, String> input) {
		this.input = input;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWorkInputID() {
		return workInputID;
	}
	public void setWorkInputID(String workInputID) {
		this.workInputID = workInputID;
	}
	
}

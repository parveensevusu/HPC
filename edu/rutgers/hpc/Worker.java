package edu.rutgers.hpc;


import java.util.HashMap;
import java.util.List;


public class Worker extends User {
	
	public Worker()
	{
		setType("Worker");
	}
	private List<String> skills;

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}


}

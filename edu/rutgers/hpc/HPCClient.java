package edu.rutgers.hpc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HPCClient {

	public static void main(String[] args) throws Exception {
		List<String> skillsNeeded = new ArrayList<String>();
		skillsNeeded.add("Driving NJ");
		skillsNeeded.add("Driving OH");

		User user = new User();
		user.setUserID("parveen");

		HPC hpc = new HPC(user);

		String taskDescription = "Finding Directions";
		HashMap<String, String> input = new HashMap<String, String>();
		input.put("Start Location", "Piscatway, NJ");
		input.put("End Location", "Cincinnati, OH");

		for (Map.Entry<String, String> entry : input.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("HPC input Key = " + key);
			System.out.println("HPC input Value = " + value);
		}
		long deadLine = 10000; // milleseconds

		Cost cost = new Cost();
		cost.setWorkHours(1);  //Assuming 1 hour task
		cost.setMoney(new BigDecimal(10.00)); // Money 10 dollar 
		cost.setEffort(10); // Effort scale 1-100, 10 being the least effort

		HashMap<String, String> hpcResponse = hpc.invoke(taskDescription,
				input, deadLine, skillsNeeded, cost);

		for (Map.Entry<String, String> entry : hpcResponse.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("HPC Response Key = " + key);
			System.out.println("HPC Response Value = " + value);
		}
	}

}

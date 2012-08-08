package edu.rutgers.hpc;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

public interface HPCInterface {
	
	


	public HashMap<String, String> invoke(String taskDescription,
			HashMap<String, String> input, long deadLine,
			List<String> skillsNeeded, Cost cost) throws Exception;

}

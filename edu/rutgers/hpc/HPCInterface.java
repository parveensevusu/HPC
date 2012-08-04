package edu.rutgers.hpc;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

public interface HPCInterface {
	
	public Map<String, Object> invoke(String taskDescription,Map<String,Object> input, Timestamp deadLine, List<String> skills, Cost cost) ;

}

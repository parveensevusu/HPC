function (doc,req)

{
		if(doc.type == "WorkInput" )
			{
			  if(doc.workAssignmentID)
			  {
			     if (doc.workAssignmentID == req.query.workAssignmentID)
			    	 {
				    	 return true;
			    	 }
			  }
			}
		return false;
		
}
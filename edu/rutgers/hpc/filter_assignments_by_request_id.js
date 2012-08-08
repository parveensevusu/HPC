function (doc,req)

{
		if(doc.type == "WorkAssignment" )
			{
			  if(doc.workRequestID)
				  {
				     if (doc.workRequestID == req.query.requestID)
				    	 {
				    	 return true;
				    	 }
				  }
			}
		return false;
		
}
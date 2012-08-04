function (doc,req)

{
		if(doc.type == req.query.type)
			{
				return true;
			}
		return false;
		
}
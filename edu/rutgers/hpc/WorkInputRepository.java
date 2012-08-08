package edu.rutgers.hpc;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.Filter;
import org.ektorp.support.Filters;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@View ( name = "workinput_by_workAssignmentID",  map = "function(doc) { if(doc.type == 'WorkInput' && doc.workAssignmentID)   {  emit(doc.workAssignmentID, doc._id); } } ")
@Filters( {
	  
	   @Filter( name = "output_by_assignment_id", file="filter_output_by_assignment_id.js" ),
	   @Filter( name = "input_by_assignment_id", file="filter_input_by_assignment_id.js" )
	  
	   } )
@Component
public class WorkInputRepository extends CouchDbRepositorySupport<WorkInput> {

	    @Autowired	
        public WorkInputRepository(CouchDbConnector db) {
                super(WorkInput.class, db);
                initStandardDesignDocument();
        }
         
	   
	    public ArrayList<WorkInput> findByWorkAssignmentID(String workAssignmentID)
	    {
            return (ArrayList<WorkInput>) queryView("workinput_by_workAssignmentID", workAssignmentID);
        }
	    
	    @GenerateView
        public List<WorkInput> findByWorkInputID(String workInputID) {
                return  queryView("by_workInputID", workInputID);
        }
}
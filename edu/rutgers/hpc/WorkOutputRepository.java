package edu.rutgers.hpc;
import java.util.List;
import java.util.ArrayList;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.Filter;
import org.ektorp.support.Filters;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@View ( name = "workoutput_by_workAssignmentID",  map = "function(doc) { if(doc.type == 'WorkOutput' && doc.workAssignmentID)   {  emit(doc.workAssignmentID, doc._id); } } ")

@Component
public class WorkOutputRepository extends CouchDbRepositorySupport<WorkOutput> {

	    @Autowired	
        public WorkOutputRepository(CouchDbConnector db) {
                super(WorkOutput.class, db);
                initStandardDesignDocument();
        }

	    
	    public ArrayList<WorkOutput> findByWorkAssignmentID(String workAssignmentID) {
            return (ArrayList<WorkOutput>) queryView("workoutput_by_workAssignmentID", workAssignmentID);
         }
	    @GenerateView
        public List<WorkOutput> findByWorkOutputID(String workOutputID) {
                return  queryView("by_workOutputID", workOutputID);
        }
}
package edu.rutgers.hpc;
import java.awt.List;
import java.util.ArrayList;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkInputRepository extends CouchDbRepositorySupport<WorkInput> {

	    @Autowired	
        public WorkInputRepository(CouchDbConnector db) {
                super(WorkInput.class, db);
                initStandardDesignDocument();
        }
         
	    @GenerateView
	    public ArrayList<WorkInput> findByWorkAssignmentID(String workAssignmentID) {
            return (ArrayList<WorkInput>) queryView("by_workAssignmentID", workAssignmentID);
    }
}
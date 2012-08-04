package edu.rutgers.hpc;
import java.awt.List;
import java.util.ArrayList;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkOutputRepository extends CouchDbRepositorySupport<WorkOutput> {

	    @Autowired	
        public WorkOutputRepository(CouchDbConnector db) {
                super(WorkOutput.class, db);
                initStandardDesignDocument();
        }

	    @GenerateView
	    public ArrayList<WorkOutput> findByWorkAssignmentID(String workAssignmentID) {
            return (ArrayList<WorkOutput>) queryView("by_workAssignmentID", workAssignmentID);
    }
}
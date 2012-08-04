package edu.rutgers.hpc;
import java.awt.List;
import java.util.ArrayList;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.Filter;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Filter( name = "assignments_by_worker_id", file="filter_assignments_by_worker_id.js" )
@Component
public class WorkRequestRepository extends CouchDbRepositorySupport<WorkRequest> {

	    @Autowired
        public WorkRequestRepository(CouchDbConnector db) {
                super(WorkRequest.class, db);
                initStandardDesignDocument();
        }

	    @GenerateView
        public ArrayList<WorkRequest> findByRequestorID(String requestorID) {
                return (ArrayList<WorkRequest>) queryView("by_requestorID", requestorID);
        }
        
	    @GenerateView
        public ArrayList<WorkRequest> findByRequestID(String requestID) {
            return (ArrayList<WorkRequest>) queryView("by_requestID", requestID);
    }
}
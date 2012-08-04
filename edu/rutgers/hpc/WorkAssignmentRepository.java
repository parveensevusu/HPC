package edu.rutgers.hpc;
import java.util.ArrayList;


import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkAssignmentRepository extends CouchDbRepositorySupport<WorkAssignment> {

	    @Autowired
        public WorkAssignmentRepository(CouchDbConnector db) {
                super(WorkAssignment.class, db);
                initStandardDesignDocument();
        }

	    @GenerateView
        public ArrayList<WorkAssignment> findByRequestorID(String requestorID) {
                return (ArrayList<WorkAssignment>) queryView("by_requestorID", requestorID);
        }
	    
	    @GenerateView
        public ArrayList<WorkAssignment> findByWorkerID(String workerID) {
                return (ArrayList<WorkAssignment>) queryView("by_workerID", workerID);
        }
	    
	    @GenerateView
        public ArrayList<WorkAssignment> findByWorkRequestID(String workRequestID) {
                return (ArrayList<WorkAssignment>) queryView("by_workRequestID", workRequestID);
        }
}
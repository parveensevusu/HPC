package edu.rutgers.hpc;
import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.Filter;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Filter( name = "assignments_by_worker_id", file="filter_assignments_by_worker_id.js" )
@Component
public class WorkerRepository extends CouchDbRepositorySupport<Worker> {

	    @Autowired
        public WorkerRepository(CouchDbConnector db) {
                super(Worker.class, db);
                initStandardDesignDocument();
        }

	    @GenerateView
        public List<Worker> findByUserID(String userID) {
                return (List<Worker>) queryView("by_userID", userID);
        }
	   
}
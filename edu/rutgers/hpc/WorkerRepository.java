package edu.rutgers.hpc;
import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.Filter;
import org.ektorp.support.Filters;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@View ( name = "worker_by_skill",  map = "function(doc) { if(doc.type == 'worker' && doc.skills)  { for(skill in doc.skills) {  emit(doc.skills[skill], doc); } }} ")
@Filters( {
   @Filter( name = "assignments_by_worker_id", file="filter_assignments_by_worker_id.js" ),
   @Filter( name = "assignments_by_request_id", file="filter_assignments_by_request_id.js" )
  
   } )
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
	    public List<Worker> findBySkill(String skill) {
            return queryView("worker_by_skill", skill);
    }
	   
}
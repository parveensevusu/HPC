package edu.rutgers.hpc;


import java.util.ArrayList;
import java.util.List;

import org.ektorp.*;
import org.ektorp.support.*;
import org.ektorp.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Filter( name = "by_type", file="filter_by_type.js" )

@Component
public class UserRepository extends CouchDbRepositorySupport<User> {
	
	    @Autowired
        public UserRepository(CouchDbConnector db) {
                super(User.class, db);
                initStandardDesignDocument();

        }
        
	    @GenerateView
        public List<User> findByUserID(String userID) {
                return (List<User>) queryView("by_userID", userID);
        }
}
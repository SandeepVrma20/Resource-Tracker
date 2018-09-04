/**
 * 
 */
package com.nl.abnamro.services;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.nl.abnamro.entity.ResourceDetails;

/**
 * @author C33129
 *
 */
@Repository
public interface ResouceTrackerServices  extends MongoRepository<ResourceDetails, String>{
	
}

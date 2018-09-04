/**
 * 
 */
package com.nl.abnamro.dataaccess;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.nl.abnamro.entity.ResourceDetails;

/**
 * @author C33129
 *
 */
@Repository
public class ResourceTrackerDALImpl implements ResourceTrackerDAL{
	
	@Autowired
	private  MongoTemplate mongoTemplate;
	

	@Override
	public ResourceDetails findOne(ResourceDetails resource) {
		System.out.println("inside find one");
		BasicDBObject docs = new BasicDBObject();
		docs.put("_id", resource.get_id());
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(resource.get_id()));
		ResourceDetails val=mongoTemplate.findOne(query, ResourceDetails.class);
		return val;
	}


	@Override
	public void saveUser(ResourceDetails resource) {
		resource.set_id(nextNumber());
		mongoTemplate.save(resource);
	}

	
	  /**
	   * The method gives unique number .
	   *
	   * @return the int
	   */
	  public static int nextNumber() {
	    String dateFormat = new SimpleDateFormat("yyyyMM-ddHHmm-ssSSS", Locale.ENGLISH).format(new Date());
	    String[] dateSplit = dateFormat.split("-");
	    return Integer.parseInt(dateSplit[0]) + Integer.parseInt(dateSplit[1]) + Integer.parseInt(dateSplit[2]);
	  }
	
	@Override
	public ResourceDetails updateUser(ResourceDetails resource) {
		mongoTemplate.save(resource);
		return findOne(resource);
	}
	
	@Override
	public ResourceDetails deleteUser(ResourceDetails resource) {
		BasicDBObject docs = new BasicDBObject();
		docs.put("_id", resource.get_id());
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(resource.get_id()));
		return mongoTemplate.findAndRemove(query, ResourceDetails.class);
		
		
		 
	}

}

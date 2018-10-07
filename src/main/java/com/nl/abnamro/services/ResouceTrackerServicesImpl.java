/**
 * 
 */
package com.nl.abnamro.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import com.mongodb.BasicDBObject;
import com.nl.abnamro.entity.ResourceDetails;

/**
 * @author C33129
 *
 */
public class ResouceTrackerServicesImpl {

  @Autowired
  private ResouceTrackerServices resouceTrackerServices;


  public void saveUser(ResourceDetails resource) {
    resouceTrackerServices.save(resource);

  }


  @SuppressWarnings("unchecked")
  public Optional<ResourceDetails> getUser(ResourceDetails details) {

    BasicDBObject query = new BasicDBObject();
    query.put("firstName", details.getFirstName());
    ResourceDetails resource = new ResourceDetails();
    // resource= resouceTrackerServices.findOne(query);
    System.out.println(resource.getFileName());
    return resouceTrackerServices.findById("5b77f20d105ae01f8c544380");
    // BasicDBObject query = new BasicDBObject();
    // query.put("fileName", "Sandeep_pic.jpg");

  }
}

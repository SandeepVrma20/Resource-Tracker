package com.nl.abnamro.dataaccess;

import com.nl.abnamro.entity.ResourceDetails;

/**
 * @author C33129
 *
 */
public interface ResourceTrackerDAL {
	
	public ResourceDetails findOne(ResourceDetails resource);
	
	public void saveUser(ResourceDetails resource);
	
	public ResourceDetails updateUser(ResourceDetails resource);
	
	public ResourceDetails deleteUser(ResourceDetails resource);


}

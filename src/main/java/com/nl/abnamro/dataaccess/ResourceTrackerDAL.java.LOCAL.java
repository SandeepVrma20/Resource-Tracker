package com.nl.abnamro.dataaccess;

import java.util.List;

import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.TotalRequierments;

/**
 * @author C33129
 *
 */
public interface ResourceTrackerDAL {

	
	public List<ResourceDetails> findAll();
	
	public ResourceDetails findOne(ResourceDetails resource);
	
	public void saveUser(ResourceDetails resource);
	
	public ResourceDetails updateUser(ResourceDetails resource);
	
	public ResourceDetails deleteUser(ResourceDetails resource);
	
	public List<RequirementDetailsJO> findAllRequierments();
	
	public boolean saveRequierments(RequirementDetailsJO requirementDetails);
	
	public List<TotalRequierments> findAllGroupedReq();
	
	public List<RequirementDetailsJO> findReqBySkill(String skillCategory);
	
	public boolean updateRequierments(RequirementDetailsJO requirementDetails);

	public RequirementDetailsJO findRequirementById(Long reqId);


}

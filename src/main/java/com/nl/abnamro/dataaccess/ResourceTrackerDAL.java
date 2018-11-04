package com.nl.abnamro.dataaccess;

import java.util.List;

import com.nl.abnamro.entity.LoginDetailsJO;
import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.TotalRequirements;

/**
 * @author C33129
 *
 */
public interface ResourceTrackerDAL {

	public List<ResourceDetails> findAll();
	
	public ResourceDetails findOne(ResourceDetails resource);
	
	public void createEmployee(ResourceDetails resource);
	
	public ResourceDetails updateUser(ResourceDetails resource);
	
	public ResourceDetails deleteUser(ResourceDetails resource);
	
	public List<RequirementDetailsJO> findAllRequierments();
	
	public boolean saveRequierments(RequirementDetailsJO requirementDetails);
	
	public List<TotalRequirements> findAllGroupedReq();
	
	public List<RequirementDetailsJO> findReqBySkill(String skillCategory);
	
	public boolean updateRequierments(RequirementDetailsJO requirementDetails);

	public RequirementDetailsJO findRequirementById(Long reqId);
	
	public boolean createUser(LoginDetailsJO loginDetails);
	
	public LoginDetailsJO getUserById(LoginDetailsJO loginDetails);



}

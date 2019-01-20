package com.nl.abnamro.dataaccess;


import java.sql.Date;
import java.util.List;

import com.nl.abnamro.entity.LoginDetailsJO;
import com.nl.abnamro.entity.RequirementDetails;
import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.SkillCategoryJO;
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
	
	public boolean saveRequierments(RequirementDetails requirementDetails);
	
	public List<TotalRequirements> findAllGroupedReq(String filterType);
	
	public List<RequirementDetailsJO> findReqByFilterType(String filterType,String filterValue);
	
	public boolean updateRequierments(RequirementDetailsJO requirementDetails);

	public RequirementDetailsJO findRequirementById(Long reqId);
	
	public boolean createUser(LoginDetailsJO loginDetails);
	
	public LoginDetailsJO getUserById(LoginDetailsJO loginDetails);
	
	public List<String> getSkillCategory();
	
	public List<TotalRequirements> findReqByDates(Date startDate,Date endDate,String status,String dashboradType);




}

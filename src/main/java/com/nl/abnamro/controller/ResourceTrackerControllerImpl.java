/**
 * 
 */
package com.nl.abnamro.controller;

import com.nl.abnamro.entity.RequirementDetails;
import com.nl.abnamro.entity.RequirementDetailsJO;

/**
 * @author C33129
 *
 */
public class ResourceTrackerControllerImpl {
	
	public static final String OPEN_DATE_FORMAT="EEE MMM d HH:mm:ss zzz yyyy";
	
	public RequirementDetails getRequirementDetail(RequirementDetailsJO requirementJo){
		
		RequirementDetails requirementDetail= new RequirementDetails();
		
		requirementDetail.setAccount(requirementJo.getAccount());
		requirementDetail.setAdditionalSkill(requirementJo.getAdditionalSkill());
		requirementDetail.setAllocationDate(requirementJo.getAllocationDate());
		requirementDetail.setClosedBy(requirementJo.getClosedBy());
		requirementDetail.setClosedDate(requirementJo.getClosedDate());
		requirementDetail.setContractor(requirementJo.getContractor());
		requirementDetail.setDomain(requirementJo.getDomain());
		requirementDetail.setEmployeeId(requirementJo.getEmployeeId());
        requirementDetail.setEucRefId(requirementJo.getEucRefId());
        requirementDetail.setExpBand(requirementJo.getExpBand());
        requirementDetail.setLocation(requirementJo.getLocation());
        requirementDetail.setMainSkill(requirementJo.getMainSkill());
        requirementDetail.setOnboardDate(requirementJo.getOnboardDate());
        
		requirementDetail.setOpenDate(convertUtilToSqlDate(requirementJo.getOpenDate()));
		
		requirementDetail.setStartDate(convertUtilToSqlDate(requirementJo.getStartDate()));
		
		requirementDetail.setPosition(requirementJo.getPosition());
		requirementDetail.setPositionOwner(requirementJo.getPositionOwner());
		requirementDetail.setProjectName(requirementJo.getProjectName());
		requirementDetail.setRemarks(requirementJo.getRemarks());
		requirementDetail.setReqClass(requirementJo.getReqClass());
		requirementDetail.setReqId(requirementJo.getReqId());
		requirementDetail.setResourceName(requirementJo.getResourceName());
		requirementDetail.setRevenueWithinQtr(requirementJo.getRevenueWithinQtr());
		requirementDetail.setRgsId(requirementJo.getRgsId());
		requirementDetail.setSite(requirementJo.getSite());
		requirementDetail.setSkillCategory(requirementJo.getSkillCategory());
		
		requirementDetail.setStatus(requirementJo.getStatus());
		requirementDetail.setThrough(requirementJo.getThrough());
		requirementDetail.setTrainee(requirementJo.getTrainee());
		requirementDetail.setWon(requirementJo.getWon());
		
	return requirementDetail;
		
	}
	
	private java.sql.Date convertUtilToSqlDate(java.util.Date utilDate){
		if(null!=utilDate) {
			return new java.sql.Date(utilDate.getTime());
		}
		return null;
		
	  }

}
;
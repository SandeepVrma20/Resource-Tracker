/**
 * 
 */
package com.nl.abnamro.dataaccess;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.RequirementGrpJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.TotalRequierments;

/**
 * @author C33129
 *
 */
@Repository
public class ResourceTrackerDALImpl implements ResourceTrackerDAL {

  @Autowired
  private MongoTemplate mongoTemplate;


  @Override
  public ResourceDetails findOne(ResourceDetails resource) {
    System.out.println("inside find one");
    BasicDBObject docs = new BasicDBObject();
    docs.put("_id", resource.get_id());
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").in(resource.get_id()));
    ResourceDetails val = mongoTemplate.findOne(query, ResourceDetails.class);
    return val;
  }

  @Override
  public List<ResourceDetails> findAll() {
    System.out.println("inside find All");
    List<ResourceDetails> resourceList = mongoTemplate.findAll(ResourceDetails.class);
    System.out.println("resourceList------->" + resourceList.size());
    return resourceList;
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
	public List<RequirementDetailsJO> findAllRequierments() {
		System.out.println("inside findAllRequierments method");
		 List<RequirementDetailsJO> requiermentList=mongoTemplate.findAll(RequirementDetailsJO.class);
		 System.out.println("resourceList------->"+ requiermentList.size());
		return requiermentList;
	}

	@Override
	public boolean saveRequierments(RequirementDetailsJO requirementDetails) {
		
		BasicDBObject docs = new BasicDBObject();
		docs.put("rgsId", requirementDetails.getRgsId());
		docs.put("reqId", requirementDetails.getReqId());
		Query query = new Query();
		query.addCriteria(Criteria.where("rgsId").in(requirementDetails.getRgsId()).andOperator(Criteria.where ("reqId").in(requirementDetails.getReqId())));
		RequirementDetailsJO val=mongoTemplate.findOne(query, RequirementDetailsJO.class);
		if(null!=val){
				return false;
		}else{
			mongoTemplate.save(requirementDetails);
			return true;
			}
	}

	@Override
	public List<TotalRequierments> findAllGroupedReq() {
		System.out.println("inside findAllRequierments method");
		
		GroupOperation group =  Aggregation.group("skillCategory").count().as("total");

		Aggregation aggregation  = Aggregation.newAggregation(group);
		
		AggregationResults<RequirementGrpJO> requiermentList =mongoTemplate.aggregate(aggregation, RequirementGrpJO.class, RequirementGrpJO.class);
		
		 System.out.println("resourceList------->"+ requiermentList.getMappedResults().size());
		List<RequirementGrpJO> requierments= requiermentList.getMappedResults();
		
		List<TotalRequierments> reqList= new ArrayList<TotalRequierments>();
		
		if(null!=requierments && !requierments.isEmpty()){
			for(RequirementGrpJO req:requierments){
				TotalRequierments requierment= new TotalRequierments();
				requierment.setMainSkill(req.get_id());
				requierment.setCount(req.getTotal());
				
				reqList.add(requierment);
			}
		}
		return reqList;
	}

	@Override
	public List<RequirementDetailsJO> findReqBySkill(String skillCategory) {
		System.out.println("inside find one");
		Query query = new Query();
		query.addCriteria(Criteria.where("skillCategory").in(skillCategory));
		List<RequirementDetailsJO> requiermentDetails=mongoTemplate.find(query, RequirementDetailsJO.class);
		return requiermentDetails;
	}

	

  @Override
  public ResourceDetails deleteUser(ResourceDetails resource) {
    BasicDBObject docs = new BasicDBObject();
    docs.put("_id", resource.get_id());
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").in(resource.get_id()));
    return mongoTemplate.findAndRemove(query, ResourceDetails.class);
  }

@Override
public boolean updateRequierments(RequirementDetailsJO requirementDetails) {

	BasicDBObject docs = new BasicDBObject();
	docs.put("reqId", requirementDetails.getReqId());
	Query query = new Query();
	query.addCriteria(Criteria.where ("reqId").in(requirementDetails.getReqId()));
	query.fields().include("reqId");
	Update update= new Update();
	update.set("account", requirementDetails.getAccount());
	update.set("positionOwner", requirementDetails.getPositionOwner());
	update.set("rgsId", requirementDetails.getRgsId());
	update.set("openDate", requirementDetails.getOpenDate());
	update.set("position", requirementDetails.getPosition());
	update.set("skillCategory", requirementDetails.getSkillCategory());
	update.set("mainSkill", requirementDetails.getMainSkill());
	update.set("additionalSkill", requirementDetails.getAdditionalSkill());
	update.set("domain", requirementDetails.getDomain());
	update.set("projectName", requirementDetails.getProjectName());
	update.set("expBand", requirementDetails.getExpBand());
	update.set("eucRefId", requirementDetails.getEucRefId());
	update.set("site", requirementDetails.getSite());
	update.set("location", requirementDetails.getLocation());
	update.set("startDate", requirementDetails.getStartDate());
	update.set("reqType", requirementDetails.getReqType());
	update.set("reqClass", requirementDetails.getReqClass());
	update.set("contractor", requirementDetails.getContractor());
	update.set("trainee", requirementDetails.getTrainee());
	update.set("revenueWithinQtr", requirementDetails.getRevenueWithinQtr());
	update.set("status", requirementDetails.getStatus());
	update.set("employeeId", requirementDetails.getEmployeeId());
	update.set("resourceName", requirementDetails.getResourceName());
	update.set("closedDate", requirementDetails.getClosedDate());
	update.set("onboardDate", requirementDetails.getOnboardDate());
	update.set("won", requirementDetails.getWon());
	update.set("allocationDate", requirementDetails.getAllocationDate());
	update.set("closedBy", requirementDetails.getClosedBy());
	update.set("remarks", requirementDetails.getRemarks());
	UpdateResult val=mongoTemplate.updateFirst(query, update, RequirementDetailsJO.class);
	System.out.println("val-->" +val);
	if(null!=val && val.getMatchedCount()==val.getModifiedCount()){
		return true;
	}else{
		return false;
	}
		
}

@Override
public RequirementDetailsJO findRequirementById(Long reqId) {
  System.out.println("inside find one");
  BasicDBObject docs = new BasicDBObject();
  docs.put("reqId", reqId);
  Query query = new Query();
  query.addCriteria(Criteria.where("reqId").in(reqId));
  RequirementDetailsJO val = mongoTemplate.findOne(query, RequirementDetailsJO.class);
  return val;
}

}

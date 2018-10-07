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
import org.springframework.stereotype.Repository;
import com.mongodb.BasicDBObject;
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
	public String saveRequierments(RequirementDetailsJO requirementDetails) {
		
		BasicDBObject docs = new BasicDBObject();
		docs.put("rgsId", requirementDetails.getRgsId());
		docs.put("reqId", requirementDetails.getReqId());
		Query query = new Query();
		query.addCriteria(Criteria.where("rgsId").in(requirementDetails.getRgsId()).andOperator(Criteria.where ("reqId").in(requirementDetails.getReqId())));
		RequirementDetailsJO val=mongoTemplate.findOne(query, RequirementDetailsJO.class);
		if(null!=val){
			return "Requirement is already present against the requirement id.!!!" ;
		}else{
			mongoTemplate.save(requirementDetails);
			return "Requirement Saved Successfully!!!!";
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

}

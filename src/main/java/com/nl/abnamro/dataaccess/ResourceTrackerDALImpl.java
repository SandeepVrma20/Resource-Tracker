/**
 * 
 */
package com.nl.abnamro.dataaccess;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.nl.abnamro.entity.LoginDetailsJO;
import com.nl.abnamro.entity.RequirementDetails;
import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.RequirementGrpJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.SkillCategoryJO;
import com.nl.abnamro.entity.TotalRequirements;

/**
 * @author C33129
 *
 */
@Repository
public class ResourceTrackerDALImpl implements ResourceTrackerDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	public static final String OPEN_DATE_FORMAT="EEE MMM d HH:mm:ss zzz yyyy";
	private static final String OPEN="Open";
	private static final String CLOSED="Closed";
	private static final String OFFSHORE="Offshore";
	private static final String ONSITE="Onsite";


	@Override
	public ResourceDetails findOne(ResourceDetails resource) {
		BasicDBObject docs = new BasicDBObject();
		docs.put("_id", resource.get_id());
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(resource.get_id()));
		ResourceDetails val = mongoTemplate.findOne(query, ResourceDetails.class);
		return val;
	}

	@Override
	public List<ResourceDetails> findAll() {
		List<ResourceDetails> resourceList = mongoTemplate.findAll(ResourceDetails.class);
		return resourceList;
	}

	@Override
	public List<String> getSkillCategory() {
		List<SkillCategoryJO> skillCategoryLists = mongoTemplate.findAll(SkillCategoryJO.class);
		List<String> allSkills =new ArrayList<>();
		for(SkillCategoryJO skills: skillCategoryLists){
			allSkills.add(skills.getSkillCategory());
		}
		return allSkills;
	}


	@Override
	public void createEmployee(ResourceDetails resource) {
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


	private java.sql.Date convertUtilToSqlDate(java.util.Date utilDate){
		return new java.sql.Date(utilDate.getTime());
	}

	@Override
	public List<RequirementDetailsJO> findAllRequierments() {
		List<RequirementDetailsJO> requiermentList=mongoTemplate.findAll(RequirementDetailsJO.class);
		List<RequirementDetailsJO> requiermentDetailList = new ArrayList<>();

		for(RequirementDetailsJO requirements: requiermentList){
			if(null!=requirements.getOpenDate()){
				requirements.setOpenDate(convertUtilToSqlDate(requirements.getOpenDate()));
			}

			if(null!=requirements.getStartDate()){
				requirements.setStartDate(convertUtilToSqlDate(requirements.getStartDate()));
			}
			requiermentDetailList.add(requirements);
		}

		return requiermentList;
	}

	@Override
	public boolean saveRequierments(RequirementDetails requirementDetails) {

		BasicDBObject docs = new BasicDBObject();
		docs.put("rgsId", requirementDetails.getRgsId());
		docs.put("reqId", requirementDetails.getReqId());
		Query query = new Query();
		query.addCriteria(Criteria.where("rgsId").in(requirementDetails.getRgsId()).andOperator(Criteria.where ("reqId").in(requirementDetails.getReqId())));
		RequirementDetailsJO val=mongoTemplate.findOne(query, RequirementDetailsJO.class);
		if(null!=val){
			return false;
		}else{
			requirementDetails.set_id(generateSequence());
			mongoTemplate.save(requirementDetails);
			return true;
		}
	}

	@Override
	public List<TotalRequirements> findAllGroupedReq(String filterType) {
		GroupOperation group =null;
		if(null!=filterType && filterType.equalsIgnoreCase("skillwise")){
			group =  Aggregation.group("skillCategory").count().as("total");
		}else if(null!=filterType && filterType.equalsIgnoreCase("domainwise")){
			group =  Aggregation.group("domain").count().as("total");
		}else if(null!=filterType && filterType.equalsIgnoreCase("projectwise")){
			group =  Aggregation.group("projectName").count().as("total");
		}else if(null!=filterType && filterType.equalsIgnoreCase("ownerwise")){
			group =  Aggregation.group("positionOwner").count().as("total");
		}

		Aggregation aggregation  = Aggregation.newAggregation(group);
		AggregationResults<RequirementGrpJO> requiermentList =mongoTemplate.aggregate(aggregation, 
				RequirementGrpJO.class, RequirementGrpJO.class);
		List<RequirementGrpJO> requierments= requiermentList.getMappedResults();
		List<TotalRequirements> reqList= new ArrayList<TotalRequirements>();
		if(null!=requierments && !requierments.isEmpty()){
			for(RequirementGrpJO req:requierments){
				TotalRequirements requierment= new TotalRequirements();
				requierment.setCount(req.getTotal());
				if(null!=filterType && filterType.equalsIgnoreCase("skillwise"))
					requierment.setMainSkill(req.get_id());
				if(null!=filterType && filterType.equalsIgnoreCase("domainwise"))
					requierment.setDomain(req.get_id());
				if(null!=filterType && filterType.equalsIgnoreCase("ownerwise"))
					requierment.setPositionOwner(req.get_id());
				if(null!=filterType && filterType.equalsIgnoreCase("projectwise"))
					requierment.setProjectName(req.get_id());

				reqList.add(requierment);
			}
		}
		return reqList;
	}

	@Override
	public List<RequirementDetailsJO> findReqByFilterType(String filterType,String filterValue) {
		List<RequirementDetailsJO> requiermentDetailList = new ArrayList<>();
		Query query = new Query();
		if(null!=filterType && null!=filterValue && filterType.equalsIgnoreCase("mainSkill")){
			query.addCriteria(Criteria.where("skillCategory").in(filterValue));
		}else if(null!=filterType && null!=filterValue && filterType.equalsIgnoreCase("domain")){
			query.addCriteria(Criteria.where("domain").in(filterValue));
		}else if(null!=filterType && null!=filterValue && filterType.equalsIgnoreCase("projectName")){
			query.addCriteria(Criteria.where("projectName").in(filterValue));
		}else if(null!=filterType && null!=filterValue && filterType.equalsIgnoreCase("positionOwner")){
			query.addCriteria(Criteria.where("positionOwner").in(filterValue));
		}
		List<RequirementDetailsJO> requiermentDetails=mongoTemplate.find(query, RequirementDetailsJO.class);
		for(RequirementDetailsJO requirements: requiermentDetails){
			if(null!=requirements.getOpenDate()){
				requirements.setOpenDate(convertUtilToSqlDate(requirements.getOpenDate()));
			}

			if(null!=requirements.getStartDate()){
				requirements.setStartDate(convertUtilToSqlDate(requirements.getStartDate()));
			}

			requiermentDetailList.add(requirements);
		}

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
		if(null!=val && val.getMatchedCount()==val.getModifiedCount()){
			return true;
		}else{
			return false;
		}

	}

	@Override
	public RequirementDetailsJO findRequirementById(Long reqId) {
		BasicDBObject docs = new BasicDBObject();
		docs.put("reqId", reqId);
		Query query = new Query();
		query.addCriteria(Criteria.where("reqId").in(reqId));
		RequirementDetailsJO requirementJO = mongoTemplate.findOne(query, RequirementDetailsJO.class);
		if(null!=requirementJO.getOpenDate()){
			requirementJO.setOpenDate(convertUtilToSqlDate(requirementJO.getOpenDate()));
		}

		if(null!=requirementJO.getStartDate()){
			requirementJO.setStartDate(convertUtilToSqlDate(requirementJO.getStartDate()));
		}

		return requirementJO;
	}

	@Override
	public boolean createUser(LoginDetailsJO loginDetails) {
		Query query = new Query();
		query.addCriteria(Criteria.where("employeeId").in(loginDetails.getEmployeeId()).andOperator(Criteria.where ("emailId").in(loginDetails.getEmailId())));
		LoginDetailsJO val=mongoTemplate.findOne(query, LoginDetailsJO.class);
		if(null!=val){
			return false;
		}else{
			loginDetails.set_id(generateSequence());
			mongoTemplate.save(loginDetails);
			return true;
		}
	}


	/**
	 * The method gives unique number .
	 *
	 * @return the int
	 */
	public static int generateSequence() {
		String dateFormat = new SimpleDateFormat("yyyyMM-ddHHmm-ssSSS", Locale.ENGLISH).format(new Date());
		String[] dateSplit = dateFormat.split("-");
		return Integer.parseInt(dateSplit[0]) + Integer.parseInt(dateSplit[1]) + Integer.parseInt(dateSplit[2]);
	}


	@Override
	public LoginDetailsJO getUserById(LoginDetailsJO loginDetails) {
		Query query = new Query();
		query.addCriteria(Criteria.where("employeeId").in(loginDetails.getEmployeeId())
				.andOperator(Criteria.where ("password").in(loginDetails.getPassword()))
				);
		//query.addCriteria((Criteria.where("employeeId").in(loginDetails.getEmployeeId()).orOperator(Criteria.where("userName")).in(loginDetails.getUserName())).andOperator(Criteria.where ("password").in(loginDetails.getPassword())));
		LoginDetailsJO val = mongoTemplate.findOne(query, LoginDetailsJO.class);
		return val;
	}

	@Override
	public List<TotalRequirements> findReqByDates(java.sql.Date startDate, java.sql.Date endDate, String status,String dashboardType) {
		GroupOperation group =null;
		if(null!=dashboardType && dashboardType.equalsIgnoreCase("mainSkill")){
			group =  Aggregation.group("skillCategory").count().as("total");
		}else if(null!=dashboardType && dashboardType.equalsIgnoreCase("domain")){
			group =  Aggregation.group("domain").count().as("total");
		}else if(null!=dashboardType && dashboardType.equalsIgnoreCase("projectName")){
			group =  Aggregation.group("projectName").count().as("total");
		}else if(null!=dashboardType && dashboardType.equalsIgnoreCase("positionOwner")){
			group =  Aggregation.group("positionOwner").count().as("total");
		}

		Aggregation.match(Criteria.where ("openDate").in(startDate));
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("openDate").gte(startDate).lte(endDate) 
				.andOperator(Criteria.where("status").in(status))),
				group);
		AggregationResults<RequirementGrpJO> requiermentList =mongoTemplate.aggregate(aggregation, RequirementGrpJO.class, RequirementGrpJO.class);
		List<RequirementGrpJO> requierments= requiermentList.getMappedResults();
		List<TotalRequirements> reqList= new ArrayList<TotalRequirements>();

		if(null!=requierments && !requierments.isEmpty()){
			for(RequirementGrpJO req:requierments){
				TotalRequirements requierment= new TotalRequirements();
				requierment.setCount(req.getTotal());
				if(null!=dashboardType && dashboardType.equalsIgnoreCase("mainSkill"))
					requierment.setMainSkill(req.get_id());
				else if(null!=dashboardType && dashboardType.equalsIgnoreCase("domain"))
					requierment.setDomain(req.get_id());
				else if(null!=dashboardType && dashboardType.equalsIgnoreCase("positionOwner"))
					requierment.setPositionOwner(req.get_id());
				else if(null!=dashboardType && dashboardType.equalsIgnoreCase("projectName")) 
					requierment.setProjectName(req.get_id());

				reqList.add(requierment);
			}
		}
		return reqList;
	}

	@Override
	public List<TotalRequirements> findMonthlyGroupedReq() {
		List<TotalRequirements> reqList= new ArrayList<TotalRequirements>();
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yyyy",Locale.ENGLISH);
		for(int i=0 ;i<9;i++){
			YearMonth currMonth    = YearMonth.now();
			String monthYear=currMonth.minusMonths(i).format(monthYearFormatter);
			YearMonth month = YearMonth.from(currMonth.minusMonths(i));
			LocalDate startDate = month.atDay(1);
			LocalDate endDate   = month.atEndOfMonth();
			Query query = new Query();
			query.addCriteria(Criteria.where("openDate").gte(startDate).lte(endDate));
			List<RequirementDetailsJO> requiermentDetails=mongoTemplate.find(query, RequirementDetailsJO.class);

			if(null!=requiermentDetails && !requiermentDetails.isEmpty()){
				int openOnReqCount=0;
				int openOffReqCount=0;
				int closeOffReqCount=0;
				int closeOnReqCount=0;
				int totalOffshoreCount=0;
				int totalOnshoreCount=0;
				TotalRequirements requierment= new TotalRequirements();;
				for(RequirementDetailsJO req:requiermentDetails){
					if(req.getStatus().trim().equalsIgnoreCase(OPEN) && req.getSite().trim().equalsIgnoreCase(OFFSHORE)){
						openOffReqCount ++;
						totalOffshoreCount++;
					}else if (req.getStatus().trim().equalsIgnoreCase(OPEN) && req.getSite().trim().equalsIgnoreCase(ONSITE)){
						openOnReqCount++;
						totalOnshoreCount++;
					}else if (req.getStatus().trim().equalsIgnoreCase(CLOSED) && req.getSite().trim().equalsIgnoreCase(OFFSHORE)){
						closeOffReqCount++;
						totalOffshoreCount++;
					}else if (req.getStatus().trim().equalsIgnoreCase(CLOSED) && req.getSite().trim().equalsIgnoreCase(ONSITE)){
						closeOnReqCount++;
						totalOnshoreCount++;
					}
				}
				requierment.setMonthYear(monthYear);
				requierment.setCloseOffReqCount(closeOffReqCount);
				requierment.setCloseOnReqCount(closeOnReqCount);
				requierment.setOpenOnReqCount(openOnReqCount);
				requierment.setOpenOffReqCount(openOffReqCount);
				requierment.setOffshoreReqCount(totalOffshoreCount);
				requierment.setOnshoreReqCount(totalOnshoreCount);
				reqList.add(requierment);
			}else{
				TotalRequirements requierment= new TotalRequirements();
				requierment.setCount("0");
				requierment.setMonthYear(monthYear);
				requierment.setCloseOffReqCount(0);
				requierment.setCloseOnReqCount(0);
				requierment.setOpenOnReqCount(0);
				requierment.setOpenOffReqCount(0);
				requierment.setOffshoreReqCount(0);
				requierment.setOnshoreReqCount(0);
				reqList.add(requierment);
			}
		}
		return reqList;
	}

	@Override
	public Map<String,Object> forgetPassword(LoginDetailsJO loginDetails) {
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		Query query = new Query();
		query.addCriteria(Criteria.where("employeeId").in(loginDetails.getEmployeeId()));
		LoginDetailsJO val=mongoTemplate.findOne(query, LoginDetailsJO.class);
		if(null!=val){
			if(val.getSecretQuestion().equalsIgnoreCase(loginDetails.getSecretQuestion()) 
					&& val.getAnswer().equals(loginDetails.getAnswer())){
				Update update = new Update();
				update.set("password", loginDetails.getPassword());
				mongoTemplate.updateFirst(query, update, LoginDetailsJO.class);
				responseMsg.put("flag",true);
				responseMsg.put("response","Password updated successfully for employee id " + loginDetails.getEmployeeId());
				return responseMsg;
			}else{
				responseMsg.put("flag",false);
				responseMsg.put("response","Kindly provide the correct secret question and answer for employee id "+ loginDetails.getEmployeeId());
				return responseMsg;
			}
		}else{
			responseMsg.put("flag",false);
			responseMsg.put("response","Employee Id does not exists. Please sign up !!! ");
			return responseMsg;
		}
	}
}		




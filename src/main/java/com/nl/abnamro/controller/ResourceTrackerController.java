/**
 * 
 */
package com.nl.abnamro.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.abnamro.dataaccess.ResourceTrackerDAL;
import com.nl.abnamro.entity.LoginDetailsJO;
import com.nl.abnamro.entity.RequirementDetails;
import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.RequirementSearchJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.ResourceDetailsJO;
import com.nl.abnamro.entity.TotalRequirements;

import io.swagger.annotations.ApiOperation;

/**
 * @author C33129 
 *
 */
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api")
public class ResourceTrackerController {

	private final ResourceTrackerDAL resoucerTrackerDal;

	public ResourceTrackerController(ResourceTrackerDAL resoucerTrackerDal){
		this.resoucerTrackerDal=resoucerTrackerDal;

	}
	
	  @RequestMapping(value = "/home", method = RequestMethod.GET)
	  public String homepage() {
	    return "index";
	  }

	@RequestMapping(value="/jsonToJava")
	public void convertJsonToJava(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			
			List<RequirementDetails> requiermentDetailsList = mapper.readValue(new File("C:\\Users\\C33129\\Development\\dev\\InputFiles\\JanEndDataJson.json"), new TypeReference<List<RequirementDetails>>(){});
	       	for(RequirementDetails requiermentDetails :requiermentDetailsList){
				resoucerTrackerDal.saveRequierments(requiermentDetails);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	
	@ApiOperation(value = "Get List of all Requirements")
	@RequestMapping(value="/requirements")
	public List<RequirementDetailsJO> getAllRequierments() throws IOException{
		System.out.println("inside get");
		/**
		 * To be Implemented
		 */
		List<RequirementDetailsJO> requiermentDetails =resoucerTrackerDal.findAllRequierments();
		System.out.println(requiermentDetails.size());
		return requiermentDetails;
	}


	@RequestMapping(value="/requirements/grouped",method=RequestMethod.GET,headers="Accept=application/json")
	public List<TotalRequirements>  getAllGroupedRequierments() throws IOException{
		System.out.println("inside get getAllGroupedRequierments");
		List<TotalRequirements> requierments=resoucerTrackerDal.findAllGroupedReq("skillwise");
		return requierments;
	}
	
	@RequestMapping(value="/requirements/domainwise",method=RequestMethod.GET,headers="Accept=application/json")
	public List<TotalRequirements>  getGrpRequirementByDomain() throws IOException{
		System.out.println("inside get getGrpRequirementByDomain");
		List<TotalRequirements> requierments=resoucerTrackerDal.findAllGroupedReq("domainwise");
		return requierments;
	}
	
	@RequestMapping(value="/requirements/projectwise",method=RequestMethod.GET,headers="Accept=application/json")
	public List<TotalRequirements>  getGrpRequirementByProject() throws IOException{
		System.out.println("inside get getGrpRequirementByProject");
		List<TotalRequirements> requierments=resoucerTrackerDal.findAllGroupedReq("projectwise");
		return requierments;
	}
	
	@RequestMapping(value="/requirements/ownerwise",method=RequestMethod.GET,headers="Accept=application/json")
	public List<TotalRequirements>  getGrpRequirementByOwner() throws IOException{
		System.out.println("inside get getGrpRequirementByOwner");
		List<TotalRequirements> requierments=resoucerTrackerDal.findAllGroupedReq("ownerwise");
		return requierments;
	}

	@RequestMapping(value="/requirements/retrieve/filterType/{filterType}/filterValue/{filterValue}")
	public List<RequirementDetailsJO> getAllReqBySkill(@PathVariable String filterType, @PathVariable String filterValue) throws IOException{
		System.out.println("inside get filterType -->" +filterType + "filterValue--->" +filterValue);
		/**
		 * To be Implemented
		 */
		if(null!=filterType){
			List<RequirementDetailsJO> requiermentDetails =resoucerTrackerDal.findReqByFilterType(filterType,filterValue);
			System.out.println(requiermentDetails.size());
			return requiermentDetails;
		}
		return null;

	}

	@RequestMapping(value="/requirements/insert" ,method=RequestMethod.POST, headers = "Accept=application/json")
	public Map<String,Object> insertRequierments(@RequestBody RequirementDetailsJO requiermentDetailsJO,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		ResourceTrackerControllerImpl impl= new ResourceTrackerControllerImpl();
		RequirementDetails requiermentDetails =impl.getRequirementDetail(requiermentDetailsJO);
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
		boolean isSaved=resoucerTrackerDal.saveRequierments(requiermentDetails);
		responseMsg.put("reqId", requiermentDetails.getReqId());
		responseMsg.put("flag", isSaved);
		if(isSaved){
			responseMsg.put("response", "Requirement Saved Successfully for Requirement Id " + requiermentDetails.getReqId());
		}else{
			responseMsg.put("response", "Requirement is already present against the requirement id " + requiermentDetails.getReqId());
		}
		
		return responseMsg;
	}

	
	@RequestMapping(value="/requirements/update" ,method=RequestMethod.PUT, headers = "Accept=application/json")
	public Map<String,Object> updatRequierments(@RequestBody RequirementDetailsJO requiermentDetails,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		boolean isSaved=resoucerTrackerDal.updateRequierments(requiermentDetails);
		responseMsg.put("reqId", requiermentDetails.getReqId());
		responseMsg.put("flag", isSaved);
		if(isSaved){
			responseMsg.put("response", "Requirement Updated Successfully for Requirement Id " + requiermentDetails.getReqId());
		}else{
			responseMsg.put("response", "Requirement is not Updated for requirement id " + requiermentDetails.getReqId());
		}
		
		
		return responseMsg;
	}
	
	
	@RequestMapping(value="/user/insert" ,method=RequestMethod.POST, headers = "Accept=application/json")
	public Map<String,Object> createUsers(@RequestBody LoginDetailsJO loginDetails,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		boolean isSaved=resoucerTrackerDal.createUser(loginDetails);
		responseMsg.put("employeeId", loginDetails.getEmployeeId());
		responseMsg.put("flag", isSaved);
		if(isSaved){
			responseMsg.put("response", "User Saved Successfully with employee id  " + loginDetails.getEmployeeId());
		}else{
			responseMsg.put("response", "User is already present in the system with employee id " + loginDetails.getEmployeeId());
		}
		
		return responseMsg;
	}
	
	//@ApiOperation(value = "Get Authorized user" ,response = LoginDetailsJO.class)
	@RequestMapping(value="/user/employeeId",method=RequestMethod.POST, headers="Accept=application/json")
	public Map<String,Object> getUserById(@RequestBody LoginDetailsJO loginDetails,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		responseMsg.put("employeeId", loginDetails.getEmployeeId());
		LoginDetailsJO loginDetail=resoucerTrackerDal.getUserById(loginDetails);
		if(null!=loginDetail){
			responseMsg.put("response", "Successfully Login !!!");
			responseMsg.put("isSuccess", "true");
			responseMsg.put("employeeId", loginDetail.getEmployeeId());
			responseMsg.put("firstName", loginDetail.getFirstName());
			responseMsg.put("isAdmin", loginDetail.isAdmin());
		}else{
			responseMsg.put("response", "User Id and Password are incorrect");
			responseMsg.put("isSuccess", "false");
			
	}
		return responseMsg;
	}

	
	@RequestMapping(value="/user/changePassword" ,method=RequestMethod.POST, headers = "Accept=application/json")
	public Map<String,Object> forgetPassword(@RequestBody LoginDetailsJO loginDetails,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		responseMsg=resoucerTrackerDal.forgetPassword(loginDetails);
		System.out.println("inside forget password");
		/*responseMsg.put("flag", true);
		responseMsg.put("response", response);
		*//*if(isSaved){
			responseMsg.put("response", "User Saved Successfully with employee id  " + loginDetails.getEmployeeId());
		}else{
			responseMsg.put("response", "User is already present in the system with employee id " + loginDetails.getEmployeeId());
		}*/
		
		return responseMsg;
	}
	
	@RequestMapping(value="/requirements/autoFilled",method=RequestMethod.GET,headers="Accept=application/json")
	public Map<String,List<String>>  getAllPreFilledData() throws IOException{
		Map<String,List<String>> responseMsg=new HashMap<String,List<String>>();
		List<String> skillCategorys=resoucerTrackerDal.getSkillCategory();
		if(null!=skillCategorys && !skillCategorys.isEmpty()){
			responseMsg.put("skillCategory", skillCategorys);
		}
		return responseMsg;
	}
	
	
	@RequestMapping(value="/requirements/customDates",method=RequestMethod.PUT,headers="Accept=application/json")
	public Map<String,List<TotalRequirements>> getReqByDates(@RequestBody RequirementSearchJO requirementSearch,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,List<TotalRequirements>> responseMsg=new HashMap<String,List<TotalRequirements>>();
		List<TotalRequirements> totalReq=resoucerTrackerDal.findReqByDates(requirementSearch.getStartDate(), requirementSearch.getClosedDate() ,
				requirementSearch.getStatus(),requirementSearch.getDashboardType());
		if(null!=totalReq && totalReq.size()>1){
			responseMsg.put("requirementData", totalReq);
			}else{
			responseMsg.put("requirementData", null);
		}
		
		return responseMsg;
		
	}
	
	@RequestMapping(value="/requirements/monthwise",method=RequestMethod.GET,headers="Accept=application/json")
	public  Map<String,Object[]> getGrpRequirementByMonth() throws IOException{
		List<String> monthYearList=new ArrayList<>();
		List<Integer> openOffReqCount = new ArrayList<>();
		List<Integer> closeOffReqCount = new ArrayList<>();
		List<Integer> openOnReqCount = new ArrayList<>();
		List<Integer> closeOnReqCount = new ArrayList<>();
		List<Integer> onshoreReqCount = new ArrayList<>();
		List<Integer> offshoreReqCount = new ArrayList<>();
		
		Map<String,Object[]> barChartData=new HashMap<String,Object[]>();
		
		List<TotalRequirements> requierments=resoucerTrackerDal.findMonthlyGroupedReq();
		for(TotalRequirements req:requierments){
			monthYearList.add(req.getMonthYear());
			openOffReqCount.add(req.getOpenOffReqCount());
			closeOffReqCount.add(req.getCloseOffReqCount());
			openOnReqCount.add(req.getOpenOnReqCount());
			closeOnReqCount.add(req.getCloseOnReqCount());
			onshoreReqCount.add(req.getOnshoreReqCount());
			offshoreReqCount.add(req.getOffshoreReqCount());
		}
		
		String [] monthYearListArray=monthYearList.toArray(new String[0]);
		Integer[] openOffReqCountList = openOffReqCount.toArray(new Integer[openOffReqCount.size()]);
		Integer[] closeOffReqCountList =closeOffReqCount.toArray(new Integer[closeOffReqCount.size()]);
		Integer[] openOnReqCountList = openOnReqCount.toArray(new Integer[openOnReqCount.size()]);
		Integer[] closeOnReqCountList = closeOnReqCount.toArray(new Integer[closeOnReqCount.size()]);
		Integer[] offshoreReqCountList = onshoreReqCount.toArray(new Integer[onshoreReqCount.size()]);
		Integer[] onshoreReqCountList = offshoreReqCount.toArray(new Integer[offshoreReqCount.size()]);
		
		
		
		barChartData.put("MonthYear", monthYearListArray);
		barChartData.put("OpenOffshore", openOffReqCountList);
		barChartData.put("CloseOffshore", closeOffReqCountList);
		barChartData.put("OpenOnshore", openOnReqCountList);
		barChartData.put("CloseOnshore", closeOnReqCountList);
		barChartData.put("totalOffshore", offshoreReqCountList);
		barChartData.put("totalOnshore", onshoreReqCountList);
		
		return barChartData;
	}
	
	
	// Employee Registration method starts below -------------------------------------------------------------------------------------
	
	@RequestMapping(value="/employees")
	public List<ResourceDetails> getAllUsers() throws IOException{
		System.out.println("inside get");
		/**
		 * To be Implemented
		 */
		List<ResourceDetails> resourceDetails = resoucerTrackerDal.findAll();
		System.out.println(resourceDetails.size());
		return resourceDetails;
	}


	@RequestMapping(value="/employees/insert" ,method=RequestMethod.POST, headers = "Accept=application/json")
	public void insertUser(@RequestBody ResourceDetailsJO resource,
			//@RequestParam("fileData") MultipartFile file,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		System.out.println("inside hello");

		File f = new File("C:\\Users\\C33129\\Documents\\Rahul_resume.doc");
		byte[] array = Files.readAllBytes(f.toPath());
		//MultipartFile file =resource.getResume();
		// File f = new File(file.getBytes());
		//byte[] array = Files.readAllBytes(file.);
		// System.out.println(file);

		/*runDetails.setId(id);
	        runDetails.setFileContentType(file.getContentType());
	        runDetails.setFileDataBytes(file.getBytes());
	        runDetails.setFileName(file.getOriginalFilename());
	        runDetails.setFileData(file);*/

		ResourceDetails resourceDetails = new ResourceDetails();
		//resourceDetails.set_id("1234");
		resourceDetails.setFirstName(resource.getFirstName());
		resourceDetails.setFileName(f.getName());
		resourceDetails.setMiddleName(resource.getMiddleName());
		resourceDetails.setLastName(resource.getLastName());
		resourceDetails.setGender(resource.getGender());
		resourceDetails.setPhone(resource.getPhone());
		resourceDetails.setCreationDate(resource.getCreationDate());
		resourceDetails.setTitle(resource.getTitle());
		resourceDetails.setEmail(resource.getEmail());
		resourceDetails.setDob(resource.getDob());
		resourceDetails.setEmployeeId(resource.getEmployeeId());
		resourceDetails.setAddressLine1(resource.getAddressLine1());
		resourceDetails.setAddressLine2(resource.getAddressLine2());
		resourceDetails.setPhone(resource.getPhone());
		resourceDetails.setAlternatePhone(resource.getAlternatePhone());
		resourceDetails.setFile(new Binary(BsonBinarySubType.BINARY,array));

		resoucerTrackerDal.createEmployee(resourceDetails);
	}

	@RequestMapping(value="/requirements/id/{id}")
	public RequirementDetailsJO getRequiermentById(@PathVariable long id) throws IOException{
		System.out.println("inside get" +id);
		
		RequirementDetailsJO requiermentDetails =resoucerTrackerDal.findRequirementById(id);
		System.out.println(requiermentDetails);
		return requiermentDetails;
	}
	

	@RequestMapping(value="/employees/retrieve/id/{id}")
	public void getuser(@PathVariable long id) throws IOException{
		System.out.println("inside retrieve");
		String RETRIEVE_FOLDER= "C:\\Users\\C33129\\Documents\\getFromMongodb\\";

		ResourceDetails resourceDetails= new ResourceDetails();
		resourceDetails.set_id(id);
		resoucerTrackerDal.findOne(resourceDetails);
		ResourceDetails resource=	resoucerTrackerDal.findOne(resourceDetails);
		if(null!=resource){
			System.out.println("resource --> " +resource.toString());

			Binary fileReceive=resource.getFile();
			if(fileReceive != null) {
				FileOutputStream fileOuputStream = null;
				try {
					fileOuputStream = new FileOutputStream(RETRIEVE_FOLDER + resource.getFileName());
					fileOuputStream.write(fileReceive.getData());
				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					if (fileOuputStream != null) {
						try {
							fileOuputStream.close();
						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
		}


	}


	@RequestMapping(value="/employees/update/id/{id}")
	public void updateUser(@PathVariable long id) throws IOException{
		System.out.println("inside update");
		String RETRIEVE_FOLDER= "C:\\Users\\C33129\\Documents\\getFromMongodb\\";

		ResourceDetails resourceDetails= new ResourceDetails();
		resourceDetails.set_id(id);
		ResourceDetails resource=	resoucerTrackerDal.findOne(resourceDetails);


		if(null!=resource){
			File f = new File("C:\\Users\\C33129\\Documents\\Prashant_resume.doc");
			byte[] array = Files.readAllBytes(f.toPath());

			resource.setFileName(f.getName());
			resource.setFile(new Binary(BsonBinarySubType.BINARY,array));

			resoucerTrackerDal.updateUser(resource);


			Binary fileReceive=resource.getFile();
			if(fileReceive != null) {
				FileOutputStream fileOuputStream = null;
				try {
					fileOuputStream = new FileOutputStream(RETRIEVE_FOLDER + resource.getFileName());
					fileOuputStream.write(fileReceive.getData());
				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					if (fileOuputStream != null) {
						try {
							fileOuputStream.close();
						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
		}


	}

	@RequestMapping(value="/employees/delete/id/{id}")
	public void deleteUser(@PathVariable Object id) throws IOException{
		System.out.println("inside delete");
		ResourceDetails resourceDetails= new ResourceDetails();
		resourceDetails.set_id(id);
		ResourceDetails deletedResources= resoucerTrackerDal.deleteUser(resourceDetails);
		System.out.println("deletedResources--->"+ deletedResources.toString());
	}
	
	
}

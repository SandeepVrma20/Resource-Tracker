/**
 * 
 */
package com.nl.abnamro.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nl.abnamro.dataaccess.ResourceTrackerDAL;
import com.nl.abnamro.entity.RequirementDetailsJO;
import com.nl.abnamro.entity.RequirementGrpJO;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.ResourceDetailsJO;
import com.nl.abnamro.entity.TotalRequierments;
import com.nl.abnamro.services.ResouceTrackerServicesImpl;

/**
 * @author C33129
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api")
public class ResourceTrackerController {

	@Autowired
	private ResouceTrackerServicesImpl resouceTrackerServicesImpl;

	private final ResourceTrackerDAL resoucerTrackerDal;

	public ResourceTrackerController(ResourceTrackerDAL resoucerTrackerDal){
		this.resoucerTrackerDal=resoucerTrackerDal;

	}


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
	public List<TotalRequierments>  getAllGroupedRequierments() throws IOException{
		System.out.println("inside get getAllGroupedRequierments");
		List<TotalRequierments> requierments=resoucerTrackerDal.findAllGroupedReq();
		return requierments;
	}

	@RequestMapping(value="/requirements/retrieve/skillCategory/{skillCategory}")
	public List<RequirementDetailsJO> getAllReqBySkill(@PathVariable String skillCategory ) throws IOException{
		System.out.println("inside get");
		/**
		 * To be Implemented
		 */
		if(null!=skillCategory){
			List<RequirementDetailsJO> requiermentDetails =resoucerTrackerDal.findReqBySkill(skillCategory);
			System.out.println(requiermentDetails.size());
			return requiermentDetails;
		}
		return null;

	}

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

		//resouceTrackerServicesImpl.saveUser(resourceDetails);
		resoucerTrackerDal.saveUser(resourceDetails);
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

	@RequestMapping(value="/requirements/insert" ,method=RequestMethod.POST, headers = "Accept=application/json")
	public Map<String,Object> insertRequierments(@RequestBody RequirementDetailsJO requiermentDetails,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException{
		Map<String,Object> responseMsg=new HashMap<String,Object>();
		System.out.println("inside requiermentDetails");
		System.out.println(requiermentDetails.getStartDate());
		boolean isSaved=resoucerTrackerDal.saveRequierments(requiermentDetails);
		System.out.println("returnValue--->" +isSaved);
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
		System.out.println("inside requiermentDetails");
		System.out.println(requiermentDetails.getStartDate());
		boolean isSaved=resoucerTrackerDal.updateRequierments(requiermentDetails);
		System.out.println("returnValue--->" +isSaved);
		responseMsg.put("reqId", requiermentDetails.getReqId());
		responseMsg.put("flag", isSaved);
		if(isSaved){
			responseMsg.put("response", "Requirement Updated Successfully for Requirement Id " + requiermentDetails.getReqId());
		}else{
			responseMsg.put("response", "Requirement is not Updated for requirement id " + requiermentDetails.getReqId());
		}
		
		
		return responseMsg;
	}



}

/**
 * 
 */
package com.nl.abnamro.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nl.abnamro.dataaccess.ResourceTrackerDAL;
import com.nl.abnamro.entity.ResourceDetails;
import com.nl.abnamro.entity.ResourceDetailsJO;
import com.nl.abnamro.services.ResouceTrackerServicesImpl;

/**
 * @author C33129
 *
 */
@RestController
@RequestMapping(value = "/home")
public class ResourceTrackerController {
	
	
	
	@Autowired
	private ResouceTrackerServicesImpl resouceTrackerServicesImpl;
	
	private final ResourceTrackerDAL resoucerTrackerDal;
	
	public ResourceTrackerController(ResourceTrackerDAL resoucerTrackerDal){
		this.resoucerTrackerDal=resoucerTrackerDal;
		
	}
	
	
	@RequestMapping(value="/insert")
	public void insertUser() throws IOException{
		System.out.println("inside hello");
		ResourceDetailsJO resource = new ResourceDetailsJO();
		resource.setFirstName("Rohit");
		resource.setLastName("Sharma");
		resource.setGender("Male");
		resource.setMonth("JAN");
		resource.setPhone("123456");
		resource.setYear(1989);
		resource.setDate(04);
		resource.setEmail("prashant.verma@gmail.com");
		
		String dateOfBirth= resource.getMonth()+"-"+resource.getDate() +"-" +resource.getYear();
		 File f = new File("C:\\Users\\C33129\\Documents\\Rahul_resume.doc");
		 byte[] array = Files.readAllBytes(f.toPath());
		
		ResourceDetails resourceDetails = new ResourceDetails();
		//resourceDetails.set_id("1234");
		resourceDetails.setFirstName(resource.getFirstName());
		resourceDetails.setFileName(f.getName());
		resourceDetails.setLastName(resource.getLastName());
		resourceDetails.setGender(resource.getGender());
		resourceDetails.setPhone(resource.getPhone());
		resourceDetails.setCreationDate(resource.getCreationDate());
		resourceDetails.setTitle(resource.getTitle());
		resourceDetails.setEmail(resource.getEmail());
		resourceDetails.setDob(dateOfBirth);
		resourceDetails.setFile(new Binary(BsonBinarySubType.BINARY,array));
		
		//resouceTrackerServicesImpl.saveUser(resourceDetails);
		resoucerTrackerDal.saveUser(resourceDetails);
	}
	

	@RequestMapping(value="/retrieve/id/{id}")
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
	

	@RequestMapping(value="/update/id/{id}")
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

	

	@RequestMapping(value="/delete/id/{id}")
	public void deleteUser(@PathVariable Object id) throws IOException{
		System.out.println("inside delete");
		 ResourceDetails resourceDetails= new ResourceDetails();
		 resourceDetails.set_id(id);
		 ResourceDetails deletedResources= resoucerTrackerDal.deleteUser(resourceDetails);
		System.out.println("deletedResources--->"+ deletedResources.toString());
	}
	
	
	
	
	@RequestMapping(value="/abc")
	public void getAlluser() throws IOException{
		System.out.println("inside retrieve");
		 String RETRIEVE_FOLDER= "C:\\Users\\C33129\\Documents\\getFromMongodb\\";
		
		 ResourceDetails resourceDetails= new ResourceDetails();
		 resourceDetails.setFirstName("Sandy");
		
		Optional<ResourceDetails> resource=	resouceTrackerServicesImpl.getUser(resourceDetails);
		 ResourceDetails value= resource.get();
		 Binary fileReceive=value.getFile();
		 if(fileReceive != null) {
		        FileOutputStream fileOuputStream = null;
		        try {
		            fileOuputStream = new FileOutputStream(RETRIEVE_FOLDER + value.getFileName());
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

/**
 * 
 */
package com.nl.abnamro.entity;

import java.util.Date;

import org.bson.types.Binary;
import org.junit.Ignore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author C33129
 *
 */
@Document(collection = "Resource_Tracker")
public class ResourceDetails {
	
	@Id
	private Object _id;
	private String age;
	private String phone;
	private String title;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private String dob;
	private String email;
	private String resume;
	private Date creationDate = new Date();
	
	private Binary file;
	private String fileName;
	

	public String toString(){
		return "first Name :" +this.firstName +
				" Middle Name :" +this.middleName +
				" last Name :" +this.lastName +
				" email :" +this.email +
				" phone no :" +this.phone +
				" gender :" +this.gender +
				" dob:" +this.dob +
				" file Name :" +this.fileName ;
				
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public Object get_id() {
		return _id;
	}
	public void set_id(Object _id) {
		this._id = _id;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Binary getFile() {
		return file;
	}
	public void setFile(Binary file) {
		this.file = file;
	}
	
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	

}

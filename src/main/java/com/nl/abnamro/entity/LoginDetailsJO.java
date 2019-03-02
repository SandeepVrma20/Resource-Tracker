/**
 * 
 */
package com.nl.abnamro.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author C33129
 *
 */
@Document(collection = "LoginDetails")
public class LoginDetailsJO {
	
	//@ApiModelProperty(notes = "Autogenerated Id of the user")
	@Id
	private Object _id;
	//@ApiModelProperty(notes = "Employee Id of the user")
	private Long employeeId;
	//@ApiModelProperty(notes = "First name of the user")
	private String firstName;
	//@ApiModelProperty(notes = "Last Name of the user")
	private String lastName;
	//@ApiModelProperty(notes = "User name of the user")
	private String userName;
	//@ApiModelProperty(notes = "Password of the user")
	private String password;
	//@ApiModelProperty(notes = "Email Id of the user")
	private String emailId;
	//@ApiModelProperty(notes = "Secrect Questions of the user")
	private String secretQuestion;
	//@ApiModelProperty(notes = "Secret Answer")
	private String answer;
	//@ApiModelProperty(notes = "Phone no of the user")
	private String phoneNo;
	//@ApiModelProperty(notes = "If the user id Admin or not")
	private boolean isAdmin =false;
	
	
	
	
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getSecretQuestion() {
		return secretQuestion;
	}
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Object get_id() {
		return _id;
	}
	public void set_id(Object _id) {
		this._id = _id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	
}

/**
 * 
 */
package com.nl.abnamro.entity;

import java.util.Date;
import org.bson.types.Binary;
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

  private String title;
  private String firstName;
  private String lastName;
  private String middleName;
  private String gender;
  private String month;
  private String email;
  private String phone;
  private Date dob;
  private long employeeId;
  private String alternatePhone;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String country;
  private String pinCode;
  private Binary file;
  private String fileName;
  private String resume;
  private Date creationDate = new Date();


  public String toString() {
    return "first Name :" + this.firstName + " Middle Name :" + this.middleName + " last Name :" + this.lastName
        + " email :" + this.email + " phone no :" + this.phone + " gender :" + this.gender + " dob:" + this.dob
        + " file Name :" + this.fileName;

  }


  public Object get_id() {
    return _id;
  }


  public void set_id(Object _id) {
    this._id = _id;
  }



  public String getPhone() {
    return phone;
  }


  public void setPhone(String phone) {
    this.phone = phone;
  }


  public Date getDob() {
    return dob;
  }


  public void setDob(Date dob) {
    this.dob = dob;
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


  public String getMonth() {
    return month;
  }


  public void setMonth(String month) {
    this.month = month;
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


  public Date getCreationDate() {
    return creationDate;
  }


  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }


  public long getEmployeeId() {
    return employeeId;
  }


  public void setEmployeeId(long employeeId) {
    this.employeeId = employeeId;
  }


  public String getAlternatePhone() {
    return alternatePhone;
  }


  public void setAlternatePhone(String alternatePhone) {
    this.alternatePhone = alternatePhone;
  }


  public String getAddressLine1() {
    return addressLine1;
  }


  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }


  public String getAddressLine2() {
    return addressLine2;
  }


  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }


  public String getCity() {
    return city;
  }


  public void setCity(String city) {
    this.city = city;
  }


  public String getState() {
    return state;
  }


  public void setState(String state) {
    this.state = state;
  }


  public String getCountry() {
    return country;
  }


  public void setCountry(String country) {
    this.country = country;
  }


  public String getPinCode() {
    return pinCode;
  }


  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }


  public Binary getFile() {
    return file;
  }


  public void setFile(Binary file) {
    this.file = file;
  }


  public String getFileName() {
    return fileName;
  }


  public void setFileName(String fileName) {
    this.fileName = fileName;
  }



}

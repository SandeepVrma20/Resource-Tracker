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
@Document(collection = "Requierment_Tracker")
public class RequirementDetailsJO {

  @Id
  private String _id;

  private long rgsId;
  private long reqId;
  private String account;
  private String positionOwner;
  private String openDate;
  private String position;
  private String skillCategory;
  private String mainSkill;
  private String additionalSkill;
  private String domain;
  private String projectName;
  private String expBand;
  private String eucRefId;
  private String site;
  private String location;
  private String startDate;
  private String reqType;
  private String reqClass;
  private String contractor;
  private String trainee;
  private String revenueWithinQtr;
  private String status;
  private String employeeId;
  private String resourceName;
  private String closedDate;
  private String onboardDate;
  private String won;
  private String allocationDate;
  private String through;
  private String closedBy;
  private String remarks;


  // id?: string,



  public long getRgsId() {
    return rgsId;
  }

  public String getEucRefId() {
    return eucRefId;
  }

  public void setEucRefId(String eucRefId) {
    this.eucRefId = eucRefId;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getReqType() {
    return reqType;
  }

  public void setReqType(String reqType) {
    this.reqType = reqType;
  }

  public String getReqClass() {
    return reqClass;
  }

  public void setReqClass(String reqClass) {
    this.reqClass = reqClass;
  }

  public String getContractor() {
    return contractor;
  }

  public void setContractor(String contractor) {
    this.contractor = contractor;
  }

  public String getTrainee() {
    return trainee;
  }

  public void setTrainee(String trainee) {
    this.trainee = trainee;
  }

  public String getRevenueWithinQtr() {
    return revenueWithinQtr;
  }

  public void setRevenueWithinQtr(String revenueWithinQtr) {
    this.revenueWithinQtr = revenueWithinQtr;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public String getClosedDate() {
    return closedDate;
  }

  public void setClosedDate(String closedDate) {
    this.closedDate = closedDate;
  }

  public String getOnboardDate() {
    return onboardDate;
  }

  public void setOnboardDate(String onboardDate) {
    this.onboardDate = onboardDate;
  }

  public String getWon() {
    return won;
  }

  public void setWon(String won) {
    this.won = won;
  }

  public String getAllocationDate() {
    return allocationDate;
  }

  public void setAllocationDate(String allocationDate) {
    this.allocationDate = allocationDate;
  }

  public String getThrough() {
    return through;
  }

  public void setThrough(String through) {
    this.through = through;
  }

  public String getClosedBy() {
    return closedBy;
  }

  public void setClosedBy(String closedBy) {
    this.closedBy = closedBy;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public void setRgsId(long rgsId) {
    this.rgsId = rgsId;
  }

  public long getReqId() {
    return reqId;
  }

  public void setReqId(long reqId) {
    this.reqId = reqId;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPositionOwner() {
    return positionOwner;
  }

  public void setPositionOwner(String positionOwner) {
    this.positionOwner = positionOwner;
  }

  public String getOpenDate() {
    return openDate;
  }

  public void setOpenDate(String openDate) {
    this.openDate = openDate;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getSkillCategory() {
    return skillCategory;
  }

  public void setSkillCategory(String skillCategory) {
    this.skillCategory = skillCategory;
  }

  public String getMainSkill() {
    return mainSkill;
  }

  public void setMainSkill(String mainSkill) {
    this.mainSkill = mainSkill;
  }

  public String getAdditionalSkill() {
    return additionalSkill;
  }

  public void setAdditionalSkill(String additionalSkill) {
    this.additionalSkill = additionalSkill;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getExpBand() {
    return expBand;
  }

  public void setExpBand(String expBand) {
    this.expBand = expBand;
  }



}

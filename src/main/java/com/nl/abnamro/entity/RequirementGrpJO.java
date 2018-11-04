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
public class RequirementGrpJO {

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
  private String total;



  public String get_id() {
    return _id;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public long getRgsId() {
    return rgsId;
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

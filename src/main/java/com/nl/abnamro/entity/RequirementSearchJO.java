/**
 * 
 */
package com.nl.abnamro.entity;

import java.sql.Date;

/**
 * @author C33129
 *
 */
public class RequirementSearchJO {
	
	private Date startDate;
	private Date closedDate;
	private String status;
	private String dashboardType;
	
	
	
	public String getDashboardType() {
		return dashboardType;
	}
	public void setDashboardType(String dashboardType) {
		this.dashboardType = dashboardType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}

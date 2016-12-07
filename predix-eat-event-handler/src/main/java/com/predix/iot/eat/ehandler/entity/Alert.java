/**
 * 
 */
package com.predix.iot.eat.ehandler.entity;

public class Alert{
	
	String messageType;
	double messageValue;
	Long buildingId;

	public Alert(){
		
	}
	
	public Alert(String messageType, double messageValue, Long buildingId) {
		super();
		this.messageType = messageType;
		this.messageValue = messageValue;
		this.buildingId = buildingId;
	}
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public double getMessageValue() {
		return messageValue;
	}
	public void setMessageValue(double messageValue) {
		this.messageValue = messageValue;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
}

/**
 * 
 */
package com.predix.iot.eat.timeseries.dto;
import java.util.Date;

/**
 * @author Tai Huynh
 *
 */

public class Alert{
	
	String messageType;
	Date messageTs;
	double messageValue;
	Integer messageQlty;
	Long buildingId;
	Long floorId;
	Long roomId;
	Long equipmentId;
	Date dateCreated;
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public Date getMessageTs() {
		return messageTs;
	}
	public void setMessageTs(Date messageTs) {
		this.messageTs = messageTs;
	}
	public double getMessageValue() {
		return messageValue;
	}
	public void setMessageValue(double messageValue) {
		this.messageValue = messageValue;
	}
	public Integer getMessageQlty() {
		return messageQlty;
	}
	public void setMessageQlty(Integer messageQlty) {
		this.messageQlty = messageQlty;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Long getFloorId() {
		return floorId;
	}
	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	
}

package com.predix.iot.bms.dbhandler.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Incident entity to store the exceptional data
 * 
 * @author Anh V. Nguyen
 */
@Entity
@Table(name="Alert")
public class Alert extends BaseEntity {
	
	@Column(name = "message_type")
	String messageType;
	
	@Column(name = "message_ts")
	Date messageTs;
	
	@Column(name = "message_value")
	double messageValue;
	
	@Column(name = "message_qlty")
	Integer messageQlty;
	
	@Column(name = "building_id")
	Long buildingId;
	
	@Column(name = "floor_id")
	Long floorId;
	
	@Column(name = "room_id")
	Long roomId;
	
	@Column(name = "equipment_id")
	Long equipmentId;
	
	@Column(name = "date_created")
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

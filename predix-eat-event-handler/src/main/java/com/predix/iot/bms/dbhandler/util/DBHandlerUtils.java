/**
 * 
 */
package com.predix.iot.bms.dbhandler.util;

import java.util.Date;

import com.predix.iot.bms.dbhandler.entity.Alert;

/**
 * @author Tai Huynh
 * all utilities DB Handler
 */
public class DBHandlerUtils {

	/**
	 * convert from {@link com.predix.iot.Alert.common.dto.Incident} to {@link Alert} to save to DB 
	 * @param fromIncident {@link com.predix.iot.Alert.common.dto.Incident} dto incident to convert to Entity in DB
	 * @return {@link Alert}
	 * */
	public static Alert convertToDBAlert(Alert alertMessage){
		Alert alert = new Alert();
		alert.setMessageType(alertMessage.getMessageType());
		alert.setMessageTs(alertMessage.getMessageTs());
		alert.setMessageValue(alertMessage.getMessageValue());
		alert.setMessageQlty(alertMessage.getMessageQlty());
		alert.setDateCreated(new Date());
		alert.setBuildingId(alertMessage.getBuildingId());
		alert.setFloorId(alertMessage.getFloorId());
		alert.setRoomId(alertMessage.getRoomId());
		alert.setEquipmentId(alertMessage.getEquipmentId());
		return alert;
	}
}

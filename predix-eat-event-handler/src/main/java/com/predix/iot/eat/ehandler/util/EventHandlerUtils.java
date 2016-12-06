/**
 * 
 */
package com.predix.iot.eat.ehandler.util;

import java.util.Date;

import com.predix.iot.eat.ehandler.entity.Alert;

/**
 * @author Tai Huynh
 * all utilities DB Handler
 */
public class EventHandlerUtils {

	/**
	 * convert from {@link com.predix.iot.Alert.common.dto.Incident} to {@link Alert} to save to DB 
	 * @param fromIncident {@link com.predix.iot.Alert.common.dto.Incident} dto incident to convert to Entity in DB
	 * @return {@link Alert}
	 * */
	public static Alert convertToDBAlert(Alert alertMessage){
		Alert alert = new Alert();

		return alert;
	}
}

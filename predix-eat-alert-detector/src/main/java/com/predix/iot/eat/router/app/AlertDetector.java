package com.predix.iot.eat.router.app;

import java.util.Date;
import java.util.List;
import java.util.Map;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.predix.iot.bms.common.dto.TimeTag;
import com.predix.iot.bms.common.utils.JsonUtils;
import com.predix.iot.bms.timeseries.configuration.DbHandlerConfiguration;
import com.predix.iot.bms.timeseries.dto.Alert;
import com.predix.iot.bms.timeseries.dto.TimeSeriesMessage;

/**
 * @author Tai Huynh
 * Receive message from queue.
 * In case that is the incident, send it to incident Queue and Incident UI
 * TODO: use Redis to cache asset info from Asset Service.
 **/
@Component
public class AlertDetector {

	private static final Logger logger = LoggerFactory.getLogger(AlertDetector.class);
	private final String API = "/alerts/batch";
	private static Double lastECpoint = null;
	private HttpClient client = buildHttpClient();

	@Autowired
	DbHandlerConfiguration dbhandlerConfig;

	@Autowired
	private SimpMessagingTemplate simpleMessagingTemplate;


	/**
	 * @author Tai Huynh:
	 * Consuming message from timeseriesIncidentQueue
	 * Check and forward to incident queue
	 * @param message
	 */
	@RabbitListener(queues = "alertQueue")
	public void onMessage(@Payload String message) throws InterruptedException {
		if(message == null) {
			logger.warn("Message is null from queue: " + message);
		} else {
			logger.info("Queue Message: " + message);
			List<TimeTag> allTimeTags = convertJsonToTimeTags(message);
			if(allTimeTags == null || allTimeTags.size() < 1) {
				logger.warn("Invalid data from queue: " + message);
			} else {
				for (TimeTag timeTag : allTimeTags) {
					String tagName = timeTag.getName();
					if (tagName != null) {
						processTag(tagName, timeTag);
					} else {
						logger.warn("Cannot extract tag name from message:" +  message);
					}
				}
			}
		}
	}


	/**
	 * Convert Json string to  TimeTag Java object
	 *
	 * @param jsonStr
	 * @return TimeTag
	 */
	private List<TimeTag> convertJsonToTimeTags(String jsonStr) {
		try {
			TimeSeriesMessage timeSeries = JsonUtils.MAPPER.readValue(jsonStr, TimeSeriesMessage.class);
			if(timeSeries == null)
				return null;
			List<TimeTag> tags = timeSeries.getBody();
			return tags;
		} catch (Exception e) {
			logger.warn("Error to convert Json to TimeTag", e);
			return null;
		}
	}

	/**
	 * @author Tai Huynh
	 * call to Asset to get info then
	 * @param turbineUri also the same tagName
	 * @param tag {@link TimeTag}
	 * */
	private void processTag(String messageType, TimeTag tag) {
		Long buildingId = null;
		Long floorId = null;
		Long roomId = null;
		Long equipmentId = null;

		Map<String, String> attributes = tag.getAttributes();
		if(null != attributes.get("buildingId") && !"".equals(attributes.get("buildingId"))){
			buildingId = Long.valueOf(attributes.get("buildingId"));
		}
		if(null != attributes.get("floorId") && !"".equals(attributes.get("floorId"))){
			floorId = Long.valueOf(attributes.get("floorId"));
		}
		if(null != attributes.get("roomId") && !"".equals(attributes.get("roomId"))){
			roomId = Long.valueOf(attributes.get("roomId"));
		}
		if(null != attributes.get("equipmentId") && !"".equals(attributes.get("equipmentId"))){
			equipmentId = Long.valueOf(attributes.get("equipmentId"));
		}

		Object[][] datapoints = tag.getDatapoints();
		for (Object[] datapoint : datapoints) {
			String messageTS = datapoint[0].toString();
			double messageValue = Double.parseDouble((datapoint[1].toString()));
			Integer messageQlty = Integer.parseInt(datapoint[2].toString());

			Alert alert = new Alert();
			alert.setMessageType(messageType);
			alert.setMessageTs(new Date(Long.valueOf(messageTS)));
			alert.setMessageValue(messageValue);
			alert.setMessageQlty(messageQlty);
			alert.setBuildingId(buildingId);
			alert.setFloorId(floorId);
			alert.setRoomId(roomId);
			alert.setEquipmentId(equipmentId);

			if("INDOOR_TEMPERATURE".equalsIgnoreCase(messageType)){
				if(messageValue < 65 || messageValue > 75){
					saveAlertMessage(alert);
					forwardMessage(alert);
				}
			}else if("CO2_LEVEL".equalsIgnoreCase(messageType)) {
				if(messageValue < 300 || messageValue > 650){
					saveAlertMessage(alert);
					forwardMessage(alert);
				}
			}else if("ENERGY_CONSUMPTION".equalsIgnoreCase(messageType)){
				if(null == lastECpoint){
					logger.info("Init last EC point: " + messageValue);
					lastECpoint = messageValue;
				}else{
					logger.info("Last EC point: " + lastECpoint);
					logger.info("Current EC point: " + messageValue);
					if(messageValue > (lastECpoint + lastECpoint*0.2)){
						saveAlertMessage(alert);
						forwardMessage(alert);
					}
				}
			}
		}
	}

	/**
	 * forward message to db handler
	 * @param message
	 */
	private synchronized void saveAlertMessage(Alert message) {
		String dbHandlerEndpoint = dbhandlerConfig.getRuntimeUri() + API;
		String alertMessage;
		try {
			Alert[] alert = new Alert[1];
			alert[0] = message;

			alertMessage = JsonUtils.MAPPER.writeValueAsString(alert);

			if (alertMessage != null) {
				logger.debug("Alert Message: " + alertMessage);
				HttpPost request = new HttpPost(dbHandlerEndpoint);

				if (logger.isInfoEnabled()) {
					logger.info("DB Handler Endpoint: " + dbHandlerEndpoint + ", sending input: " + alertMessage);
				}
				request.setHeader("Content-Type", "application/json");
				StringEntity input = new StringEntity(alertMessage);
				input.setContentType("application/json");				
				//MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				//builder.addPart("alerts", new StringBody(alertMessage, ContentType.APPLICATION_JSON));
				request.setEntity(input);

				HttpResponse response = client.execute(request);
				String rawResponse = EntityUtils.toString(response.getEntity());
				if (logger.isInfoEnabled()) {
					logger.info("rawResponse: " + rawResponse);
				}
			}else{
				logger.error("Error", "No Alert Message to Save");
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	private synchronized HttpClient buildHttpClient() {
		return HttpClientBuilder.create().build();
	}

	/**
	 * forward message to UI through WebSocket
	 * @param message
	 */
	private synchronized void forwardMessage(Alert message) {		
		String alertMessage;
		try {
			alertMessage = JsonUtils.MAPPER.writeValueAsString(message);
			logger.debug("Alert Message: " + alertMessage);
			
			simpleMessagingTemplate.convertAndSend("/topic/alerts", alertMessage);
		} catch (Exception e) {
			logger.error("Error", e);
		}

	}
}

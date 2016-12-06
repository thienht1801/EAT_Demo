package com.predix.iot.eat.router.app;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.predix.iot.eat.timeseries.util.JsonUtils;
import com.predix.iot.eat.timeseries.configuration.UaaConfiguration;
import com.predix.iot.eat.timeseries.util.UaaUtils;
import com.predix.iot.eat.timeseries.configuration.EventHandlerConfiguration;
import com.predix.iot.eat.timeseries.dto.Alert;
import com.predix.iot.eat.timeseries.dto.BuildingEntity;

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
	
	private HttpClient client = buildHttpClient();

	@Autowired
	EventHandlerConfiguration eventHandlerConfig;

	@Autowired
	UaaConfiguration uaaConfig;
	
	private String token;

	@RabbitListener(queues = "tempQueue")
	public void onMessage(@Payload String message) throws InterruptedException {
		if(message == null) {
			logger.warn("Message is null from queue: " + message);
		} else {
			logger.info("Queue Message: " + message);
			BuildingEntity messageObject = JsonUtils.fromJson(message, BuildingEntity.class);
			processMessage(messageObject);	
		}
	}

	private void processMessage(BuildingEntity message) {
		Alert alert = null;
		Long buildingId = message.getId();
		int indoorTempCheck = Double.compare(message.getInDoorTemp(), 95.0);
		int outdoorTempCheck = Double.compare(message.getOutDoorTemp(), 95.0);
		int energyConsumeCheck = Double.compare(message.getEnergyConsume(), 450.0);
		
		if(indoorTempCheck > 0){
			alert = new Alert();
			alert.setBuildingId(buildingId);
			alert.setMessageType("INDOOR_TEMP_ALERT");
			alert.setMessageValue(message.getInDoorTemp());
			
			saveAlertMessage(alert);
		}
		
		if(outdoorTempCheck > 0){
			alert = new Alert();
			alert.setBuildingId(buildingId);
			alert.setMessageType("OUTDOOR_TEMP_ALERT");
			alert.setMessageValue(message.getOutDoorTemp());
			
			saveAlertMessage(alert);			
		}
		
		if(energyConsumeCheck > 0){
			alert = new Alert();
			alert.setBuildingId(buildingId);
			alert.setMessageType("ENERGY_CONSUME_ALERT");
			alert.setMessageValue(message.getEnergyConsume());
			
			saveAlertMessage(alert);			
		}

		checkTokenInCache();
	}

	/**
	 * forward message to event handler
	 * @param message
	 */
	private synchronized void saveAlertMessage(Alert message) {
		String eventHandlerEndpoint = eventHandlerConfig.getRuntimeUri() + API;
		String alertMessage;
		try {
			Alert[] alert = new Alert[1];
			alert[0] = message;

			alertMessage = JsonUtils.MAPPER.writeValueAsString(alert);

			if (alertMessage != null) {
				logger.debug("Alert Message: " + alertMessage);
				HttpPost request = new HttpPost(eventHandlerEndpoint);

				if (logger.isInfoEnabled()) {
					logger.info("Event Handler Endpoint: " + eventHandlerEndpoint + ", sending input: " + alertMessage);
				}
				
				request.setHeader("Content-Type", "application/json");
				if(null == token || "".equalsIgnoreCase(token)){
					logger.error("Error", "TOKEN IS EMPTY");
				}
				request.setHeader("Authorization", "Bearer " + token);
				
				StringEntity input = new StringEntity(alertMessage);
				input.setContentType("application/json");				
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
	
	@PostConstruct
	private void putTokenToCache() {
		try {
			token = UaaUtils.getAdminAccessToken(uaaConfig);
			JSONObject jsonObject = UaaUtils.getAccountInfoInUaa(token, uaaConfig);
			Long expiredTime = (jsonObject.getLong("exp") * 1000);
			UaaUtils.putTokenToCache(token, expiredTime);
		} catch (Exception ex) {
			logger.warn("Can't get the token in uaa", ex);
		}
	}
	
	private void checkTokenInCache(){
		if(token != null) {
			if(UaaUtils.isTokenExpired(token)) {
				UaaUtils.removeTokenInCache(token);
				putTokenToCache();
				logger.info("********** TOKEN IS EXPIRED **********");
			}
		} else {
			putTokenToCache();
		}
	}
}

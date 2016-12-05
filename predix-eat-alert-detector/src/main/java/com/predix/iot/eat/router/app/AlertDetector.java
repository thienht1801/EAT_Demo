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

import com.predix.iot.eat.timeseries.util.JsonUtils;
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
	private static Double lastECpoint = null;
	private HttpClient client = buildHttpClient();

	@Autowired
	EventHandlerConfiguration eventHandlerConfig;

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
			BuildingEntity messageObject = JsonUtils.fromJson(message, BuildingEntity.class);
			processMessage(messageObject);	
		}
	}

	private void processMessage(BuildingEntity message) {
		Long buildingId = null;

			Alert alert = new Alert();

/*			if("INDOOR_TEMPERATURE".equalsIgnoreCase(messageType)){
				if(messageValue < 65 || messageValue > 75){
					saveAlertMessage(alert);
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
					}
				}
			}*/
	}

	/**
	 * forward message to db handler
	 * @param message
	 */
	private synchronized void saveAlertMessage(Alert message) {
		String dbHandlerEndpoint = eventHandlerConfig.getRuntimeUri() + API;
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
}

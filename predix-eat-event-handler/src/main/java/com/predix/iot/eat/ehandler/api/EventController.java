package com.predix.iot.eat.ehandler.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.predix.iot.eat.ehandler.config.AuditTrailConfiguration;
import com.predix.iot.eat.ehandler.config.UaaConfiguration;
import com.predix.iot.eat.ehandler.entity.Alert;
import com.predix.iot.eat.ehandler.entity.EventEntity;
import com.predix.iot.eat.ehandler.util.EventHandlerUtils;
import com.predix.iot.eat.ehandler.util.JsonUtils;
import com.predix.iot.eat.ehandler.util.UaaUtils;


@RestController
@RequestMapping(value="/alerts")
public class EventController{

	private static Logger log = Logger.getLogger(EventController.class);
	
	@Autowired
	AuditTrailConfiguration eventHandlerConfig;
	
	@Autowired
	UaaConfiguration uaaConfig;
	
	private String token;

	private HttpClient client = buildHttpClient();
	
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	public ResponseEntity<String> saveEvent(@RequestBody List<Alert> alerts) {
		if(alerts == null) {
			log.warn("Alert Message is null");
		} else {
			log.info("Alert Message: " + alerts);
			processMessage(alerts);	
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	private void processMessage(List<Alert> alerts) {
		checkTokenInCache();
		for(Alert alert : alerts){
			EventEntity event = new EventEntity();
			event.setContext("Alert");
			event.setTag(alert.getMessageType());
			event.setClassification(String.valueOf(alert.getBuildingId()));
			event.setData(alert);
			
			saveEvent(event);
		}		
	}

	private synchronized void saveEvent(EventEntity event) {
		String eventHandlerEndpoint = eventHandlerConfig.getRuntimeUri() + "tenants/" + 
				eventHandlerConfig.getTenantUuid() + "/events";
		String eventMessage;
		try {
			EventEntity[] eventArray = new EventEntity[1];
			eventArray[0] = event;

			eventMessage = JsonUtils.MAPPER.writeValueAsString(eventArray);

			if (eventMessage != null) {
				log.debug("Alert Message: " + eventMessage);
				HttpPost request = new HttpPost(eventHandlerEndpoint);

				if (log.isInfoEnabled()) {
					log.info("Event Handler Endpoint: " + eventHandlerEndpoint + ", sending input: " + eventMessage);
				}
				
				request.setHeader("Content-Type", "application/json");
				if(null == token || "".equalsIgnoreCase(token)){
					log.error("TOKEN IS EMPTY");
				}
				request.setHeader("Authorization", "Bearer " + token);
				
				StringEntity input = new StringEntity(eventMessage);
				input.setContentType("application/json");				
				request.setEntity(input);

				HttpResponse response = client.execute(request);
				String rawResponse = EntityUtils.toString(response.getEntity());
				if (log.isInfoEnabled()) {
					log.info("rawResponse: " + rawResponse);
				}
			}else{
				log.error("No Alert Message to Save");
			}
		} catch (Exception e) {
			log.error("Error", e);
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
			log.warn("Can't get the token in uaa", ex);
		}
	}
	
	private void checkTokenInCache(){
		if(token != null) {
			if(UaaUtils.isTokenExpired(token)) {
				UaaUtils.removeTokenInCache(token);
				putTokenToCache();
				log.info("********** TOKEN IS EXPIRED **********");
			}
		} else {
			putTokenToCache();
		}
	}
}

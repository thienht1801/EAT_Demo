package com.predix.iot.eat.temperature.datasimulator;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.predix.iot.eat.temperature.datasimulator.configuration.UaaConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.predix.iot.eat.temperature.datasimulator.app.exceptions.CustomException;
import com.predix.iot.eat.temperature.datasimulator.app.service.GatewayClient;
import com.predix.iot.eat.temperature.datasimulator.timeseries.entity.BuildingEntity;
import com.predix.iot.eat.temperature.datasimulator.utils.UaaUtils;

@Component
public class TemperateSimulator {

	private static final Logger logger = LoggerFactory.getLogger(TemperateSimulator.class);

	@Autowired
	private GatewayClient client;

	@Autowired
	UaaConfiguration uaaConfig;

	private String token;
	
	private static Long maxBuildingId = 10L;
	private static Double maxInDoorTemp = 100.0;
	private static Double maxOutDoorTemp = 100.0;
	private static Double maxEnergyConsume = 500.0;
	private static Double minInDoorTemp = 50.0;
	private static Double minOutDoorTemp = 50.0;
	private static Double minEnergyConsume = 200.0;
	
	
	@Scheduled(fixedDelayString = "${scheduler.time}")
	public void feedData() {
		logger.info("Start feeding temperature data to gateway");

		generateData();
	}

	@Async
	protected void generateData() {
		ObjectMapper mapper = new ObjectMapper();
		BuildingEntity building = new BuildingEntity();
				
		try {
			Long randomId = ThreadLocalRandom.current().nextLong(0, maxBuildingId);
			Double randomInDoorTemp = ThreadLocalRandom.current().nextDouble(minInDoorTemp, maxInDoorTemp);
			Double randomOutDoorTemp = ThreadLocalRandom.current().nextDouble(minOutDoorTemp, maxOutDoorTemp);
			Double randomEnergyConsume = ThreadLocalRandom.current().nextDouble(minEnergyConsume, maxEnergyConsume);
			
			building.setId(randomId);
			building.setInDoorTemp( Math.round( randomInDoorTemp * 100.0 ) / 100.0);
			building.setOutDoorTemp(Math.round( randomOutDoorTemp * 100.0 ) / 100.0);
			building.setEnergyConsume(Math.round( randomEnergyConsume * 100.0 ) / 100.0);
			
			String payload = mapper.writeValueAsString(building);
			sendData(payload);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error("Error in generateData:" + e);
		}

	}

	private <T> void sendData(String building) {
		String payload = building;
		// send message to data gateway
		try {
			client.postTemperatureDataToGateway(payload);
		} catch (CustomException e) {
			e.printStackTrace();
			logger.error("Error in sendData:" + e);
		}
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

}

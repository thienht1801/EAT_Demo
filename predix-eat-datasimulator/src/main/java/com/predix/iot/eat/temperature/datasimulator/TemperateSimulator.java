package com.predix.iot.eat.temperature.datasimulator;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ge.predix.timeseries.model.builder.IngestionRequestBuilder;
import com.ge.predix.timeseries.model.builder.IngestionTag;
import com.ge.predix.timeseries.model.builder.IngestionTag.Builder;
import com.ge.predix.timeseries.model.datapoints.DataPoint;
import com.ge.predix.timeseries.model.datapoints.Quality;
import com.predix.iot.eat.temperature.datasimulator.configuration.UaaConfiguration;
import com.predix.iot.eat.temperature.datasimulator.app.exceptions.TimeSeriesException;
import com.predix.iot.eat.temperature.datasimulator.app.service.GatewayClient;
import com.predix.iot.eat.temperature.datasimulator.timeseries.entity.BuildingEntity;
import com.predix.iot.eat.temperature.datasimulator.utils.EnergyConsumptionGenerator;
import com.predix.iot.eat.temperature.datasimulator.utils.IndoorTemperatureGenerator;
import com.predix.iot.eat.temperature.datasimulator.utils.OutdoorTemperatureGenerator;
import com.predix.iot.eat.temperature.datasimulator.utils.UaaUtils;

@Component
public class TemperateSimulator {

	private static final Logger logger = LoggerFactory.getLogger(TemperateSimulator.class);

	@Autowired
	private GatewayClient client;

	@Autowired
	UaaConfiguration uaaConfig;

	private String token;

	@Scheduled(fixedDelayString = "${scheduler.time}")
	public void feedData() {
		logger.info("Start feeding temperature data to Timeseries");

		generateData();
	}

	@Async
	protected void generateData() {

		BuildingEntity building = new BuildingEntity();
		//add code to generate random data here
		generateAndSendData(building);

	}

	private <T> void generateAndSendData(T asset) {
		String payload = generateData(asset);
		// send message to data gateway
		try {
			client.postTemperatureDataToGateway(payload);
		} catch (TimeSeriesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error in generateData:" + e);
		}
	}

	private static <T> String generateData(T data) {
		String payload = "";
		Long timestamp = Instant.now().toEpochMilli();
		IngestionRequestBuilder ingestionBuilder = IngestionRequestBuilder.createIngestionRequest()
				.withMessageId("MESSAGE_@_" + timestamp)
				.addIngestionTag(
						decorateBuilder(data, IngestionTag.Builder.createIngestionTag().withTagName(OutdoorTemperatureGenerator.TAG))
						.addDataPoints(Arrays.asList(new DataPoint(timestamp, OutdoorTemperatureGenerator.getNextValue(), Quality.GOOD)))
						.build())
				.addIngestionTag(
						decorateBuilder(data, IngestionTag.Builder.createIngestionTag().withTagName(IndoorTemperatureGenerator.TAG))
						.addDataPoints(Arrays.asList(new DataPoint(timestamp, IndoorTemperatureGenerator.getNextValue(), Quality.GOOD)))
						.build())
				.addIngestionTag(
						decorateBuilder(data, IngestionTag.Builder.createIngestionTag().withTagName(EnergyConsumptionGenerator.TAG))
						.addDataPoints(Arrays.asList(new DataPoint(timestamp, EnergyConsumptionGenerator.getNextValue(), Quality.GOOD)))
						.build());
		try {
			payload = ingestionBuilder.build().get(0);
			logger.info("Generate Message: " + payload);
			return payload;
		} catch (IOException e) {
			logger.error("Error while building payload for ingestion ", e);
			return null;
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

	private static <T> Builder decorateBuilder(T data, Builder builder) {
		BuildingEntity building=null;

		if(data instanceof BuildingEntity){
			building = (BuildingEntity)data;
		}

		if (building != null) {
			return decorateBuildingPayload(building, builder);
		}
		
		return builder;
	}

	private static Builder decorateBuildingPayload(BuildingEntity building, Builder builder) {
		return builder.addAttribute("buildingId", building.getId() + "");
	}
}

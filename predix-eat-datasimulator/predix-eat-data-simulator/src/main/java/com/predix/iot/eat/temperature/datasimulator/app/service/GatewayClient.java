package com.predix.iot.eat.temperature.datasimulator.app.service;

import com.predix.iot.eat.temperature.datasimulator.app.exceptions.CustomException;

public interface GatewayClient {


	public void postTemperatureDataToGateway(String message) throws CustomException;

}

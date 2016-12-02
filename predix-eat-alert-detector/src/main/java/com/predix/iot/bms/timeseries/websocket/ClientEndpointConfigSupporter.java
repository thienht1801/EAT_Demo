package com.predix.iot.bms.timeseries.websocket;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;

public class ClientEndpointConfigSupporter {

	public static ClientEndpointConfig getClientEndpointConfig(Configurator configurator) {
		return ClientEndpointConfig.Builder.create().configurator(configurator).build();
	}
}

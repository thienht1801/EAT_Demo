package com.predix.iot.bms.timeseries.websocket;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.ClientEndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class ClientEndpointConfigurator extends ClientEndpointConfig.Configurator {

	private static final Logger logger = LoggerFactory.getLogger(ClientEndpointConfigurator.class);
	
	private HttpHeaders tsHeaders;

	public ClientEndpointConfigurator(HttpHeaders headers) {
		this.tsHeaders = headers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.websocket.ClientEndpointConfig.Configurator#beforeRequest(java.util
	 * .Map)
	 */
	@Override
	public void beforeRequest(Map<String, List<String>> headers) {
		super.beforeRequest(headers);
		for (Entry<String, List<String>> entry : this.tsHeaders.entrySet()) {
			headers.put(entry.getKey(), entry.getValue());
		}
		logger.info(headers.toString());
	}
}

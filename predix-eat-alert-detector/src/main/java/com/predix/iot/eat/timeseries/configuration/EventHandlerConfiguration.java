package com.predix.iot.eat.timeseries.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "event-handler")
public class EventHandlerConfiguration {

	private String runtimeUri;

	public String getRuntimeUri() {
		return runtimeUri;
	}

	public void setRuntimeUri(String runtimeUri) {
		this.runtimeUri = runtimeUri;
	}
		
}

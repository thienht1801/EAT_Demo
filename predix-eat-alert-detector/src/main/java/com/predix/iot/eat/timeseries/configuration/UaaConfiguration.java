package com.predix.iot.eat.timeseries.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="uaa")
public class UaaConfiguration {
	
	String clientId;
	String clientSecret;
	String uri;
	String clientBase64Token;
	
	public String getClientId() {
		return clientId;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getClientBase64Token() {
		return clientBase64Token;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public void setClientBase64Token(String clientBase64Token) {
		this.clientBase64Token = clientBase64Token;
	}

	
}

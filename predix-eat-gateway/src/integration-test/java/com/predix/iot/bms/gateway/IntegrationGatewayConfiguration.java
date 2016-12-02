package com.predix.iot.wts.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tai Huynh
 * */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="gatewayAPI")
public class IntegrationGatewayConfiguration {
	
	private String gatewayUri;
	private String assetUri;
	private String assetInstanceId;
	
	/**
	 * @return the assetInstanceId
	 */
	public String getAssetInstanceId() {
		return assetInstanceId;
	}
	/**
	 * @param assetInstanceId the assetInstanceId to set
	 */
	public void setAssetInstanceId(String assetInstanceId) {
		this.assetInstanceId = assetInstanceId;
	}
	/**
	 * @return the gatewayUri
	 */
	public String getGatewayUri() {
		return gatewayUri;
	}
	/**
	 * @param gatewayUri the gatewayUri to set
	 */
	public void setGatewayUri(String gatewayUri) {
		this.gatewayUri = gatewayUri;
	}
	/**
	 * @return the assetUri
	 */
	public String getAssetUri() {
		return assetUri;
	}
	/**
	 * @param assetUri the assetUri to set
	 */
	public void setAssetUri(String assetUri) {
		this.assetUri = assetUri;
	}
	
	
}
/**
 * 
 */
package com.predix.iot.bms.timeseries.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tai Huynh
 * The class contains all the configuration for Router 
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "util")
public class UtilConfiguration {
	//in milliseconds
	private int uiTimeOut = 500;

	/**
	 * @return the uiTimeOut
	 */
	public int getUiTimeOut() {
		return uiTimeOut;
	}

	/**
	 * @param uiTimeOut the uiTimeOut to set
	 */
	public void setUiTimeOut(int uiTimeOut) {
		this.uiTimeOut = uiTimeOut;
	}
}

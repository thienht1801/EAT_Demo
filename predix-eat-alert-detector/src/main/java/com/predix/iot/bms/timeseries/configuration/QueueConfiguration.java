package com.predix.iot.bms.timeseries.configuration;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@EnableConfigurationProperties
@EnableRabbit
public class QueueConfiguration {

	private String alertQueueName;

	/**
	 * @return the alertQueueName
	 */
	public String getAlertQueueName() {
		return alertQueueName;
	}

	/**
	 * @param alertQueueName the alertQueueName to set
	 */
	public void setAlertQueueName(String alertQueueName) {
		this.alertQueueName = alertQueueName;
	}


	
}

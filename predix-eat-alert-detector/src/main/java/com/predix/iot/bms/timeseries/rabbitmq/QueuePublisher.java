package com.predix.iot.bms.timeseries.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.predix.iot.bms.timeseries.configuration.QueueConfiguration;

@Component
public class QueuePublisher {

	@Autowired
	private QueueConfiguration queueConfig;

	@Autowired
	RabbitTemplate rabbitTemplate;

	public int sendIncidentMessage(String message) {
		rabbitTemplate.convertAndSend(queueConfig.getAlertQueueName(), message);
		return 1;
	}
}

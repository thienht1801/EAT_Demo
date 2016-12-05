package com.predix.iot.eat.gateway.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class QueuePublisher {
	private static final Logger logger = LoggerFactory.getLogger(QueuePublisher.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private final String FANOUT_EXCHANGE_NAME =  "fanout";

	public void sendTimeseriesMessage(String message){
		logger.info("---------- Gateway receive message --------" + message);
		rabbitTemplate.setExchange(FANOUT_EXCHANGE_NAME);
		rabbitTemplate.convertAndSend(message);
		if(logger.isDebugEnabled()) {
			logger.debug("---------------######### COMPLETE WITH MESSAGE ########-----------" + message);
		}
	}

}

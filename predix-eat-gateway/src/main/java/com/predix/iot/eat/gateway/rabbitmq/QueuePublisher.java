package com.predix.iot.eat.gateway.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * modified by Tai Huynh
 * publish message to all listeners:
 * + TimeseriesListener
 * + TimeseriesIncidentListener
 * + TimeseriesUIListener
 * */
@Component
public class QueuePublisher {
	private static final Logger logger = LoggerFactory.getLogger(QueuePublisher.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private final String FANOUT_EXCHANGE_NAME =  "fanout";
	/**
	 * send message to listeners
	 * @param message the content to send. should be a TimeTag
	 * sample data: {"messageId":"1453338376222","body":[{"name":"TaiHT_Test","datapoints":[[1460571280258,487,2],[1460571280259,935,2]],"attributes":{"host":"server1","customer":"Acme"}}]}
	 * */
	public void sendTimeseriesMessage(String message){
		logger.info("---------- Gateway receive message --------" + message);
		rabbitTemplate.setExchange(FANOUT_EXCHANGE_NAME);
		rabbitTemplate.convertAndSend(message);
		if(logger.isDebugEnabled()) {
			logger.debug("---------------######### COMPLETE WITH MESSAGE ########-----------" + message);
		}
	}

}

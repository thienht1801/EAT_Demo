package com.predix.iot.bms.timeseries.rabbitmq;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.predix.iot.bms.timeseries.configuration.QueueConfiguration;
import com.predix.iot.bms.timeseries.rabbitmq.QueuePublisher;



public class QueuePublisherTest {

	private QueuePublisher publisher;

	@Autowired
	private QueueConfiguration queueConfig;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Before
	public void setup(){
		publisher=new QueuePublisher();
		setupMockQueueConfiguration();
		setupMockRabbit();
	}

	private void setupMockRabbit(){
		rabbitTemplate=Mockito.mock(RabbitTemplate.class);
		Mockito.doNothing().when(rabbitTemplate).convertAndSend(Matchers.anyObject());
		ReflectionTestUtils.setField(publisher, "rabbitTemplate",rabbitTemplate);
	}

	private void setupMockQueueConfiguration(){
		queueConfig=Mockito.mock(QueueConfiguration.class);
		Mockito.when(queueConfig.getAlertQueueName()).thenReturn("abc");
		ReflectionTestUtils.setField(publisher, "queueConfig",queueConfig);
	}

	@Test
	public void testSendTimeseriesMessage() {
		String message="abc";
		int result=publisher.sendIncidentMessage(message);
		assertTrue(result==1);
	}


}

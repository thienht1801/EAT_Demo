package com.predix.iot.bms.gateway.rabbitmq;

import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.predix.iot.eat.gateway.rabbitmq.QueuePublisher;

//FIXME: this test class does not run any testing. Need to rework to make it work.

public class QueuePublisherTest {

	private static final String TIMESERIES_QUEUE_NAME = "timeseriesQueue";
    private QueuePublisher publisher;

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
		Mockito.when(TIMESERIES_QUEUE_NAME).thenReturn("abc");
	}

}

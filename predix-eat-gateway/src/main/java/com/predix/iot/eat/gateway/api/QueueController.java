package com.predix.iot.eat.gateway.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.predix.iot.eat.gateway.rabbitmq.QueuePublisher;

@RestController
public class QueueController {

	private static Logger log = Logger.getLogger(QueueController.class);

	@Autowired
	private QueuePublisher publisher;


	/**
	 * Query API with create queue message
	 *
	 * @return ResponseEntity object
	 */
	@RequestMapping(value = "/queues", method = RequestMethod.POST)
	public ResponseEntity<Void> createQueue(@RequestBody String timeserieData) {
		log.debug("Create queue  message for time series data: " + timeserieData);
		publisher.sendTimeseriesMessage(timeserieData);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

}

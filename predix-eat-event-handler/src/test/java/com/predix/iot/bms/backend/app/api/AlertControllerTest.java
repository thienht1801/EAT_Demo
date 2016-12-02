package com.predix.iot.bms.backend.app.api;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.predix.iot.bms.dbhandler.app.Application;
import com.predix.iot.bms.dbhandler.repository.AlertRepository;


/**
 * Unit test for Incident controller 
 * 
 * @author Anh V. Nguyen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class AlertControllerTest extends BaseControllerTest<AlertRepository> {
	
}

/**
 * 
 */
package com.predix.iot.bms.timeseries.listener.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.predix.iot.bms.timeseries.configuration.UtilConfiguration;

/**
 * @author Tai Huynh
 *
 */

@ActiveProfiles("unit-test")
@SpringApplicationConfiguration(classes = UtilConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UtilConfigurationTest {

	@Autowired
	private UtilConfiguration utilConfig;

	/**
	 * check the current Asset Timeout config is valid number
	 * */
	@Test
	public void testValidConfigForTimeOut() {
		Assert.assertEquals(500, utilConfig.getUiTimeOut());
	}

}

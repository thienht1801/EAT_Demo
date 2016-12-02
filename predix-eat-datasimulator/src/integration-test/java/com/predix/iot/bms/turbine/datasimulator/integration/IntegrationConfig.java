/**
 * predix-wts-turbine-datasimulator
 */
package com.predix.iot.wts.turbine.datasimulator.integration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.predix.iot.wts.common.configurations.UaaConfiguration;
import com.predix.iot.wts.turbine.datasimulator.configuration.TimeSeriesConfiguration;

/******************************************************************
 * IntegrationConfig
 * 
 * @author Andy
 * @date Feb 2, 2016
 * 
 ******************************************************************/
@Configuration
@ComponentScan(basePackageClasses = { TimeSeriesConfiguration.class, UaaConfiguration.class, CrudOperation.class })
public class IntegrationConfig {

}

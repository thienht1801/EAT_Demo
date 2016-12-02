/**
 * predix-wts-turbine-datasimulator
 */
package com.predix.iot.wts.turbine.datasimulator.integration;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.predix.iot.wts.common.asset.entity.Location;
import com.predix.iot.wts.common.asset.entity.TurbineAsset;
import com.predix.iot.wts.common.configurations.BackendConfiguration;
import com.predix.iot.wts.common.configurations.UaaConfiguration;
import com.predix.iot.wts.common.constants.Constants;
import com.predix.iot.wts.common.utils.UaaUtils;
import com.predix.iot.wts.test.annotation.type.IntegrationTest;
import com.predix.iot.wts.turbine.datasimulator.configuration.TimeSeriesConfiguration;

/******************************************************************
 * SimulatorIntegrationTest
 * 
 * @author Andy
 * @date Jan 25, 2016
 * 
 ******************************************************************/

@ActiveProfiles("integration")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationConfig.class)
@Category(IntegrationTest.class)
public class ITSimulator {

	@Autowired
	BackendConfiguration backendConfig;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UaaConfiguration uaaConfig;

	@Autowired
	TimeSeriesConfiguration simConfig;

	@Autowired
	CrudOperation<TurbineAsset> crudOp;

	TurbineAsset turbineA;
	String token;
	String turbineUrl;
	HttpHeaders headers;

	private static final String PATH = "/turbines";
	private static final String TS_QUERY = "{\"start\":\"%s\",\"end\":\"%s\",\"tags\":[{\"name\":\"%s\",\"limit\":100}]}";

	@Before
	public void setup() {
		token = UaaUtils.getAdminAccessToken(uaaConfig.getUri(), uaaConfig.getClientBase64Token());
		turbineUrl = backendConfig.getUri() + PATH;
		headers = new HttpHeaders();
		headers.add(Constants.TS_AUTHORIZATION, Constants.TOKEN_BEARER + token);

		turbineA = new TurbineAsset();
		turbineA.setUri(Constants.TURBINE_DOMAIN + "/1");
		turbineA.setId(1l);
		turbineA.setName("turbine_test");
		turbineA.setActive(true);
		turbineA.setExceptionModeEnabled(true);
		turbineA.setLocation(new Location());
		turbineA.setModel("GE");
		turbineA.setStatus("OK");
		turbineA.setMinPower(800);
		turbineA.setMaxPower(1200);

		turbineA = crudOp.insertData(turbineUrl, turbineA, headers, TurbineAsset.class);
	}

	@After
	public void teardown() {
		crudOp.deleteData(turbineUrl, turbineA.getId(), headers);
	}

	@Test
	public void test_getActiveTurbines() throws InterruptedException {
		Thread.sleep(60000);

		HttpHeaders tsHeaders = new HttpHeaders();
		tsHeaders.add(Constants.TS_AUTHORIZATION, Constants.TOKEN_BEARER + token);
		tsHeaders.add(Constants.TS_PREDIX_ZONE_ID, simConfig.getInstanceId());
		tsHeaders.setContentType(MediaType.APPLICATION_JSON);

		String query = String.format(TS_QUERY, "2mi-ago", "1ms-ago", Constants.TAG_NAME_PREFIX + turbineA.getId());
		System.out.println(query);
		HttpEntity<String> request = new HttpEntity<String>(query, tsHeaders);

		ResponseEntity<String> response = restTemplate.exchange(simConfig.getQueryUri(), HttpMethod.POST, request,
				String.class);
		System.out.println(response.getBody());
		Assertions.assertThat(response.getBody()).contains("manufacturer");
	}
}

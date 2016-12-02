/**
 * 
 */
package com.predix.iot.wts.gateway;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.predix.iot.wts.common.asset.entity.Location;
import com.predix.iot.wts.common.asset.entity.TurbineAsset;
import com.predix.iot.wts.common.configurations.BackendConfiguration;
import com.predix.iot.wts.common.configurations.UaaConfiguration;
import com.predix.iot.wts.common.constants.Constants;
import com.predix.iot.wts.common.utils.JsonUtils;
import com.predix.iot.wts.common.utils.UaaUtils;
import com.predix.iot.wts.test.annotation.type.IntegrationTest;

/**
 * @author Tai Huynh
 * Test Integration Incidents-Handler with DB Handler.
 */

@ActiveProfiles("integration")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(PredixWtsGatewayApplication.class)
@Category(IntegrationTest.class)
public class GatewayIT {

	//constants for the integration
	private static final int TIMEOUT = 5000;
	private Client client = ClientBuilder.newClient().property(ClientProperties.CONNECT_TIMEOUT, TIMEOUT);
	private static final String COUNT_PATH = "incidents/count";
	private static final String QUERY_PATH = "incidents/query";
	
	@Autowired
	private BackendConfiguration backendConfig;

	@Autowired
	private IntegrationGatewayConfiguration gatewayConfig;

	@Autowired
	private UaaConfiguration uaaConfig;
	
	
	private TurbineAsset sampleTurbine = new TurbineAsset();
	private String token;
	
	/**
	 * set up sample Turbine to post to Asset Service
	 * also get the valid token once to do the integration test
	 * **/
	@Before
	public void setUp() {
		sampleTurbine.setUri(Constants.TURBINE_DOMAIN + "/9999");
		sampleTurbine.setName("Integration-Turbine");
		sampleTurbine.setMaxPower(100);
		sampleTurbine.setMinPower(80);
		sampleTurbine.setStartDate(new Date());
		sampleTurbine.setOverModeEnabled(false);
		sampleTurbine.setActive(false);
		sampleTurbine.setExceptionModeEnabled(false);
		sampleTurbine.setLocation(new Location(1, 1));
		sampleTurbine.setModel("Integration_Mode");
		sampleTurbine.setLocation(new Location(123.0, 456.0));
		sampleTurbine.setWindFarm("IntegrationTest");
		//Token
		token = UaaUtils.getAdminAccessToken(uaaConfig);
			
		List<TurbineAsset> assets = new ArrayList<TurbineAsset>();
		assets.add(sampleTurbine);
		WebTarget target = client.property(ClientProperties.CONNECT_TIMEOUT, TIMEOUT)
				.property(ClientProperties.READ_TIMEOUT, TIMEOUT).target(gatewayConfig.getAssetUri()).path(Constants.TURBINE_DOMAIN);
		//post sample Turbine to Asset Service
		Response response = 
				target.request(MediaType.APPLICATION_JSON_VALUE)
				.header(Constants.TS_AUTHORIZATION, "Bearer " + token)
				.header(Constants.TS_PREDIX_ZONE_ID, gatewayConfig.getAssetInstanceId())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.post(Entity.entity(JsonUtils.toJson(assets), MediaType.APPLICATION_JSON_VALUE));

	}

	/**
	 * delete sample Turbine Asset in Asset Service
	 * delete incident record in DB Handler
	 * **/
	@After
	public void teardown() {
		//delete Turbine Asset
		WebTarget target = client.property(ClientProperties.CONNECT_TIMEOUT, TIMEOUT)
				.property(ClientProperties.READ_TIMEOUT, TIMEOUT).target(gatewayConfig.getAssetUri()).path(sampleTurbine.getUri());
		Response response = target.request().header(Constants.TS_AUTHORIZATION, "Bearer " + token)
				.header(Constants.TS_PREDIX_ZONE_ID, gatewayConfig.getAssetInstanceId()).delete();
		
		//delete incident
		WebTarget webTargetCount = client
				.target(backendConfig.getUri()).path(QUERY_PATH);
		String incidentsList = webTargetCount.request()
				.header(Constants.TS_AUTHORIZATION, "Bearer " + UaaUtils.getAdminAccessToken(uaaConfig))
				.get(String.class);
		JSONArray jsonObject = null;
		jsonObject = new JSONArray(incidentsList);
		JSONObject object = (JSONObject) jsonObject.get(0);
		webTargetCount = client.target(backendConfig.getUri()).path("/incidents" + "/" + object.get("id"));
		webTargetCount.request()
		.header(Constants.TS_AUTHORIZATION, "Bearer " + UaaUtils.getAdminAccessToken(uaaConfig))
		.delete();
	}
	
	/**
	 * Step 1: get total incidents in DB Handler
	 * Step 2: send one turbine Asset to Queue. The turbine asset goes 
	 * 							from Gateway -> 
	 * 										Router -> 
	 * 												Incident-Handler -> 
	 * 													 			Db-handler.
	 * Step 3: get total incidents in DB Handler. Current value should be increased 1.
	 * 
	 * */
	@Test
	public void sendIncidentTest() {
		WebTarget webTargetCount = client
				.target(backendConfig.getUri()).path(COUNT_PATH);
		String response =	webTargetCount.request()
				.header(Constants.TS_AUTHORIZATION, "Bearer " + UaaUtils.getAdminAccessToken(uaaConfig))
				.get(String.class);
		int beforeTotal = Integer.parseInt(response);
		
		TsWebSocketRequest message = TimeSeriesUtils.getRequestV2(sampleTurbine);
		
		String timeseriesData = JsonUtils.toJson(message);
		webTargetCount = client.target(gatewayConfig.getGatewayUri()).path("queues");
		
		Response response1 = webTargetCount.request(MediaType.APPLICATION_JSON_VALUE)
				.header("Authorization", "Bearer " + UaaUtils.getAdminAccessToken(uaaConfig))
				.post(Entity.entity(timeseriesData, MediaType.APPLICATION_JSON_VALUE));
		System.out.println(timeseriesData);
		System.out.println(response1.getStatus());
		
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		webTargetCount = client
				.target(backendConfig.getUri()).path(COUNT_PATH);
		response =	webTargetCount.request()
				.header(Constants.TS_AUTHORIZATION, "Bearer " + UaaUtils.getAdminAccessToken(uaaConfig))
				.get(String.class);
		int currentTotal = Integer.parseInt(response);

		Assert.assertEquals(beforeTotal+1, currentTotal);
	}
}

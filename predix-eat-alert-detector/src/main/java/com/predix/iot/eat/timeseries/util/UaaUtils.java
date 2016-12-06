package com.predix.iot.eat.timeseries.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.predix.iot.eat.timeseries.configuration.UaaConfiguration;

/**
 * Utilities to work with UAA APIs.
 *
 * @author Dang Ngo (dangndq@fsoft.com.vn)
 */
public class UaaUtils {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(UaaUtils.class);

	/** The Constant AUTHORIZATION. */
	private static final String AUTHORIZATION = "Authorization";

	/** The Constant CONNECTION_TIMEOUT. */
	private static final int CONNECTION_TIMEOUT = 3000;

	/** The Constant READ_TIMEOUT. */
	private static final int READ_TIMEOUT = 3000;

	/** The client. */
	private static Client client = null;

	static {
		client = ClientBuilder.newClient()
				.property(ClientProperties.CONNECT_TIMEOUT, CONNECTION_TIMEOUT)
				.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
	};

	// cache the token with the Expired time
	//this is just for memory cache for each module
	private static final Map<String, Long> USERINFO_LOCAL_CACHE = new HashMap<String, Long>();
	 /**
	 * Gets the admin access token.
	 *
	 * @param uaaConfig the uaa config
	 * @return return token of client id
	 */
	public static String getAdminAccessToken(UaaConfiguration uaaConfig) {
		String uaaUri = uaaConfig.getUri();
		String clientBase64Token = uaaConfig.getClientBase64Token();
		try {
			WebTarget target = client.target(uaaUri).path("oauth/token");
			Form form = new Form();
			form.param("grant_type", "client_credentials");
			String response = target
					.request()
					.header(AUTHORIZATION, "Basic " + clientBase64Token)
					.post(Entity.entity(form,
							MediaType.APPLICATION_FORM_URLENCODED_VALUE),
							String.class);
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject.getString("access_token");
		} catch (Exception ex) {
			logger.error("Exception in getAdminAccessToken: ", ex);
			return null;
		}
	}

	/**
	 * put a token to local cache
	 * @param token token to save
	 * @param expiredTime allInfo to save
	 * */
	public static void putTokenToCache(String token, Long expiredTime) {
		USERINFO_LOCAL_CACHE.put(token, expiredTime);
	}

	/**
	 * 
	 * get the JSONObject in UAA
	 * Sample: 
	 * {  
			   "sub":"predix_client",
			   "iss":"https://87e1a0d6-63a8-4b3f-acc8-489bb4f0df25.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token",
			   "client_id":"predix_client",
			   "aud":[  
			      "predix_client",
			      "acs.policies",
			      "predix-acs.zones.9077f3b9-5610-4b73-b52e-16a77ac6b894",
			      "timeseries.zones.1b8a4e13-537c-4ead-b98a-7d33c126adc1",
			      "scim",
			      "uaa",
			      "acs.attributes",
			      "openid",
			      "predix-asset.zones.76dc41a2-365a-4b53-aa15-b04b7de943d7"
			   ],
			   "zid":"87e1a0d6-63a8-4b3f-acc8-489bb4f0df25",
			   "grant_type":"client_credentials",
			   "azp":"predix_client",
			   "scope":[  
			      "acs.policies.read",
			      "predix-acs.zones.9077f3b9-5610-4b73-b52e-16a77ac6b894.user",
			      "acs.policies.write",
			      "timeseries.zones.1b8a4e13-537c-4ead-b98a-7d33c126adc1.user",
			      "scim.me",
			      "uaa.resource",
			      "timeseries.zones.1b8a4e13-537c-4ead-b98a-7d33c126adc1.query",
			      "acs.attributes.read",
			      "openid",
			      "timeseries.zones.1b8a4e13-537c-4ead-b98a-7d33c126adc1.ingest",
			      "predix-asset.zones.76dc41a2-365a-4b53-aa15-b04b7de943d7.user",
			      "acs.attributes.write"
			   ],
			   "exp":1465471798,
			   "iat":1465428598,
			   "jti":"0563365e-f1ee-4430-9531-2f457cb16c23",
			   "rev_sig":"45842e45",
			   "cid":"predix_client"
			}
			@param token the token
			@param uaaConfig uaaConfig 
	 * */
	public static JSONObject getAccountInfoInUaa(String token, UaaConfiguration uaaConfig) {
		try {
			WebTarget target = client.target(uaaConfig.getUri()).path("check_token");
			Form form = new Form();
			form.param("token", token);
			String response = target
					.request()
					.header(AUTHORIZATION, "Basic " + uaaConfig.getClientBase64Token())
					.post(Entity.entity(form,
							MediaType.APPLICATION_FORM_URLENCODED_VALUE),
							String.class);
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject;
		} catch (Exception ex) {
			logger.error("Exception in getAllAccountInfo: ", ex);
			return null;
		}
	}
	/**
	 * check the token is expired or not
	 * @param token the token in cache
	 * */
	public static boolean isTokenExpired(String token) {
		Long expiredTime = USERINFO_LOCAL_CACHE.get(token);
		if(expiredTime == null || expiredTime.longValue() == 0) {
			return true;
		} 
		boolean isExpired = System.currentTimeMillis() > expiredTime.longValue();
		return isExpired;
	}
	
	/**
	 * remove token in local cache
	 * @param token token to save
	 * */
	public static void removeTokenInCache(String token) {
		USERINFO_LOCAL_CACHE.remove(token);
	}
}

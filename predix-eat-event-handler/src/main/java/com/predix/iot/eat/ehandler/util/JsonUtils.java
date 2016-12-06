/**
 * predix-wts-timeseries-listener
 */
package com.predix.iot.eat.ehandler.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/******************************************************************
 * JsonUtils
 * 
 * @author Andy
 * @date Apr 22, 2016
 * 
 ******************************************************************/

public class JsonUtils {

	private static Logger log = LoggerFactory.getLogger(JsonUtils.class);

	public static ObjectMapper MAPPER = new ObjectMapper();

	public static <T> T fromJson(String json, Class<T> valueType) {
		try {
			return MAPPER.readValue(json, valueType);
		} catch (JsonParseException e) {
			log.error("Message incorrect format: ", e);
		} catch (JsonMappingException e) {
			log.error("Message incorrect format: ", e);
		} catch (IOException e) {
			log.error("IOException occurs: ", e);
		}
		return null;
	}

	public static String toJson(Object o) {
		try {
			return MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.error("Unable to process object: ", e);
		}
		return null;
	}
}

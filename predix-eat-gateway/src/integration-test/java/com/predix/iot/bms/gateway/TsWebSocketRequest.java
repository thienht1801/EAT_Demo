package com.predix.iot.wts.gateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TsWebSocketRequest {
	
	private static final Logger logger = LoggerFactory.getLogger(TsWebSocketRequest.class);
	
	private String messageId;

	private List<TimeTag> body;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public List<TimeTag> getBody() {
		return body;
	}

	public void setBody(List<TimeTag> body) {
		this.body = body;
	}

	public String toJson() {
		String val = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			val = mapper.writeValueAsString(this);
		} catch (Exception e) {
			logger.error("TsWebSocketRequest parsing error " + e);
		}

		return val;
	}
}

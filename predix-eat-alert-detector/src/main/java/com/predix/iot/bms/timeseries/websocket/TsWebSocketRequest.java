package com.predix.iot.bms.timeseries.websocket;

import java.util.ArrayList;
import java.util.List;

import com.predix.iot.bms.common.dto.TimeTag;

/**
 * The Class TsWebSocketRequest.
 */
public class TsWebSocketRequest {

	/** The message id. */
	private String messageId;

	/** The body. */
	private List<TimeTag> body = new ArrayList<>();

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Sets the message id.
	 *
	 * @param messageId
	 *            the new message id
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public List<TimeTag> getBody() {
		return body;
	}

	/**
	 * Sets the body.
	 *
	 * @param body
	 *            the new body
	 */
	public void setBody(List<TimeTag> body) {
		this.body = body;
	}
}

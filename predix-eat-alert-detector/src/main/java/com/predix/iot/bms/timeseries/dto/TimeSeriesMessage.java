/**
 * 
 */
package com.predix.iot.bms.timeseries.dto;

import java.util.List;

import com.predix.iot.bms.common.dto.TimeTag;

/**
 * @author Tai Huynh
 *
 */
public class TimeSeriesMessage {

	private String messageId;

	private List<TimeTag> body;
	/**
	 * @return the body
	 */
	public List<TimeTag> getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(List<TimeTag> body) {
		this.body = body;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}

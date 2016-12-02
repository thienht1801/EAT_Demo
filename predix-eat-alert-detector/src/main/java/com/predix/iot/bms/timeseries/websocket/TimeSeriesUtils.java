package com.predix.iot.bms.timeseries.websocket;

import java.util.List;

import com.predix.iot.bms.common.constants.Constants;
import com.predix.iot.bms.common.dto.TimeTag;

public class TimeSeriesUtils {

	public static final String TURBINE_LOCATION = "location";
	public static final String TURBINE_MANUFACTURER = "manufacturer";


	/**
	 * Generate request for websocket for list of turbines
	 *
	 * @param tags
	 * @return
	 */
	public static TsWebSocketRequest getRequestByTags(List<TimeTag> tags) {
		TsWebSocketRequest request = new TsWebSocketRequest();
		request.setMessageId(Constants.TAG_NAME_PREFIX + System.currentTimeMillis());
		request.setBody(tags);
		return request;
	}
}

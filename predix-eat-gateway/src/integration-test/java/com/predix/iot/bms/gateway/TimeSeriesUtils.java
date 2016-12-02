package com.predix.iot.wts.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.predix.iot.wts.common.asset.entity.TurbineAsset;
import com.predix.iot.wts.common.constants.Constants;

public class TimeSeriesUtils {

	public static final String LOCATION_LAT = "lat";
	public static final String LOCATION_LNG = "lng";
	public static final String TURBINE_MODEL = "model";
	public static final String WIND_FARM = "windFarm";

	/**
	 * Generate request for WebSocket;
	 * 
	 * @param turbine
	 * @return
	 */
	public static TsWebSocketRequest getRequestV2(TurbineAsset turbine) {
		TimeTag tag = getTagV2(turbine);
		List<TimeTag> tags = new ArrayList<TimeTag>();
		tags.add(tag);

		return getRequestByTags(tags);
	}

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

	/**
	 * Generate TimeTag base on Turbine's info
	 * 
	 * @param turbine
	 * @version 2 (for TurbineAsset)
	 * @return
	 */
	public static TimeTag getTagV2(TurbineAsset turbine) {
		TimeTag tag = new TimeTag();
		tag.setName(turbine.getUri());
		long timestamp = System.currentTimeMillis();
		Double value = 12.50;
		Integer quality = 1;
		Double max = turbine.getMaxPower();
		Double min = turbine.getMinPower();
		if (max == null || max <= 0) {
			max = 1000.0;
		}
		if (min == null || min <= 0) {
			min = max * 0.8;
		}

		Object[][] datapoint = { { timestamp, value, quality } };
		tag.setDatapoints(datapoint);

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(LOCATION_LAT, String.valueOf(turbine.getLocation().getLat()));
		attributes.put(LOCATION_LNG, String.valueOf(turbine.getLocation().getLng()));
		attributes.put(TURBINE_MODEL, turbine.getModel() == null ? " " : turbine.getModel());
		attributes.put(WIND_FARM, turbine.getModel() == null ? " " : turbine.getWindFarm());
		tag.setAttributes(attributes);

		return tag;
	}

}

package com.predix.iot.wts.gateway;

import java.util.Map;

public class TimeTag {
	private String name;
	private Object[][] datapoints;
	private Map<String, String> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object[][] getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(Object[][] datapoints) {
		this.datapoints = datapoints;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}

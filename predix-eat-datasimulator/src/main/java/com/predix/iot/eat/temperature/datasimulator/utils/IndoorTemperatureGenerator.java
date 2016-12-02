package com.predix.iot.eat.temperature.datasimulator.utils;

public class IndoorTemperatureGenerator {
	
	public static final String TAG = "INDOOR_TEMPERATURE";

	public static int getNextValue() {
		return SampleData.getData(TAG);
	}
}

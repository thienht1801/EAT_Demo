package com.predix.iot.eat.temperature.datasimulator.utils;

public class OutdoorTemperatureGenerator {

	public static final String TAG = "OUTDOOR_TEMPERATURE";

	public static int getNextValue() {
		return SampleData.getData(TAG);
	}
}

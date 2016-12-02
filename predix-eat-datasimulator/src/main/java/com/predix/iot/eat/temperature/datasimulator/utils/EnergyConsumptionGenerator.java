package com.predix.iot.eat.temperature.datasimulator.utils;

import java.util.concurrent.ThreadLocalRandom;

public class EnergyConsumptionGenerator {
	
	public static final String TAG = "ENERGY_CONSUMPTION";

	public static int getNextValue() {
		
		ThreadLocalRandom randomizer = ThreadLocalRandom.current();
		
		boolean isPeak = (randomizer.nextInt(0, 5) % 5) == 0;

		if (isPeak) {
			int sampleData = SampleData.getRawData(TAG);
			return sampleData + randomizer.nextInt(sampleData / 4, sampleData / 3);
		}
		return SampleData.getData(TAG);
	}
}

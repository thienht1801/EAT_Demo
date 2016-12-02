package com.predix.iot.eat.temperature.datasimulator.utils;

import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class SampleData {

	private static HashMap<String, ArrayList<Integer>> sampleData;
	private static ArrayList<ArrayList<Integer>> sampleDataFlatten;

	public static void load() throws FileNotFoundException {
		Scanner scanner = new Scanner(SampleData.class.getResourceAsStream("/hourly_normal.csv"));
		String[] headers = scanner.nextLine().split(",");
		sampleData = new HashMap<String, ArrayList<Integer>>();
		sampleDataFlatten = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < headers.length; i++) {
			ArrayList<Integer> data = new ArrayList<Integer>();
			sampleDataFlatten.add(data);
			sampleData.put(headers[i], data);
		}

		while (scanner.hasNext()) {
			String[] values = scanner.nextLine().split(",");
			for (int i = 0; i < values.length; i++) {
				sampleDataFlatten.get(i).add(Integer.parseInt(values[i]));
			}
		}
		scanner.close();
	}

	public static int getData(String tag) {
		int baseValue = getRawData(tag);

		return baseValue + ThreadLocalRandom.current().nextInt(-baseValue / 5, baseValue==0?1:(baseValue / 5));
	}

	public static int getRawData(String tag) {
		return sampleData.get(tag).get(LocalTime.now().getHour());
	}
}

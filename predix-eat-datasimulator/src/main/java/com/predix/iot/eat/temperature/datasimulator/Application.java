package com.predix.iot.eat.temperature.datasimulator;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.predix.iot.eat.temperature.datasimulator.utils.SampleData;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "com.predix.iot, com.ge.predix.solsvc")
public class Application {

	public static void main(String[] args) throws FileNotFoundException {
		SampleData.load();
		SpringApplication.run(Application.class, args);
	}
}

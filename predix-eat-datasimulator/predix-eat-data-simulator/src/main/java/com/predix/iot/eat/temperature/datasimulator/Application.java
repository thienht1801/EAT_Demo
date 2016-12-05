package com.predix.iot.eat.temperature.datasimulator;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "com.predix.iot")
public class Application {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(Application.class, args);
	}
}

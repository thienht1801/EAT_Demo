package com.predix.iot.eat.router.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.predix.iot")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

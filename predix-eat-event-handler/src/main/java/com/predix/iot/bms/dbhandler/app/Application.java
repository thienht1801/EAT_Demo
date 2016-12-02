package com.predix.iot.bms.dbhandler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages="com.predix.iot")
@EnableJpaRepositories("com.predix.iot.bms.dbhandler.repository")
@EntityScan("com.predix.iot.bms.dbhandler.entity")
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

package com.emtech.ushurusmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class  UshuruSmartApplication {
	public static void main(String[] args) {
		SpringApplication.run(UshuruSmartApplication.class, args);
	}
}

package com.plus33.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Plus33ErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(Plus33ErpApplication.class, args);
	}

}

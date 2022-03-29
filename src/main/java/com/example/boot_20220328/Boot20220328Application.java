package com.example.boot_20220328;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.example.controller",
		"com.example.service",
		"com.example.config"
})
public class Boot20220328Application {

	public static void main(String[] args) {
		SpringApplication.run(Boot20220328Application.class, args);
	}

}

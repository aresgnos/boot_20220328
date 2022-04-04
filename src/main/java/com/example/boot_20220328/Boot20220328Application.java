package com.example.boot_20220328;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy // aop 추가
// 컨트롤러, 환경파일, 서비스(mybatis 1버전용)
@ComponentScan(basePackages = {
		"com.example.controller",
		"com.example.service",
		"com.example.config",
		"com.example.aop",
		"com.example.interceptor"
})

// Mapper(mybatis 2버전용)
@MapperScan(basePackages = {
		"com.example.mapper"
})
public class Boot20220328Application {

	public static void main(String[] args) {
		SpringApplication.run(Boot20220328Application.class, args);
	}

}

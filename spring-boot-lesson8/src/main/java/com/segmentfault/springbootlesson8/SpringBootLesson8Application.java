package com.segmentfault.springbootlesson8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringBootLesson8Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLesson8Application.class, args);
	}
}

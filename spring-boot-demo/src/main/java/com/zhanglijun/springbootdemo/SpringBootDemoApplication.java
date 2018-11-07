package com.zhanglijun.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "com.zhanglijun.springbootdemo")
@EnableAutoConfiguration
@EnableAsync
// 扫描自定义Servlet
@ServletComponentScan(basePackages = {"com.zhanglijun.springbootdemo.segmentfault.lesson4"})
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

	@RequestMapping(value = "/getString")
	public String getString() {
		Map<String, Object> map = new HashMap<>();
		return "string";
	}
}

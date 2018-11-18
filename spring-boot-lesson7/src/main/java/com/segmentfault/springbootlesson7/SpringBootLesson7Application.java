package com.segmentfault.springbootlesson7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
// 因为配置了mysql的驱动和jdbc的驱动，所以默认会注入DataSourceAutoConfiguration中的配置为dataSource的配置
// 这里没有去配dataSource访问相关的属性，所以exclude掉让项目能起来
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
// 测试mybatis-starter 所以又注释掉上边的代码
public class SpringBootLesson7Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLesson7Application.class, args);
	}
}

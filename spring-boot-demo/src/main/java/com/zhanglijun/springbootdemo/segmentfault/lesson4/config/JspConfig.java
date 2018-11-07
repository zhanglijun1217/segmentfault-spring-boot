package com.zhanglijun.springbootdemo.segmentfault.lesson4.config;

import com.zhanglijun.springbootdemo.SpringBootDemoApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * jsp config
 * springboot使用jsp要配置 实现抽象类SpringBootServletInitializer 组装SpringApplicationBuilder
 * @author 夸克
 * @date 22/10/2018 00:37
 */
public class JspConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // source启动类 告知一些静态资源
        builder.sources(SpringBootDemoApplication.class);
        return builder;
    }
}

package com.zhanglijun.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 夸克
 * @date 2018/9/24 15:21
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 创建文档基本信息
                .apiInfo(apiInfo())
                // 选择器
                .select()
                // 扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.zhanglijun.springbootdemo.web.controller"))
                // 路径扫描
//                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("spring-boot-demo rest api swagger接口文档")
                // 创建人
                .contact("zlj")
                // 版本号
                .version("1.0")
                // 描述
                .description("api文档")
                .build();
    }
}

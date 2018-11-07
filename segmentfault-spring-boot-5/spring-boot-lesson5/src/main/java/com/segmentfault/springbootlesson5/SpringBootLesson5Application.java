package com.segmentfault.springbootlesson5;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootLesson5Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLesson5Application.class, args);
	}

	@Bean
	public static EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
		return new EmbeddedServletContainerCustomizer(){
			@Override
			public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
				if (configurableEmbeddedServletContainer instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory cast = TomcatEmbeddedServletContainerFactory.class.cast(configurableEmbeddedServletContainer);

                    // 添加上下文的配置
                    cast.addContextCustomizers(new TomcatContextCustomizer() {
                        @Override
                        public void customize(Context context) {
                            // 访问的路径
                            context.setPath("/spring-boot");
                        }
                    });

                    // 添加内置容器tomcat的一些配置 相当于传统容器中的server.xml中的配置
                    cast.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                        @Override
                        public void customize(Connector connector) {
                            connector.setPort(8888);
                            // 这个方法现在不建议 但是可以使用
                            connector.setProtocol(Http11Nio2Protocol.class.getName());
                        }
                    });
                }
			}
		};
	}
}



package com.zhanglijun.springbootdemo.segmentfault.lesson4.config;

import com.zhanglijun.springbootdemo.segmentfault.lesson4.servlet_api.listener.MyListener;
import com.zhanglijun.springbootdemo.segmentfault.lesson4.spring_boot.filter.MyFilter2;
import com.zhanglijun.springbootdemo.segmentfault.lesson4.spring_boot.servelt.MyServlet2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * @author 夸克
 * @date 22/10/2018 00:49
 */
@Configuration
public class Spring_boot_apiConfig {

    /**
     * 配置servlet bean 配合spring-boot api注册servlet
     * @return
     */
    @Bean
    public static ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();

        servletRegistrationBean.setServlet(new MyServlet2());
        servletRegistrationBean.addUrlMappings("/myServlet2");
        servletRegistrationBean.setName("myServlet2");
        servletRegistrationBean.addInitParameter("name", "myServlet2");

        return servletRegistrationBean;
    }

    /**
     * 配置filter bean 配合spring-boot api注册filter
     * @return
     */
    @Bean
    public static FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        filterRegistrationBean.setFilter(new MyFilter2());
        filterRegistrationBean.addServletNames("myServlet2");
        // 触发过滤器的时机
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.FORWARD);

        return filterRegistrationBean;
    }

    /**
     * 配置listener bean 配合spring-boot api注册listener
     * @return
     */
    @Bean
    public static ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();

        // 这样会注册两遍Listener 因为这个Listener中有WebListener注解
        servletListenerRegistrationBean.setListener(new MyListener());

        return servletListenerRegistrationBean;
    }
}

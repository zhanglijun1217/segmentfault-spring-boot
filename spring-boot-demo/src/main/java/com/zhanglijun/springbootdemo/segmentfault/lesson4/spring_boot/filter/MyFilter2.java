package com.zhanglijun.springbootdemo.segmentfault.lesson4.spring_boot.filter;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring-boot api方式注册一个Filter
 * @author 夸克
 * @date 21/10/2018 22:48
 */
public class MyFilter2 extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getRequestUri();

        filterChain.doFilter(request, response);
    }

    /**
     * 在这个方法中没有request对象，但是可以通过RequestContextHolder.getRequestAttributes方法获取request对象、
     * 因为RequestContextHolder里会存放ServletRequestAttributes 这其中会有request对象 这其中的原理是ThreadLocal
     * 主要在RequestContextListener这个Listener中的init中塞入这个线程 destory方法中去clear这个ThreadLocal（防止内存泄漏）
     */
    private void getRequestUri() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 强转为ServletRequestAttributes
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        ServletContext servletContext = request.getServletContext();
        String requestURI = request.getRequestURI();
        servletContext.log(requestURI + " was filterd");
    }
}

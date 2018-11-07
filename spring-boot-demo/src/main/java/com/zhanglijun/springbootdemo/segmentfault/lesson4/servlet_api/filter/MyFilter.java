package com.zhanglijun.springbootdemo.segmentfault.lesson4.servlet_api.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现自己的Filter 需要指定对应的servletName
 *   这个Filter也是可以@ServletCompontScan
 *
 * @author 夸克
 * @date 17/10/2018 22:52
 */
@WebFilter(servletNames = "myServlet")
public class MyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // servlet中的log
        ServletContext servletContext = request.getServletContext();
        // 会在执行servlet中输出log
        servletContext.log("/myServlet was filtered");

        filterChain.doFilter(request, response);
    }
}

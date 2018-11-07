package com.zhanglijun.springbootdemo.segmentfault.lesson4.servlet_api.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * 自定义listener listener不用指定对应的servlet
 *   也可以通过启动类中的@ServletComponentScan去扫描生这个@WebListener注解
 *
 * @author 夸克
 * @date 18/10/2018 09:58
 */
@WebListener
public class MyListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletContext servletContext = sre.getServletContext();
        servletContext.log("request is initilalized!");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ServletContext servletContext = sre.getServletContext();
        servletContext.log("request was destory");
    }
}

package com.zhanglijun.springbootdemo.segmentfault.lesson4.spring_boot.servelt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 使用Spring-boot api方式注册api
 *
 * @author 夸克
 * @date 21/10/2018 22:35
 */
public class MyServlet2 extends HttpServlet {

    private String value;

    @Override
    public void init (ServletConfig servletConfig) throws ServletException{
        // 这里要调用父类init方法 初始化其中的servletConfig 否则在下边用的时候拿不到这个值
        super.init(servletConfig);
        // 这里getInitParameter方法必须在初始化中去拿这个参数
        value = servletConfig.getInitParameter("name");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext servletContext = getServletContext();
        servletContext.log("servlet do doGet");
        Writer writer = response.getWriter();

        writer.write("<html><body>Hello,World, value = " + value +"</body></html>");
    }
}

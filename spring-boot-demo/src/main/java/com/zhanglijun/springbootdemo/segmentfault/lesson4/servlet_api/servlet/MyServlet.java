package com.zhanglijun.springbootdemo.segmentfault.lesson4.servlet_api.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 实现自己的Servlet
 *  利用@ServletComponentScan 注解扫描自定义servlet
 * @author 夸克
 * @date 17/10/2018 22:30
 */
@WebServlet(
        name = "myServlet",
        urlPatterns = "/myServlet",
        // 加入访问的参数 在ServletConfig中初始化这些属性 这里定义了一个初始化参数的名字和值
        initParams = {
                @WebInitParam(name = "myName", value = "myValue")
        }
)
public class MyServlet extends HttpServlet {

    private String value;

    /**
     * 当servlet被装配好之后 会调用init方法
     * @param servletConfig
     */
    @Override
    public void init (ServletConfig servletConfig) throws ServletException{
        // 这里要调用父类init方法 初始化其中的servletConfig 否则在下边用的时候拿不到这个值
        super.init(servletConfig);
        value = servletConfig.getInitParameter("myName");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletContext servletContext = getServletContext();
        servletContext.log("servlet do doGet");
        Writer writer = response.getWriter();

        writer.write("<html><body>Hello,World, value = " + value +"</body></html>");
    }
}

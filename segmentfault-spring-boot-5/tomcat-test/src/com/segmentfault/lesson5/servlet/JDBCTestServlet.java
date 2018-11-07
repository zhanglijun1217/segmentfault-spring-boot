package com.segmentfault.lesson5.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * 测试数据库连接的 servlet
 * @author zhanglijun
 *
 */
public class JDBCTestServlet extends HttpServlet{

	/**
	 *  序列化 这里要清楚为什么去需要序列化，大多数servlet是不需要去序列化的
	 */
	private static final long serialVersionUID = -6664514407229211022L;
	
	// 定义数据源
	private DataSource dataSource;
	
	public void init(ServletConfig servletConfig) throws ServletException {
		
		try {
			// 利用JNDI技术获取数据库的连接
			Context context = new InitialContext();
			// 上下文
			Context evnContext = (Context) context.lookup("java:comp/env");
			// 从resource中找到这个dataSource
			dataSource = (DataSource) evnContext.lookup("jdbc/TestDB");
			
			// 测试获取一个bean
			String bean = (String) evnContext.lookup("Bean");
			System.out.println(bean);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		PrintWriter write = response.getWriter();
		// 因为下边要输出<br>标签 所以设置一下输出格式
		response.setContentType("text/html;charset=UTF-8");
		
		
		try {
			// 拿到数据库连接
			Connection connection = dataSource.getConnection();			
			
			// statement 
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SHOW DATABASES");
			// 打印结果集
			while(resultSet.next()) {
				String dataName = resultSet.getString(1);
				write.write(dataName);
				write.write("</br>");
				write.flush();
			}
			
			connection.close();
			
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	

}

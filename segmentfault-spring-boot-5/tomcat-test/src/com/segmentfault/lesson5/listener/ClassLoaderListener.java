package com.segmentfault.lesson5.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ClassLoaderListener implements ServletContextListener {

	/**
	 * 初始化打印类加载器
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		
		ClassLoader classLoader = servletContext.getClassLoader();

		while(true) {
			// 打印classLoader的名称
			System.out.println(classLoader.getClass().getName());
			classLoader = classLoader.getParent();
			// classLoader为空时退出循环条件 classLoader向上找会一直找到null
			if (null == classLoader) {
				break;
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}

package com.segmentfault.lesson5;

/**
 * 说明 类加载机制 -- 双亲委派模型
 * @author zhanglijun
 *
 */
public class Main {

	public static void main(String[] args) {
		// 当前线程的classLoader
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		while(true) {
			// 打印classLoader的名称
			System.out.println(classLoader.getClass().getName());
			classLoader = classLoader.getParent();
			// classLoader为空时退出循环条件
			if (null == classLoader) {
				break;
			}
		}
		
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		System.out.println(systemClassLoader.getClass().getName());
	}
}

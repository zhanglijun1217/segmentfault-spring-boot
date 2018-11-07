package com.zhanglijun.springbootdemo.util.concurrent.aijia.factory;


import com.zhanglijun.springbootdemo.util.concurrent.aijia.TaskProcess;

/**
 * 任务处理创建工厂
 * 用于控制 核心池线程数 和 池线程数
 * @author 夸克
 * @date 2018/9/10 01:27
 */
public interface TaskProcessFactory {

    /**
     * 任务处理器
     *
     * <pre>
     * 使用默认的线程池
     * </pre>
     * @return taskProcess
     */
    TaskProcess createTaskProcess(String domain);
}

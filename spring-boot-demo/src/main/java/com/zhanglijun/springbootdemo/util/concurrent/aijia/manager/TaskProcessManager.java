package com.zhanglijun.springbootdemo.util.concurrent.aijia.manager;

import com.zhanglijun.springbootdemo.util.concurrent.aijia.TaskProcess;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.factory.DefaultTaskProcessFactory;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.factory.TaskProcessFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步任务执行管理器
 * @author 夸克
 * @date 2018/9/10 01:30
 */
public class TaskProcessManager {

    /**
     * 私有构造函数，外包不能初始化
     */
    private TaskProcessManager() {
    }

    /**
     * 默认的业务执行域
     */
    private static final String DEFAULT_BUSINESS_DOMAIN = "DEFAULT_DOMAIN";

    /**
     * 执行器的容器，根据业务域来区分<br>
     * 每个业务域只有一个执行器
     */
    private static Map<String, TaskProcess> taskProcessContainer = new ConcurrentHashMap<String, TaskProcess>();

    /**
     * 默认异步任务执行器工厂
     */
    private static TaskProcessFactory defaultTaskProcessFactory = new DefaultTaskProcessFactory();

    /**
     * 获取执行器
     * @param businessDomain 业务域
     * @param factory 任务处理工厂
     * @return 执行器
     */
    public static TaskProcess getTaskProcess(String businessDomain, TaskProcessFactory factory) {
        if (factory == null) {
            factory = defaultTaskProcessFactory;
        }
        TaskProcess taskProcess = taskProcessContainer.get(businessDomain);
        if (taskProcess == null) {
            // double check
            synchronized (TaskProcessManager.class) {
                taskProcess = taskProcessContainer.get(businessDomain);
                if (taskProcess == null) {
                    taskProcess = factory.createTaskProcess(businessDomain);
                    taskProcessContainer.put(businessDomain, taskProcess);
                }
            }
        }
        return taskProcess;
    }

    /**
     * 获取执行器，使用默认任务处理工厂
     * @param businessDomain 业务域
     * @return 执行器
     */
    public static TaskProcess getTaskProcess(String businessDomain) {
        return getTaskProcess(businessDomain, defaultTaskProcessFactory);
    }

    /**
     * 获取执行器，使用默认业务执行域
     * @param factory
     * @return
     */
    public static TaskProcess getTaskProcess(TaskProcessFactory factory) {
        return getTaskProcess(DEFAULT_BUSINESS_DOMAIN, factory);
    }

    /**
     * 使用默认的任务执行器
     * @return
     */
    public static TaskProcess getTaskProcess() {
        return getTaskProcess(DEFAULT_BUSINESS_DOMAIN, defaultTaskProcessFactory);
    }
}

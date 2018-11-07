package com.zhanglijun.springbootdemo.util.concurrent.aijia.factory;

import com.zhanglijun.springbootdemo.util.concurrent.aijia.TaskProcess;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务执行器工厂
 *
 * @author 夸克
 * @date 2018/9/10 01:31
 */
@Setter
@Accessors(chain = true)
public class DefaultTaskProcessFactory implements TaskProcessFactory{

    private static int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static int DEFAULT_POOL_SIZE = DEFAULT_CORE_SIZE * 2;

    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 线程数
     */
    private int poolSize;

    @Override
    public TaskProcess createTaskProcess(String domain) {
        return createTaskProcess(domain, corePoolSize, poolSize);
    }

    public TaskProcess createTaskProcess(String domain, int corePoolSize, int poolSize) {
        // corePoolSize or poolSize illegal, use default size
        if (judgeSize(corePoolSize) || judgeSize(poolSize)) {
            corePoolSize = DEFAULT_CORE_SIZE;
            poolSize = DEFAULT_POOL_SIZE;
        }

        return new TaskProcess(domain, corePoolSize, poolSize);
    }

    public TaskProcess createTaskProcess(ThreadPoolExecutor executor) {
        return new TaskProcess(executor);
    }

    /**
     * 判断数量
     *
     * @param size
     * @return
     */
    private boolean judgeSize(int size) {
        return size < 0 || size == 0;
    }

}

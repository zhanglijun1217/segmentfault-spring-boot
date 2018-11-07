package com.zhanglijun.springbootdemo.domain.task;

import java.util.concurrent.Callable;

/**
 * 线程通用任务
 * @author 夸克
 * @date 2018/9/10 00:27
 */
public interface CurrentTask<V> extends Callable<V> {

    /**
     * 返回本任务名字
     *
     * @return
     */
    public String getTaskName();
}

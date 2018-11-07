package com.zhanglijun.springbootdemo.util.concurrent.aijia.task;

/**
 * 任务回调封装 所有的执行任务
 * @author 夸克
 * @date 2018/9/10 01:29
 */
public interface TaskAction<T> {
    T doInAction() throws Exception;
}

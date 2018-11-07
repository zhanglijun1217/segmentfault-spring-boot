package com.zhanglijun.springbootdemo.util.concurrent.aijia.task;

/**
 * 标记型任务
 * @author 夸克
 * @date 2018/9/10 01:33
 */
public interface IdentityTaskAction<T> extends TaskAction<T> {

    /**
     * 返回标记名称
     * @return
     */
    String identity();
}

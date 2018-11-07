package com.zhanglijun.springbootdemo.util.concurrent.aijia.task;

import java.util.function.Supplier;

/**
 * 任务构造器
 *   异步任务直接使用方法引用或者lambda表达式
 * @author 夸克
 * @date 17/10/2018 16:24
 */
public class TaskActionBuilder {

    /**
     * 构造TaskAction任务
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> TaskAction<T> buildTaskAction(Supplier<T> supplier) {
        return new TaskAction<T>() {
            @Override
            public T doInAction() throws Exception {
                return supplier.get();
            }
        };
    }

    public static <T> IdentityTaskAction<T> buildIdentityTaskAction(String identity, Supplier<T> supplier) {
        return new IdentityTaskAction<T>() {
            @Override
            public String identity() {
                return identity;
            }

            @Override
            public T doInAction() throws Exception {
                return supplier.get();
            }
        };
    }
}

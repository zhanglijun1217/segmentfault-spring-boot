package com.zhanglijun.springbootdemo.util.concurrent.aijia;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 返回值Holder 用这个Holder主要是为了处理future.get()中的异常
 * @author 夸克
 * @date 2018/9/10 01:30
 */
@Slf4j
public class Holder <T>{

    private Future<T> future;

    public Holder(Future<T> future) {
        super();
        this.future = future;
    }

    /**
     * 返回holder中的返回值
     * @return
     */
    public T getResult() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("holder future getResult exception" + e);
        }
    }

    /**
     * 返回holder中的返回值（有超时时间）
     * @param timeout
     * @return
     */
    public T getResult(long timeout) {
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("holder future getResult exception" + e);
        } catch (TimeoutException e) {
            log.warn("holder future getResult timeout, and limit timeout is " + timeout);
            return null;
        }
    }

}

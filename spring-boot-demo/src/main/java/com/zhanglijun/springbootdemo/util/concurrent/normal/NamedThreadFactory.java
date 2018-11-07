package com.zhanglijun.springbootdemo.util.concurrent.normal;

import java.util.HashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 夸克
 * @date 2018/9/10 00:23
 */
public class NamedThreadFactory implements ThreadFactory {
    private static HashMap<String/*namePrefix*/, AtomicInteger> threadIdTable = new HashMap<String, AtomicInteger>();

    private String namePrefix;

    private boolean isDamon;

    public NamedThreadFactory(String namePrefix) {
        this(namePrefix, true);
    }

    public NamedThreadFactory(String namePrefix, boolean isDamon) {
        this.namePrefix = namePrefix;
        this.isDamon = isDamon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(namePrefix + "-" + generateThreadId(namePrefix));
        thread.setDaemon(isDamon);
        return thread;
    }

    private static synchronized int generateThreadId(String topic) {

        if (!threadIdTable.containsKey(topic)) {
            threadIdTable.put(topic, new AtomicInteger(1));
        }

        AtomicInteger threadId = threadIdTable.get(topic);
        return threadId.getAndIncrement();
    }
}

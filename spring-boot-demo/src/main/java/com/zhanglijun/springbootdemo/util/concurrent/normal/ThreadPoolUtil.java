package com.zhanglijun.springbootdemo.util.concurrent.normal;

import com.zhanglijun.springbootdemo.domain.task.CurrentTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 夸克
 * @date 2018/9/10 00:24
 */
@Slf4j
public class ThreadPoolUtil {

    private static final int DEFAULT_MONITOR_PERIOD = 60;
    private int corePoolSize;
    private int maxPoolSize;
    /**
     * 监控时间间隔，单位：s
     */
    private int monitorPeriod;

    private AtomicLong rejectedTaskCount = new AtomicLong(0);
    private AtomicBoolean initFlag = new AtomicBoolean(false);
    private ThreadPoolExecutor threadPool;
    private ScheduledExecutorService monitor;
    private String bizName;
    private Runnable monitorTask = new Runnable() {
        @Override
        public void run() {
            try {
                // 正在执行的任务数
                int activeCount = threadPool.getActiveCount();
                // 已完成任务数
                long completedTaskCount = threadPool.getCompletedTaskCount();
                // 总任务数
                long totalTaskCount = threadPool.getTaskCount();
                // 最大线程数
                int largestPoolSize = threadPool.getLargestPoolSize();
                int poolSize = threadPool.getPoolSize();
                log.warn("active_thread:{}, waiting_thread:{}, completed_thread:{}, rejected_thread:{}, current_pool_size:{}, largest_pool_size:{}",
                        new Object[]{activeCount, totalTaskCount - activeCount - completedTaskCount, completedTaskCount, rejectedTaskCount.get(), poolSize, largestPoolSize});
            } catch (Exception e) {
                log.error("[SYSTEM-SafeGuard]Monitor thread run fail", e);
            }

        }
    };


    private static Map<String,ThreadPoolUtil> instances=new HashMap<>();

    /**
     * 通用的线程池提供方
     * @param bizName 业务名称
     * @return
     */
    public static ThreadPoolUtil getInstance(String bizName) {
        return getInstance(bizName, 100, 100);
    }

    public static synchronized ThreadPoolUtil getInstance(String bizName, int corePoolSize, int maxPoolSize) {
        if (StringUtils.isEmpty(bizName)) {
            return null;
        }
        if (instances.get(bizName) != null) {
            return instances.get(bizName);
        }

        ThreadPoolUtil threadPoolUtil=new ThreadPoolUtil(bizName);
        threadPoolUtil.setCorePoolSize(corePoolSize);
        threadPoolUtil.setMaxPoolSize(maxPoolSize);
        threadPoolUtil.init();
        instances.put(bizName,threadPoolUtil);
        return threadPoolUtil;
    }

    private ThreadPoolUtil(String bizName){
        log.warn("new {} threadPool",bizName);
        this.bizName=bizName;
    }

    public void init() {
        if (!initFlag.compareAndSet(false, true)) {
            return; // 防止重复初始化
        }

        log.warn("init thread pool, corePoolSize:" + corePoolSize + " maxPoolSize:" + maxPoolSize);

        threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000), new NamedThreadFactory("springbootDemoProcessor"),
                new ThreadPoolExecutor.AbortPolicy()) {

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                log.debug("{} start.", t.getName());
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                printException(r,t);
            }

            private void printException(Runnable r, Throwable t) {
                if (t == null && r instanceof Future<?>) {
                    try {
                        Future<?> future = (Future<?>) r;
                        if (future.isDone()) {
                            future.get();
                        }
                    } catch (CancellationException ce) {
                        t = ce;
                    } catch (ExecutionException ee) {
                        t = ee.getCause();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // ignore/reset
                    }
                }
                if (t != null) {
                    log.warn("{} finish. exception:{}", Thread.currentThread().getName(), t.getMessage(), t);
                }
            }

            @Override
            protected void terminated() {
                super.terminated();
            }
        };

        monitorPeriod = monitorPeriod <= 0 ? DEFAULT_MONITOR_PERIOD : monitorPeriod;
        monitor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("spring-boot-demo-processor-monitor", true));
        monitor.scheduleAtFixedRate(monitorTask, monitorPeriod, monitorPeriod, TimeUnit.SECONDS);
    }


    public <T> Future<T> submit(CurrentTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        log.warn("submit task:{}", task.getTaskName());
        return threadPool.submit(task);
    }

    public void submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        threadPool.submit(task);
    }

    public void destroy() {
        log.warn("start to stop thread pool");
        threadPool.shutdown();
        log.warn("finish to stop thread pool");
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
}

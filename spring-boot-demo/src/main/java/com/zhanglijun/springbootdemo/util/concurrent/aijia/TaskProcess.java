package com.zhanglijun.springbootdemo.util.concurrent.aijia;

import com.zhanglijun.springbootdemo.util.concurrent.aijia.latch.BarrierCountLatch;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.AsyncTaskAction;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.IdentityTaskAction;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.TaskAction;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import org.apache.tomcat.jni.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步任务执行器
 *
 * @author 夸克
 * @date 2018/9/10 01:28
 */
@Getter
public class TaskProcess {

    private static Logger logger = LoggerFactory.getLogger(TaskProcess.class);
    /**
     * 监控线程默认周期
     */
    private static final int DEFAULT_MONITOR_PERIOD = 60;

    /**
     * concurrent执行器
     */
    private ThreadPoolExecutor executor;

    /**
     * 核心线程数
     */
    private int coreSize;

    /**
     * 线程池数
     */
    private int poolSize;

    /**
     * 任务域名称
     */
    private String domain;

    /**
     * 监控任务执行器
     */
    private ScheduledExecutorService monitor;

    /**
     * 监控周期
     */
    private int monitorPeriod;

    /**
     * 初始化标志
     */
    private AtomicBoolean initFlag = new AtomicBoolean(false);


    public TaskProcess(String domain, int coreSize, int poolSize) {
        this.coreSize = coreSize;
        this.poolSize = poolSize;
        this.domain = domain;
        init();
    }

    public TaskProcess(ThreadPoolExecutor executor) {
        this.executor = executor;
        addHook();
    }

    private void createThreadPool() {
        // 防止重复初始化
        if (!initFlag.compareAndSet(false, true)) {
            return;
        }
        // 有界阻塞队列
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(coreSize);
        executor = new ThreadPoolExecutor(coreSize, poolSize, 60,
                TimeUnit.SECONDS, queue, new DefaultThreadFactory(domain), new ThreadPoolExecutor.CallerRunsPolicy());
        logger.warn("init threadPool, coreSize: " + coreSize + " poolSize:" + poolSize);
        // 监控线程的设置 （守护线程）真正生产应用应该控制守护线程log的输出
        monitorPeriod = monitorPeriod <= 0 ? DEFAULT_MONITOR_PERIOD : monitorPeriod;
        monitor = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("processor-executor-monitor"));
        monitor.scheduleAtFixedRate(() -> {
                    try {
                        // 正在执行的任务数
                        int activeCount = executor.getActiveCount();
                        // 已完成任务数
                        long completedTaskCount = executor.getCompletedTaskCount();
                        // 总任务数
                        long totalTaskCount = executor.getTaskCount();
                        // 最大线程数
                        int largestPoolSize = executor.getLargestPoolSize();
                        int poolSize = executor.getPoolSize();
                        logger.warn("active_thread:{}, waiting_thread:{}, completed_thread:{}, current_pool_size:{}, largest_pool_size:{}",
                                activeCount, totalTaskCount - activeCount - completedTaskCount, completedTaskCount, poolSize, largestPoolSize);
                    } catch (Exception e) {
                        logger.error("[SYSTEM-SafeGuard]Monitor thread run fail", e);
                    }
                }, monitorPeriod, monitorPeriod, TimeUnit.SECONDS);

    }

    /**
     * 在jvm中增加一个关闭hook，当jvm关闭的时候，会执行系统中已经设置的所有通过方法addShutdownHook添加的钩子
     * 当系统执行完jdk回调hook，jvm才会关闭。这里hook是去平滑关闭任务执行器中的额
     */
    private void addHook() {
        // JDK回调钩子
        // 这个回调钩子是在线程池执行完任务之后 去执行shutdown和awaitTermination方法
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            // 如果executor为空 则不去返回
            if (executor == null) {
                return;
            }
            try {
                // shutdown要在awaitTermination方法之前 否则会造成线程等待执行shutdown方法，具体见awaitTermination方法
                executor.shutdown();
                logger.info("try to shutdown executor : domain = {}, corePoolSize = {}, poolSize = {}", domain, coreSize, poolSize);
                if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                    logger.warn("did not terminate in the specified time, executor : domain = {}, corePoolSize = {}, poolSize = {}", domain, coreSize, poolSize);
                    // 执行shutdownNow时正在运行的线程
                    List<Runnable> runnables = executor.shutdownNow();
                    logger.warn("abruptly shutdown executor : domain = {}, corePoolSize = {}, poolSize = {}, and will be terminated task number: {}"
                    , domain, coreSize, poolSize, runnables.size());
                }
            } catch (InterruptedException e) {
                // 捕获异常防止hook执行序列被破坏
                logger.warn("interrupted when shut down the process executor:\n{}", e);
            }
        }));
    }

    /**
     * 初始化
     */
    private void init() {
        createThreadPool();
        addHook();
    }

    /**
     * 执行TaskAction并等待执行结果 返回值类型要相同
     *
     * @param tasks
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> executeTask(List<TaskAction<T>> tasks) {
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            return new ArrayList<>();
        }
        TaskAction<T>[] actions = new TaskAction[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            actions[i] = tasks.get(i);
        }
        return executeTask(actions);
    }

    /**
     * 执行TaskAction并等待执行结果
     *
     * @param tasks
     * @return 执行结果
     */
    public <T> List<T> executeTask(TaskAction<T>... tasks) {
        // 使用countDownLatch闭锁来实现 最后汇总的工作在各个线程之后完成
        final CountDownLatch latch = new CountDownLatch(tasks.length);

        List<Future<T>> futures = new ArrayList<Future<T>>();
        List<T> resultList = new ArrayList<>();

        for (final TaskAction<T> runnable : tasks) {
            Future<T> future = executor.submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        return runnable.doInAction();
                    } finally {
                        latch.countDown();
                    }

                }
            });
            futures.add(future);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.info("Executing Task is interrupt.");
        }

        doWithFutures(futures, resultList);
        return resultList;
    }


    /**
     * 提交单个任务 异步执行 有返回值
     *
     * @param task
     * @param <T>
     * @return 返回值用Holder进行封装
     */
    public <T> T syncExecuteWaiting(final TaskAction<T> task) {
        Future<T> future = executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    return task.doInAction();
                } catch (Exception e) {
                    throw new RuntimeException("" + e);
                }
            }
        });
        return new Holder<>(future).getResult();
    }

    /**
     * 异步执行TaskAction，无须等待执行结果
     *
     * @param tasks
     */
    @SuppressWarnings("unchecked")
    public <T> void asyncExecuteTask(List<AsyncTaskAction> tasks) {
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            return;
        }
        AsyncTaskAction[] actions = new AsyncTaskAction[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            actions[i] = tasks.get(i);
        }
        asyncExecuteTask(actions);
    }

    /**
     * 异步执行TaskAction，无须等待执行结果
     *
     * @param tasks
     */
    public void asyncExecuteTask(AsyncTaskAction... tasks) {
        for (final AsyncTaskAction runnable : tasks) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.doInAction();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    /**
     * 并发控制任务执行
     *
     * @param concurrentCount
     * @param tasks
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> executeTaskByConcurrentControl(final int concurrentCount, List<TaskAction<T>> tasks) {
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            return new ArrayList<>();
        }
        TaskAction<T>[] actions = new TaskAction[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            actions[i] = tasks.get(i);
        }
        return executeTaskByConcurrentControl(concurrentCount, actions);
    }

    /**
     * 并发控制任务执行
     *
     * @param concurrentCount
     *            并行任务数
     * @param tasks
     * @return
     */
    public <T> List<T> executeTaskByConcurrentControl(int concurrentCount, TaskAction<T>... tasks) {
        // 总共的任务数
        int modTaskCount = tasks.length;
        // 如果并行任务数大于核心池数量而且核心池线程数量不为0 并行任务数设置成核心线程数
        if (concurrentCount > coreSize && coreSize != 0) {
            concurrentCount = coreSize;
        }
        // 总任务数 mod 并行任务数 得到余数
        int remainTaskCount = tasks.length % concurrentCount;
        // 返回值集合
        List<T> resultList = new ArrayList<>(modTaskCount);
        // future集合
        List<Future<T>> futures = new ArrayList<>(tasks.length);
        // 初始化一个map去存 map中有两个值 <并行控制任务数, 要执行的任务数组>  <模除之后的余数（可能为0）, 要执行的任务数组>
        Map<Integer, TaskAction<T>[]> currentTaskMap = new HashMap<>(2);
        // 当remainTaskCount为0时，直接塞入 <控制并发数, modTaskCount（此时是总任务数）>
        currentTaskMap.put(concurrentCount, Arrays.copyOf(tasks, modTaskCount));

        if (remainTaskCount != 0 && remainTaskCount != modTaskCount) {
            // 如果remainTaskCount不为0 这是塞入两个键值对
            modTaskCount = tasks.length - remainTaskCount;
            currentTaskMap.put(concurrentCount, Arrays.copyOf(tasks, modTaskCount));
            currentTaskMap.put(remainTaskCount, Arrays.copyOfRange(tasks, modTaskCount, tasks.length));
        }
        for (Entry<Integer, TaskAction<T>[]> entry : currentTaskMap.entrySet()) {
            futures.addAll(barrier(entry.getKey(), entry.getValue()));
        }
        doWithFutures(futures, resultList);

        return resultList;
    }

    /**
     * 循环map中 根据并行数执行任务
     * map中键值对 （1）并行控制任务数 -> 根据并行控制数拆分后的任务数组  （2）mod之后的余数 -> 剩余要执行的任务数
     *
     * @param concurrentCount
     * @param tasks
     * @param <T>
     * @return
     */
    private <T> List<Future<T>> barrier(final int concurrentCount, TaskAction<T>... tasks) {
        List<Future<T>> futures = new ArrayList<>(tasks.length);
        // 栅栏数量 控制并行数 > 当前任务数 任务数 : 控制并行数
        int lacth = tasks.length < concurrentCount ? tasks.length : concurrentCount;
        // 循环执行几次加latch的操作
        // 去除控制并发数个任务
        int divide = tasks.length / concurrentCount == 0 ? 1 : tasks.length / concurrentCount;
        for (int i = 0; i < divide; i++) {
            TaskAction<T>[] newTaskActionAry = Arrays.copyOfRange(tasks, i * concurrentCount, (i + 1) * concurrentCount);
            final CountDownLatch latch = new CountDownLatch(lacth);
            for (final TaskAction<T> action : newTaskActionAry) {
                if (action == null) {
                    continue;
                }
                Future<T> future = executor.submit(() -> {
                    try {
                        return action.doInAction();
                        } finally {
                        latch.countDown();
                    }
                });
                futures.add(future);
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info("Executing Task is interrupt.");
            }
        }
        return futures;
    }

    /**
     *  返回值进行封装
     *
     * @param futures
     * @param resultList
     * @param <T>
     */
    private <T> void doWithFutures(List<Future<T>> futures, List<T> resultList) {
        for (Future<T> future : futures) {
            Holder<T> holder = new Holder<>(future);
            T result = holder.getResult();
            if (result == null) {
                logger.warn("executor process execute result is null!");
            } else {
                resultList.add(result);
            }
        }
    }


    /**
     * 并发控制任务执行 可循环latch实现 更加简洁
     *
     * @param concurrentCount
     *            并行任务数
     * @param tasks
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> executeTaskByLatch(int concurrentCount, List<TaskAction<T>> tasks) {
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            return new ArrayList<>();
        }
        TaskAction<T>[] actions = new TaskAction[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            actions[i] = tasks.get(i);
        }
        return executeTaskByLatch(concurrentCount, actions);
    }

    /**
     * 并发控制任务执行
     *
     * @param concurrentCount
     *            并行任务数
     * @param tasks
     * @return
     */
    public <T> List<T> executeTaskByLatch(int concurrentCount, TaskAction<T>... tasks) {
        final BarrierCountLatch latch = new BarrierCountLatch(concurrentCount);

        List<Future<T>> futures = new ArrayList<Future<T>>();
        List<T> resultList = new ArrayList<T>();

        for (final TaskAction<T> runnable : tasks) {

            try {
                latch.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Future<T> future = executor.submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        return runnable.doInAction();
                    } finally {
                        latch.release();
                    }

                }
            });
            futures.add(future);
        }
        doWithFutures(futures, resultList);
        return resultList;
    }


    /**
     * 标识性任务执行，等待执行结果，对任务执行结果分类
     *
     * @param tasks
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> executeIdentityTask(List<IdentityTaskAction<T>> tasks) {
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            return new HashMap<>();
        }
        IdentityTaskAction<T>[] actions = new IdentityTaskAction[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            actions[i] = tasks.get(i);
        }
        return executeIdentityTask(actions);
    }

    /**
     * 标识性任务执行，等待执行结果，对任务执行结果分类
     *
     * @param tasks
     * @return
     */
    public <T> Map<String, T> executeIdentityTask(IdentityTaskAction<T>... tasks) {
        // 闭锁
        final CountDownLatch latch = new CountDownLatch(tasks.length);

        Map<String, Future<T>> futures = new HashMap<>();
        // 存放 <标识->返回值>的map
        Map<String, T> resultMap = new HashMap<>();

        // 执行task
        for (final IdentityTaskAction<T> callableAction : tasks) {
            Future<T> future = executor.submit(() -> {

                    long time = System.currentTimeMillis();
                    try {
                        return callableAction.doInAction();
                    } finally {
                        logger.debug("Executing Task : {} ,time :{}", callableAction.identity(), System.currentTimeMillis() - time);
                        // 闭锁 -1
                        latch.countDown();
                    }

                });
            // 封装<标识 -> Future<T>> map
            futures.put(callableAction.identity(), future);
        }

        try {
            // 闭锁 await方法
            latch.await();
        } catch (InterruptedException e) {
            logger.info("Executing Task is interrupt.");
        }
        // 处理返回值
        Iterator<Entry<String, Future<T>>> it = futures.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Future<T>> entry = it.next();
            try {
                T result = entry.getValue().get();
                resultMap.put(entry.getKey(), result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultMap;
    }

    /**
     * The default thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);

        final ThreadGroup group;

        final AtomicInteger threadNumber = new AtomicInteger(1);

        final String namePrefix;

        DefaultThreadFactory(String domain) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = domain + "-TaskProcess-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                // 守护线程设置为false
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                // 优先级设置为normal
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}

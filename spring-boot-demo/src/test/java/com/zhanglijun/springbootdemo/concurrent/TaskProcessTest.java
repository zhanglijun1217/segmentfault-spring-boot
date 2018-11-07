package com.zhanglijun.springbootdemo.concurrent;

import com.zhanglijun.springbootdemo.util.concurrent.aijia.Holder;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.TaskProcess;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.factory.DefaultTaskProcessFactory;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.manager.TaskProcessManager;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.AsyncTaskAction;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.IdentityTaskAction;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.TaskAction;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.TaskActionBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 异步任务执行器
 *
 * @author 夸克
 * @date 17/10/2018 16:49
 */
@Slf4j
public class TaskProcessTest {

    /**
     * 测试并发生成TaskProcess domain保证单例
     */
    @Test
    @SneakyThrows
    public void testConcurrentInitTaskProcess() {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        List<Future<TaskProcess>> taskProcesses = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            Future<TaskProcess> submit = executorService.submit(new Callable<TaskProcess>() {
                @Override
                public TaskProcess call() throws Exception {
                    TaskProcess taskProcess = TaskProcessManager.getTaskProcess("default");
                    Thread.sleep(200);
                    latch.countDown();
                    return taskProcess;
                }
            });
            // 不要在这里去做future.get() 应该等线程都执行 所以taskProcesses应该是Set<Future<TaskProcess>>
//            taskProcesses.add(submit.get());
            taskProcesses.add(submit);

        }

        latch.await();
        // 取出任务执行器
        Set<ThreadPoolExecutor> processes = taskProcesses.stream().map(e -> new Holder<>(e).getResult().getExecutor()).collect(Collectors.toSet());
        // 如果domain不同 应该初始化100次 默认的执行器应该只初始化一次
        Assert.assertEquals(100, processes.size());
    }

    /**
     * 测试并发执行任务
     */
    @Test
    public void testExecuteTask() {
        TaskAction<String> taskAction = TaskActionBuilder.buildTaskAction(this::businessLogicTaskF);
        List<TaskAction<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(taskAction);
        }

        long startTime = System.currentTimeMillis();
        List<String> strings = TaskProcessManager.getTaskProcess().executeTask(tasks);
        long costTime = System.currentTimeMillis() - startTime;
        log.info("executeTask execute cost time {} MS", costTime);
        Assert.assertTrue(strings.stream().allMatch(e -> Objects.equals(e, "ffffffff")));

    }

    /**
     * 测试异步执行任务
     */
    private static int num = 1;
    @Test
    @SneakyThrows
    public void testAsyncExecuteTask() {
        AsyncTaskAction asyncTaskAction = this::businessLogicTaskA;
        TaskProcessManager.getTaskProcess().asyncExecuteTask(asyncTaskAction);
        // sleep 保证线程把num更新
        TimeUnit.MILLISECONDS.sleep(100);
        Assert.assertEquals(2, num);
    }

    /**
     * 测试另起线程执行一个任务 等待返回值
     */
    @Test
    public void testSyncExecuteTask() {
        TaskAction<List<String>> taskAction = TaskActionBuilder.buildTaskAction(this::businessTaskD);
        List<String> strings = TaskProcessManager.getTaskProcess().syncExecuteWaiting(taskAction);
        Assert.assertTrue(Arrays.asList("A", "B", "...", "X").containsAll(strings));
    }

    /**
     * 测试带标记的任务 比如列表分页 查list 和 count可以作为标记任务并发执行
     */
    private static final String LIST_TASK_IDENTITY = "Junit-List-Task";
    private static final String COUNT_TASK_IDENTITY = "Junit-Count-Task";
    @Test
    public void testExecuteIdentityTask() {
        // 标记任务1
        IdentityTaskAction<List<String>> listIdentityTaskAction =
                TaskActionBuilder.buildIdentityTaskAction(LIST_TASK_IDENTITY, this::businessTaskD);

        // 标记任务2
        IdentityTaskAction<Integer> integerIdentityTaskAction =
                TaskActionBuilder.buildIdentityTaskAction(COUNT_TASK_IDENTITY, this::businessTaskE);

        List<IdentityTaskAction<Object>> list = new ArrayList(){{
            add(listIdentityTaskAction);
            add(integerIdentityTaskAction);
        }};

        Map<String, Object> stringObjectMap = TaskProcessManager.getTaskProcess().executeIdentityTask(list);
        Assert.assertTrue(Arrays.asList("A", "B", "...", "X").containsAll((List<String>) stringObjectMap.get(LIST_TASK_IDENTITY)));
        Assert.assertEquals(10, (int) stringObjectMap.get(COUNT_TASK_IDENTITY));
    }

    /**
     * 测试根据 并发数控制执行任务
     */
    @Test
    public void testExecuteTaskByConcurrent() {
        // 构建任务
        List<TaskAction<String>> taskActions = new ArrayList<>();
        for (int i=0 ; i < 8 ; i++) {
            TaskAction<String> taskAction = TaskActionBuilder.buildTaskAction(this::businessLogicTaskF);
            taskActions.add(taskAction);
        }

        DefaultTaskProcessFactory defaultTaskProcessFactory = new DefaultTaskProcessFactory();
        // 设置小一点 模拟并发控制执行任务
        defaultTaskProcessFactory.setPoolSize(2).setPoolSize(2);
        // 设置2个为并发控制数 执行f方法的时间为90 ms 总共8个任务 线程池corePoolSize=2 poolSize=2 注意这个方法的耗时 大约比 4 * 90ms 大一点 会有线程池创建的时间影响
        long startTime = System.currentTimeMillis();
        List<String> strings = TaskProcessManager.getTaskProcess("Qudao-Default", defaultTaskProcessFactory).executeTaskByConcurrentControl(2, taskActions);
        log.info("costTime {} " ,System.currentTimeMillis() - startTime);
        Assert.assertTrue(strings.stream().allMatch(e -> e.equals("ffffffff")));
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testException() {
        TaskAction<String> taskAction = TaskActionBuilder.buildTaskAction(this::businessLogicTaskB);
        List<TaskAction<String>> taskActions = Arrays.asList(taskAction, taskAction, taskAction);
        // 在执行过程中的Exception 会在Holder中作为RuntimeException抛出来
        thrown.expect(RuntimeException.class);

        // 上边设置expect的异常要在下面真正抛异常方法之前
        List<String> strings = TaskProcessManager.getTaskProcess().executeTask(taskActions);
    }

    /**
     * taskA
     */
    @SneakyThrows
    private void businessLogicTaskA() {
        TimeUnit.MILLISECONDS.sleep(70);
        num++;
        log.info("businessLogicTaskA execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
    }

    /**
     * taskB
     */
    @SneakyThrows
    private String businessLogicTaskB() {
        TimeUnit.MILLISECONDS.sleep(700);
        log.info("businessLogicTaskB execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
        throw new NullPointerException("i am npe");
    }

    /**
     * taskC
     */
    @SneakyThrows
    private void businessLogicTaskC() {
        TimeUnit.MILLISECONDS.sleep(200);
        log.info("businessLogicTaskC execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
    }

    /**
     * taskD
     * @return 返回值
     */
    @SneakyThrows
    private List<String> businessTaskD() {
        TimeUnit.MILLISECONDS.sleep(50);
        log.info("businessLogicTaskD execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
        // result
        return Arrays.asList("A", "B", "...", "X");
    }

    /**
     * taskE
     * @return 返回值map
     */
    @SneakyThrows
    private int businessTaskE() {
        TimeUnit.MILLISECONDS.sleep(70);
        log.info("businessLogicTaskE execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
        return 10;
    }

    /**
     * taskF
     * @return 返回值字符串
     */
    @SneakyThrows
    private String businessLogicTaskF() {
        TimeUnit.MILLISECONDS.sleep(90);
        log.info("businessLogicTaskD execute By thread:[{}] at time : {}", Thread.currentThread().getName(), new Date());
        return "ffffffff";
    }
}

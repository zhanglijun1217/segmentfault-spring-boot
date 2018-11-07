package com.zhanglijun.springbootdemo.concurrent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.factory.DefaultTaskProcessFactory;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.manager.TaskProcessManager;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.IdentityTaskAction;
import com.zhanglijun.springbootdemo.util.concurrent.aijia.task.TaskAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 夸克
 * @date 2018/9/11 11:36
 */
@Slf4j
public class FastTaskTest {


    /**
     * 针对需要的返回值的任务，类似FutureTask
     * 并行执行多个任务，对每个任务做标记，并行执行后，取回每个任务执行结果的返回值
     * （需要保证任务之间没有依赖，否则需要进行同步）
     *
     */
    @SneakyThrows
    @Test
    public void testIdentityTaskAction() {
        // taskD
        IdentityTaskAction<Object> taskD = new IdentityTaskAction<Object>() {
            @Override
            public String identity() {
                return "taskD";
            }

            @Override
            public Object doInAction() throws Exception {
                return businessTaskD();
            }
        };

        // taskE
        IdentityTaskAction<Object> taskE = new IdentityTaskAction<Object>() {
            @Override
            public String identity() {
                return "taskE";
            }

            @Override
            public Object doInAction() throws Exception {
                return businessTaskE();
            }
        };

        // taskF
        IdentityTaskAction<Object> taskF = new IdentityTaskAction<Object>() {
            @Override
            public String identity() {
                return "taskF";
            }

            @Override
            public Object doInAction() throws Exception {
                return businessLogicTaskF();
            }
        };

        // taskD
        IdentityTaskAction<Object> taskD1 = new IdentityTaskAction<Object>() {
            @Override
            public String identity() {
                return "taskD1";
            }

            @Override
            public Object doInAction() throws Exception {
                return businessTaskD();
            }
        };

        // taskE
        IdentityTaskAction<Object> taskE1 = new IdentityTaskAction<Object>() {
            @Override
            public String identity() {
                return "taskE1";
            }

            @Override
            public Object doInAction() throws Exception {
                return businessTaskE();
            }
        };

        // taskF
        IdentityTaskAction<Object> taskF1 = new IdentityTaskAction<Object>() {
            @Override
            public String identity() {
                return "taskF1";
            }

            @Override
            public Object doInAction() throws Exception {
                return businessLogicTaskF();
            }
        };

        // 开始时间
        long start = System.currentTimeMillis();
        Builder<IdentityTaskAction<Object>> builder = ImmutableList.builder();
        List<IdentityTaskAction<Object>> tasks = builder.add(taskD).add(taskE).add(taskF).add(taskE1).add(taskD1).add(taskF1).build();

        // 并发处理带有标记的任务 使用默认的执行器
        Map<String, Object> resultMap = TaskProcessManager.getTaskProcess().executeIdentityTask(tasks);
        // get result by task identity (结果大概是9秒左右 D-sleep-5 E-sleep-7 F-sleep-9)
        log.info("current testIdentityTaskAction elapsed:{}", System.currentTimeMillis() - start);

        log.info("current testTaskAction, result of TaskD:{}",  resultMap.get("taskD"));
        log.info("current testTaskAction, result of TaskE:{}",  resultMap.get("taskE"));
        log.info("current testTaskAction, result of TaskF:{}",  resultMap.get("taskF"));
        log.info("current testTaskAction, result of TaskD1:{}", resultMap.get("taskD1"));
        log.info("current testTaskAction, result of TaskE1:{}", resultMap.get("taskE1"));
        log.info("current testTaskAction, result of TaskF1:{}", resultMap.get("taskF1"));
    }

    /**
     * 并行执行多个任务 任务不带标记
     *
     * （保证任务之间没有依赖，否则需要一定的同步）
     */
    @Test
    public <T> void testExecuteTask() {
        List<TaskAction<T>> tasks = new ArrayList<>();
        // assemble your business logics to be Tasks
        TaskAction<T> taskA = () -> {
            businessLogicTaskA();
            return null;
        };

        TaskAction<T> taskB = () -> {
          businessLogicTaskB();
          return null;
        };

        TaskAction<T> taskC = () -> {
            businessLogicTaskC();
            return null;
        };

        long start = System.currentTimeMillis();
        TaskProcessManager.getTaskProcess().executeTask(tasks);
        // 耗时 9083 ms
        log.info("current testExecuteTask elapsed:{}", System.currentTimeMillis() - start);
        // 不关心结果
    }

    /**
     * 异步并行执行多个任务，不等待返回结果
     */
    @Test
    public <T> void testAsyncExecuteTask() {
        List<TaskAction<T>> tasks = new ArrayList<>();
        // assemble your business logics to be Tasks

        long start = System.currentTimeMillis();

        TaskProcessManager.getTaskProcess().asyncExecuteTask(Arrays.asList());
        // 9ms
        log.info("async current testExecuteTask elapsed:{}", System.currentTimeMillis() - start);
    }


    /**
     * 可控制一次并行执行的最大任务量，以便调整资源的使用
     *
     * 可控制barrier数量，barrier数量到达之后latch打开
     *
     * （保持服务之间没有相互依赖，否则用户需要一定的同步）
     */
    @Test
    public  void testExecuteByLatch() {
        List<TaskAction<Object>> tasks = new ArrayList<>();
        // assemble your business logics to be Tasks
        TaskAction<Object> taskA = () -> {
            businessLogicTaskA();
            return null;
        };

        TaskAction<Object> taskB = () -> {
            businessLogicTaskB();
            return null;
        };

        TaskAction<Object> taskC = () -> {
            businessLogicTaskC();
            return null;
        };

        TaskAction<Object> taskD = () -> businessTaskD();

        TaskAction<Object> taskE = () -> businessTaskE();

        TaskAction<Object> taskF = () -> businessLogicTaskF();
        tasks.add(taskA);
        tasks.add(taskB);
        tasks.add(taskC);
        tasks.add(taskD);
        tasks.add(taskE);
        tasks.add(taskF);

        long start = System.currentTimeMillis();
        // 一次并行执行三个任务
        List<Object> objects = TaskProcessManager.getTaskProcess().executeTaskByLatch(6, tasks);
        log.info("testExecuteByLatch testExecuteTask elapsed:{}", System.currentTimeMillis() - start);

    }

    /**
     * 测试 控制并行数 去执行任务
     */
    @Test
    public void testExecuteTaskByConcurrentControl() {
        List<TaskAction<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tasks.add(() -> businessLogicTaskF());
        }
        // tasks 里有1000个任务
        long start = System.currentTimeMillis();
        List<String> strings = TaskProcessManager.getTaskProcess(new DefaultTaskProcessFactory().setPoolSize(10).setPoolSize(100))
                .executeTaskByConcurrentControl(10, tasks);

        log.info("testExecuteTaskByConcurrentControl elapsed:{}", System.currentTimeMillis() - start);
        log.info("testExecuteTaskByConcurrentControl result is {}", strings.toString());
    }

    /**
     * 测试同步执行任务
     */
    @Test
    public void testSyncExecuteTask() {
        List<String> strings = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
             strings.add(businessLogicTaskF());
        }
        log.info("testSyncExecuteTask elapsed:{}", System.currentTimeMillis() - start);
        log.info("testSyncExecuteTask result is {}", strings.toString());
    }

    /**
     * taskA
     */
    @SneakyThrows
    private void businessLogicTaskA() {
        TimeUnit.SECONDS.sleep(5);
        log.info("businessLogicTaskA execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
    }

    /**
     * taskB
     */
    @SneakyThrows
    private void businessLogicTaskB() {
        TimeUnit.SECONDS.sleep(7);
        log.info("businessLogicTaskB execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
    }

    /**
     * taskC
     */
    @SneakyThrows
    private void businessLogicTaskC() {
        TimeUnit.SECONDS.sleep(9);
        log.info("businessLogicTaskC execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
    }

    /**
     * taskD
     * @return 返回值
     */
    @SneakyThrows
    private List<String> businessTaskD() {
        TimeUnit.SECONDS.sleep(5);
        log.info("businessLogicTaskD execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
        // result
        return Arrays.asList("A", "B", "...", "X");
    }

    /**
     * taskE
     * @return 返回值map
     */
    @SneakyThrows
    private Map<String, String> businessTaskE() {
        TimeUnit.SECONDS.sleep(7);
        Map<String, String> resultMap = ImmutableMap.of("A", "resultA", "B", "resultB");
        log.info("businessLogicTaskE execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
        return resultMap;
    }

    /**
     * taskF
     * @return 返回值字符串
     */
    @SneakyThrows
    private String businessLogicTaskF() {
        TimeUnit.MILLISECONDS.sleep(90);
        String result = new StringBuffer().append("result").append(" of").append(" businessLogicTaskF").toString();
        log.info("businessLogicTaskF execute By thread:[{}] at time:{}", Thread.currentThread().getName(), new Date());
        return result;
    }


}


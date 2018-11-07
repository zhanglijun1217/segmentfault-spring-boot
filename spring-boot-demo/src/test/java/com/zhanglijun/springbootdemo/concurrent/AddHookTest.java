package com.zhanglijun.springbootdemo.concurrent;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 夸克
 * @date 2018/9/13 22:05
 */
@Slf4j
public class AddHookTest {

    /**
     * 测试TaskProcess中加入jdk钩子 关闭线程池的方法
     * shutdown方法和awaitTermination方法配合使用
     *
     * shutdown方法：平滑的关闭ExecutorService，当方法被调用时，ExecutorService停止接收新的任务并且等待已提交的任务
     * （包括提交正在执行和提交未执行）执行完成。当所有的任务执行完毕。线程池即被关闭。
     *
     * awaitTermination方法：接收timeout和TimeUnit两个参数。当等待超过设定时间时，会监测ExecutorService是否已经关闭，
     * 若关闭则返回true，否则返回false。一般情况下和shutdown方法组合使用。
     *
     * https://blog.csdn.net/pengbaoxing/article/details/42006679 shutdown方法和awaitTermination方法的位置导致线程池无法关闭的问题
     *
      */
    @Test
    @SneakyThrows
    public void testAddHook() {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new Task2());
        executorService.submit(new Task2());
        executorService.submit(new Task2());
        executorService.submit(new Task1());

        // shutdown方法必须要在awaitTermination方法前调用 否则会造成线程池中线程一直在waiting 直到超时之后
        /**
         * awaitTermination方法被调用时该方法会被阻塞，这时会去调用线程池中的线程执行任务，直到
         *    （1）任务执行完毕并且调用了shutdown方法
         *    （2）超过了超时时间
         *    （3）被当前线程打断
         *   才会真正执行完毕这个方法
         */
        executorService.shutdown();
        while (!executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
            log.info("线程池没有关闭");
        }

        log.info("线程池已经关闭");

    }

    private class Task1 implements Runnable {
        @Override
        public void run() {
            log.info("任务1");

        }
    }


    private class Task2 implements Runnable {

        @Override
        @SneakyThrows
        public void run() {
            log.info("长时间任务");
            TimeUnit.SECONDS.sleep(5);
        }
    }
}

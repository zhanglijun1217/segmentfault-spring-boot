package com.zhanglijun.springbootdemo.concurrent;

import com.zhanglijun.springbootdemo.util.concurrent.normal.ThreadPoolUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;

/**
 * @author 夸克
 * @date 2018/9/10 00:31
 */

public class ThreadPoolUtilTest {
    private static ThreadPoolUtil threadPoolUtil = ThreadPoolUtil.getInstance("sthreadPoolTest", 10, 100);

    @SneakyThrows
    public static void main(String[] args) {

        Set<Integer> set = new HashSet<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i=0; i < 10; i++) {
            list1.add(i);
        }

        for(int i=10;i<20;i++) {
            list2.add(i);
        }

        threadPoolUtil.submit(() -> set(list1, set));
        threadPoolUtil.submit(() -> set(list2, set));
        Thread.sleep(5000);

    }

    private static void set(List<Integer> list, Set<Integer> set) {
        for (Integer integer : list) {
            set.add(integer);
        }
        System.out.println(set.size());
    }

}

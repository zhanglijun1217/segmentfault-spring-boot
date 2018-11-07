package com.zhanglijun.springbootdemo.util.streamex;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import one.util.streamex.DoubleStreamEx;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

/**
 * 流式操作 streamEX使用
 * @author 夸克
 * @date 2018/9/9 14:13
 */
public class StreamExTest {

    public static void main(String[] args) {
        List<User> list = Lists.newArrayList();
        User user1 = User.builder().age(10).name("123").build();
        User user2 = User.builder().age(20).name("456").build();
        list.add(user1);
        list.add(user2);

        // 1.收集器的快捷方法
        List<String> strings = StreamEx.of(list).map(User::getName).toList();
        System.out.println(strings);

        Map<Integer, List<User>> integerListMap = StreamEx.of(list).groupingBy(User::getAge);
        System.out.println(integerListMap);

        String joining = StreamEx.of(1, 2, 3).joining(";");
        System.out.println(joining);

        // 2.添加指定的元素
        // 在头加上
        List<String> strings1 = StreamEx.of(list).map(User::getName).prepend("prepend").toList();
        System.out.println(strings1);
        // 在尾加上
        System.out.println(StreamEx.of(list).map(User::getName).append("append").toList());

        // 3.去除元素
        List<String> lines = Arrays.asList("as", "shshhs", "fff", "");
        System.out.println(StreamEx.of(lines).remove(e -> e.contains("s")).toList());

        // 4.按照谓词选择map中key
        Map<Integer, User> map = StreamEx.of(list).toMap(User::getAge, Function.identity());
        System.out.println(StreamEx.ofKeys(map, User::isBeyondTen).toSet());

        // 5.在键值对上操作运行
        Map<Object, Object> objectMap = ImmutableMap.builder()
                .put(1, 2)
                .put("2", 3)
                .build();
        System.out.println(EntryStream.of(objectMap).mapKeys(Objects::toString).mapValues(Objects::toString).toMap());

        List<Integer> list1 = Arrays.asList(10, 20, 30);
        map.put(30, null);
        // 找出list1中元素作为key并且包含在map键值对中的 其中value还不能为null的
        Map<Integer, String> objectStringMap = StreamEx.of(list1).mapToEntry(map::get).nonNullValues().mapValues(User::getName).toMap();
        System.out.println(objectStringMap);

        // doubleStream
        double sum = DoubleStreamEx.of(Arrays.asList(1.1, 2.2, 4.4)).pairMap((a, b) -> a - b).sum();




    }

    @Data
    @AllArgsConstructor
    @Accessors(chain = true)
    @Builder
    private static class User{
        private int age;

        private String name;

        private boolean isBeyondTen() {
            return age > 15;
        }

    }
}

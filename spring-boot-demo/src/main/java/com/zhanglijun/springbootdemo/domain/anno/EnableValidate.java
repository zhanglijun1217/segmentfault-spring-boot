package com.zhanglijun.springbootdemo.domain.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.groups.Default;

/**
 * 用来表示开启hibernate校验的注解
 * @author 夸克
 * @create 2018/8/19 22:28
 */
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableValidate {

    Class<?> [] groups() default { Default.class };//校验分组信息
}

package com.zhanglijun.springbootdemo.domain.anno;

import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.*;

/**
 * 自定义dateTime注解 继承@DateTimeFormat注解功能
 * 添加处理终止时间的功能
 * @author 夸克
 * @date 2018/9/18 17:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface MyDateTimeFormat {

    String style() default "SS";

    DateTimeFormat.ISO iso() default DateTimeFormat.ISO.NONE;

    String pattern() default "";

    Type type() default Type.START;

    enum Type {
        /**
         *  起
         */
        START,
        /**
         * 止
         */
        END
    }

}

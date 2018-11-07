package com.zhanglijun.springbootdemo;

import com.zhanglijun.springbootdemo.domain.anno.MyDateTimeFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * @author 夸克
 * @date 2018/9/16 15:02
 */
@Data
public class User extends ResourceSupport {
    private int age;

    private String name;

    @MyDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", type = MyDateTimeFormat.Type.END)
    private Date time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE)
    private Date aTime;
}

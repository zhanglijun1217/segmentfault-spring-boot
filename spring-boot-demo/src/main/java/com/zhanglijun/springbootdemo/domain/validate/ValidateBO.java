package com.zhanglijun.springbootdemo.domain.validate;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 校验BO
 * @author 夸克
 * @create 2018/8/17 10:40
 */
@Data
public class ValidateBO {

    @NotBlank(message = "name不能为空")
    private String name;

    @Min(value = 18, message = "年龄不能小于18岁")
    private Integer age;

    @Email(message = "email格式错误")
    private String email;

    /**
     * 自定义注解 不能包含空格字符串
     */
    @CannotHaveBlank
    private String blank;

    /**
     * 正则校验
     */
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    private String phone;

}

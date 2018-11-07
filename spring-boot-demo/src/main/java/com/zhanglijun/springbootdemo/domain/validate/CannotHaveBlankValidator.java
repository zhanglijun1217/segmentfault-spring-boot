package com.zhanglijun.springbootdemo.domain.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 夸克
 * @create 2018/8/17 13:54
 */
public class CannotHaveBlankValidator implements ConstraintValidator<CannotHaveBlank, String> {


    @Override
    public void initialize(CannotHaveBlank cannotHaveBlank) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //null时不进行校验
        if (value != null && value.contains(" ")) {
            //获取默认提示信息
            String defaultConstraintMessageTemplate = context.getDefaultConstraintMessageTemplate();
            System.out.println("default message :" + defaultConstraintMessageTemplate);
            //禁用默认提示信息
            context.disableDefaultConstraintViolation();
            //设置提示语
            context.buildConstraintViolationWithTemplate("can not contains blank")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

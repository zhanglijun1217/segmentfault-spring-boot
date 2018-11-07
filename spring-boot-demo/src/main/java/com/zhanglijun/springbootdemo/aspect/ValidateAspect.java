package com.zhanglijun.springbootdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * validate注解 方法参数和bean属性参数配置 切面配置
 * @author 夸克
 * @date 2018/9/18 23:52
 */
@Aspect
@Configuration
public class ValidateAspect {
    /**
     * BEAN对象校验器 配置快速失败模式
     */
    private final Validator BEAN_VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure()
            //快速失败模式开启，当检测到有一项失败立即停止
            .failFast(true)
            .buildValidatorFactory().getValidator();

    /**
     * 方法参数对象校验器
     */
    private final ExecutableValidator METHOD_VALIDATOR = BEAN_VALIDATOR.forExecutables();

    /**
     * point配置
     */
    @Pointcut("execution(* com.zhanglijun.springbootdemo.web.controller..*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint point) {

        //  获得切入目标对象
        Object target = point.getThis();
        // 获得切入方法参数
        Object [] args = point.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature)point.getSignature()).getMethod();

        // 校验以基本数据类型 为方法参数的
        checkWithResult(validMethodParams(target, method, args));

        // 校验以java bean对象
        for (Object bean : args) {
            if (null != bean) {
                checkWithResult(validBeanParams(bean));

            }
        }
    }

    /**
     * 校验bean对象中的参数
     * @param bean
     * @param <T>
     * @return
     */
    private <T> Set<ConstraintViolation<T>> validBeanParams(T bean) {
        return BEAN_VALIDATOR.validate(bean);
    }

    /**
     * 校验方法中的参数
     * @param obj
     * @param method
     * @param params
     * @param <T>
     * @return
     */
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object [] params){
        return METHOD_VALIDATOR.validateParameters(obj, method, params);
    }

    /**
     * 校验参数校验结果
     * @param set
     */
    private void checkWithResult(Set<ConstraintViolation<Object>> set) {
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        Iterator<ConstraintViolation<Object>> methodIterator = set.iterator();
        if (methodIterator.hasNext()) {
            throw new IllegalArgumentException(methodIterator.next().getMessage());
        }
    }
}

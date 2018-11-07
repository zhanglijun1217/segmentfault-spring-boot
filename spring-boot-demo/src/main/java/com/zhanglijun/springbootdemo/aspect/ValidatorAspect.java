//package com.zhanglijun.springbootdemo.aspect;
//
//
//import com.zhanglijun.springbootdemo.domain.anno.EnableValidate;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.HashSet;
//import java.util.Set;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.executable.ExecutableValidator;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.hibernate.validator.HibernateValidator;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ClassUtils;
//
///**
// * @author 夸克
// * @create 2018/8/19 18:45
// */
//@Slf4j
//@Aspect
//@Configuration
//public class ValidatorAspect {
//
//    /**
//     * 获取校验的工厂的信息
//     */
//    private static final Validator validator = Validation.byProvider(HibernateValidator.class)
//            .configure()
//            //快速失败模式开启，当检测到有一项失败立即停止
//            .failFast(true)
//            .buildValidatorFactory().getValidator();
//
//    /**
//     * point配置
//     */
//    @Pointcut("execution(* com.zhanglijun.springbootdemo.web.controller..*.*(..))")
//    public void pointcut() {
//    }
//
//
//    /**
//     * @desction: 校验步骤 1.首先校验是否含有基本的Hibernate validator 注解，有异常抛出 2.校验方法参数中是否含有EgValidate注解，获取分组信息，进行Bean级别的校验，有异常抛出
//     * 3.查看当前的方法中（优先级高）(或者父类、父接口)是否含有EgValidate注解，没有获取当前类的中是否是否含有EgValidate注解，获取分组信息，针对每一个非基本类型Bean进行校验，有异常掏出
//     * @author: wangji
//     * @date: 2018/3/13 10:16
//     */
//    @Before("pointcut()")
//    public void before(JoinPoint point) {
//
//        //  获得切入目标对象
//        Object target = point.getThis();
//        // 获得切入方法参数
//        Object[] args = point.getArgs();
//        // 获得切入的方法
//        Method method = ((MethodSignature) point.getSignature()).getMethod();
//        Annotation[] classAnnotations = target.getClass().getAnnotations();
//        Annotation[] methodAnnotations = method.getAnnotations();
//        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//        //如果方法参数有基本的注解，就进行Hibernate validator 基本的参数校验
//        if (parameterAnnotations != null) {
//            validMethodParams(target, method, args);
//        }
//
//        // 判断参数中是否含有EgValidate注解，进行特殊分组，Bean级别的参数校验
//        int i = 0;
//
//        //排查掉已经在参数中校验过的参数不适用类或者方法上的校验参数在次进行校验
//        Set<Integer> idSet = new HashSet<>(3);
//        for (Object arg : args) {
//            if (arg != null) {
//                if (parameterAnnotations != null) {
//                    for (Annotation parameterAnnotation : parameterAnnotations[i]) {
//                        if (parameterAnnotation instanceof EnableValidate) {
//                            if (!ClassUtils.isPrimitiveOrWrapper(arg.getClass())) {
//                                validBeanParam(arg,
//                                        ((EnableValidate) parameterAnnotation).groups());
//                                idSet.add(i);
//                            }
//                        }
//                    }
//                }
//                i++;
//            }
//        }
//        // 如果没有异常继续校验当前的每一个非基本类型的参数
//        EnableValidate egValidate = null;
//        //方法上是否有校验参数
//        if (methodAnnotations != null) {
//            egValidate = AnnotationUtils.findAnnotation(method, EnableValidate.class);
//        }
//        // 类上是否含有
//        if (egValidate == null && classAnnotations != null) {
//            egValidate = AnnotationUtils.findAnnotation(target.getClass(), EnableValidate.class);
//        }
//        // 如果在类或者方法上加了验证注解 ，则对所有非基本类型的参数对象进行验证,不管参数对象有没有加注解，使用方法上的分组
//        if (egValidate != null && args != null && args.length > 0) {
//            i = 0;
//            for (Object arg : args) {
//                if (arg != null && !ClassUtils.isPrimitiveOrWrapper(arg.getClass()) && !idSet
//                        .contains(i)) {
//                    validBeanParam(arg, egValidate.groups());
//                }
//                i++;
//            }
//        }
//
//    }
//
//
//    /**
//     * @param obj 参数中的Bean类型参数
//     * @param groups 分组信息
//     * @desction: 进行参数中的Bean校验
//     * @author: wangji
//     * @date: 2018/3/13 10:10
//     */
//    private void validBeanParam(Object obj, Class<?>... groups) {
//        Set<ConstraintViolation<Object>> validResult = validator.validate(obj, groups);
//        throwConstraintViolationException(validResult);
//    }
//
//
//    /**
//     * @param obj 当前的实例
//     * @param method 实例的方法
//     * @param params 参数
//     * @desction: 对于Hibernate 基本校验Bean放在参数中的情况的校验 【例如 User getUserInfoById(@NotNull(message =
//     * "不能为空") Integer id);】
//     * @author: wangji
//     * @date: 2018/3/13 10:11
//     */
//    private void validMethodParams(Object obj, Method method, Object[] params) {
//        ExecutableValidator validatorParam = validator.forExecutables();
//        Set<ConstraintViolation<Object>> validResult = validatorParam
//                .validateParameters(obj, method, params);
//        throwConstraintViolationException(validResult);
//    }
//
//    /**
//     * @desction: 判断校验的结果是否存在异常
//     * @author: wangji
//     * @date: 2018/3/13 10:09
//     */
//    private void throwConstraintViolationException(Set<ConstraintViolation<Object>> validResult) {
//        if (!validResult.isEmpty()) {
//            throw new ConstraintViolationException(validResult.iterator().next().getMessage(),
//                    validResult);
//        }
//    }
//
//
//}

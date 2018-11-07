package com.zhanglijun.springbootdemo.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * web请求切面配置
 * @author 夸克
 * @date 2018/9/18 23:53
 */
@Aspect
@Configuration
@Order(1)
@Slf4j
public class WebRequestAspect {

    // TODO 可以加上sessionUser的获取

    @Pointcut("execution(* com.zhanglijun.springbootdemo.web.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        log.info("REQUEST_URL : {}, HTTP_METHOD : {}, ARGS : {}",
                request.getRequestURL().toString(),
                request.getMethod(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 打印返回内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // 容器启动时也去初始化了切面（因为是execution）这里去判断一下
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.info("REQUEST_URL : {}, RESPONSE : {} ", request.getRequestURL(), JSON.toJSONString(ret));
    }

    @AfterThrowing(throwing = "ex", pointcut = "webLog()")
    public void doAfterThrowing(Throwable ex) throws Throwable{
        // 直接抛给GlobalExceptionHandler
        throw ex;
    }
}

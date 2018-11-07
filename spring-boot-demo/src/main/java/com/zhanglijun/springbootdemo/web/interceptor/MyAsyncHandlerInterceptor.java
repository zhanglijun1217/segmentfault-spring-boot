//package com.zhanglijun.springbootdemo.web.interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
///**
// * 测试afterConcurrentHandlingStarted方法
// * @author 夸克
// * @date 2018/9/8 18:37
// */
//@Component
//@Slf4j
//public class MyAsyncHandlerInterceptor extends HandlerInterceptorAdapter {
//
//    /**
//     * 该方法是用来处理异步请求。当Controller中有异步请求方法的时候会触发该方法。
//     * 异步请求先支持preHandle、然后执行afterConcurrentHandlingStarted。
//     *
//     * @param request
//     * @param response
//     * @param handler
//     * @throws Exception
//     */
//    @Override
//    public void afterConcurrentHandlingStarted(HttpServletRequest request,
//            HttpServletResponse response, Object handler) throws Exception {
//        super.afterConcurrentHandlingStarted(request, response, handler);
//        log.info("MyAsyncHandlerInterceptor afterConcurrentHandlingStarted 方法执行");
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//            ModelAndView modelAndView) throws Exception {
//        super.postHandle(request, response, handler, modelAndView);
//        log.info("MyAsyncHandlerInterceptor postHandle 方法执行");
//    }
//}

//package com.zhanglijun.springbootdemo.web.interceptor;
//
//import com.zhanglijun.springbootdemo.domain.session.SessionUser;
//import com.zhanglijun.springbootdemo.util.session.RequestHelper;
//import java.util.Objects;
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
///**
// * SessionUser的拦截器
// *
// * @author 夸克
// * @date 2018/9/4 23:15
// */
//@Slf4j
//@Component
//public class SessionInterceptor implements HandlerInterceptor {
//
//    @Resource
//    private RequestHelper requestHelper;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        log.info("SessionInterceptor preHandle方法，在请求方法之前调用，Controller方法调用之前");
//        SessionUser sessionUser = new SessionUser();
//        // MOCK一个SessionUser对象，放入ThreadLocal中
//        sessionUser.setId(2L).setName("夸克");
//        requestHelper.setSessionUser(sessionUser);
//        // 只有这个方法返回true 请求才能继续下去
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
//            throws Exception {
//        log.info("SessionInterceptor postHandle方法，请求处理之后调用，但是在视图被渲染之前（Controller方法调用之后）");
//        // 这里可以去做sessionUser的清除 防止内存泄漏
//        requestHelper.clearSession();
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        log.info("SessionInterceptor afterCompletion方法，在整个请求结束之后调用，也就是在Dispatcher渲染了整个视图之后进行（主要进行资源清理工作）");
//        if (Objects.nonNull(requestHelper.getSessionUser())) {
//            requestHelper.clearSession();
//        }
//    }
//}

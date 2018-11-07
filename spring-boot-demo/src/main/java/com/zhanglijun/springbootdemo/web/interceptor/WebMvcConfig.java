//package com.zhanglijun.springbootdemo.web.interceptor;
//
//import com.zhanglijun.springbootdemo.web.MyDataTimeFormatAnnotationFormatFactory.MyDataTimeFormatAnnoFactory;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import javax.annotation.Resource;
//
///**
// * 配置WebMvc请求
// *
// * @author 夸克
// * @date 2018/9/5 00:02
// */
//@Component
//public class WebMvcConfig extends WebMvcConfigurerAdapter {
//
//    /**
//     * sessionInterceptor不需要拦截的请求
//     * 比如swagger的请求、比如一些静态资源的访问、比如错误统一处理的页面
//     */
//    private static final String[] EXCLUDE_SESSION_PATH= {};
//
////    @Resource
////    private SessionInterceptor sessionInterceptor;
//
//
//    /**
//     * 对所有的拦截器组成一个拦截器链
//     * addPathPatterns 用于添加拦截规则
//     * excludePathPatterns 用户排除拦截
//     *
//     * @param registry 拦截器注册对象
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 加入自定义拦截器到
//        registry.addInterceptor(sessionInterceptor).excludePathPatterns(EXCLUDE_SESSION_PATH);
//        super.addInterceptors(registry);
//    }
//
//
//    /**
//     * 向spring mvc中注册处理起止时间注解
//     * @param registry
//     */
//    @Override
//    public void addFormatters(FormatterRegistry registry) { MyDataTimeFormatAnnoFactory myDataTimeFormatAnnoFactory = new MyDataTimeFormatAnnoFactory();
//        registry.addFormatterForFieldAnnotation(myDataTimeFormatAnnoFactory);
//        super.addFormatters(registry);
//    }
//}

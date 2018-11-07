package com.zhanglijun.springbootdemo.web.controller;

import com.zhanglijun.springbootdemo.domain.session.SessionUser;
import com.zhanglijun.springbootdemo.service.HelloService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 夸克
 * @date 2018/9/4 23:11
 */
@RestController
public class ThreadLocalController {


    @Resource
    private HelloService helloService;

    /**
     * 在拦截器中用ThreadLocal
     *
     * @return SessionUser
     */
    @GetMapping(value = "/threadLocal/getSessionUser")
    public SessionUser getSessionUser() {
        return helloService.getThreadSessionUser();
    }
}

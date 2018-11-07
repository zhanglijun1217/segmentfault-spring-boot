package com.zhanglijun.springbootdemo.service.impl;

import com.zhanglijun.springbootdemo.domain.session.SessionUser;
import com.zhanglijun.springbootdemo.service.HelloService;
import com.zhanglijun.springbootdemo.util.session.RequestHelper;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 夸克
 * @create 2018/8/19 21:34
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Resource
    private RequestHelper requestHelper;

    @Override
    @Async
    public void sayHello() {
      log.info("hello");
    }

    @Override
    public void testTransaction() {

    }

    @Override
    public SessionUser getThreadSessionUser() {
        return requestHelper.getSessionUser();
    }
}

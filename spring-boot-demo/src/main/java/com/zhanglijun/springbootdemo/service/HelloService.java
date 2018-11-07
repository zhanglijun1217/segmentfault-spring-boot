package com.zhanglijun.springbootdemo.service;

import com.zhanglijun.springbootdemo.domain.session.SessionUser;

/**
 * @author 夸克
 * @create 2018/8/19 21:33
 */
public interface HelloService {

    void sayHello();

    void testTransaction();

    /**
     * 测试ThreadLocal
     *
     * @return 返回SessionUser
     */
    SessionUser getThreadSessionUser();
}

package com.zhanglijun.springbootdemo.util.session;

import com.zhanglijun.springbootdemo.domain.session.SessionUser;
import org.springframework.stereotype.Component;

/**
 * @author 夸克
 * @date 2018/9/4 23:02
 */
@Component
public class RequestHelper {

    private ThreadLocal<SessionUser> threadLocal = new ThreadLocal<>();

    public void setSessionUser(SessionUser sessionUser) {
        threadLocal.set(sessionUser);
    }

    public SessionUser getSessionUser(){
        return threadLocal.get();
    }

    public void clearSession() {
        threadLocal.remove();
    }
}

package com.zhanglijun.springbootdemo.domain.constant;

/**
 * 通用Error接口
 * @author 夸克
 * @create 2018/8/17 18:17
 */
public interface CommonError {

    /**
     * 返回错误信息
     * @return
     */
    String getMessage();

    /**
     * 返回错误码
     * @return
     */
    int getCode();
}

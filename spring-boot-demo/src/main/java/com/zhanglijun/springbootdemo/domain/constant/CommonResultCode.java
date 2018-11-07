package com.zhanglijun.springbootdemo.domain.constant;

/**
 * @author 夸克
 * @date 23/10/2018 19:13
 */
public enum CommonResultCode {

    SUCCESS(200, "success");
    public final int code;
    public final String message;

    CommonResultCode(int code, String messge) {
        this.code = code;
        this.message = messge;
    }
}

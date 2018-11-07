package com.zhanglijun.springbootdemo.exception;

import com.zhanglijun.springbootdemo.domain.constant.BizError;

/**
 * @author 夸克
 * @date 23/10/2018 20:13
 */
public class BizException extends RuntimeException {
    private int code;

    public BizException(BizError bizError) {

    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(int code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }

    public BizException(int code, Throwable e) {
        super(e);
        this.code = code;
    }

    public BizException(String message) {
        super(message);
        this.code = BizError.COMMON_BIZ_ERROR.getCode();
    }

    public BizException(int code, String message, Throwable e, boolean enableSupression, boolean writableStackTrace) {
        super(message, e, enableSupression, writableStackTrace);
        this.code = code;
    }

}

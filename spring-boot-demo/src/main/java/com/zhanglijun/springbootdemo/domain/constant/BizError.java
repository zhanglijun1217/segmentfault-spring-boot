package com.zhanglijun.springbootdemo.domain.constant;

/**
 * @author 夸克
 * @create 2018/8/17 18:19
 */
public enum BizError implements CommonError {
    /**
     * 普通业务异常
     */
    COMMON_BIZ_ERROR(500, "普通业务异常"),
    PARAM_ERROR(400, "参数异常");

    private int code;

    private String message;
    BizError(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}

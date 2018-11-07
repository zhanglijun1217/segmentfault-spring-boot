package com.zhanglijun.springbootdemo.domain.base;

import com.zhanglijun.springbootdemo.domain.constant.CommonResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 夸克
 * @date 23/10/2018 19:08
 */
@Data
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 1949910043360896391L;

    private boolean success;
    private int code;
    private String message;
    private String requestId;

    public BaseResult() {
        this.code = CommonResultCode.SUCCESS.code;
        this.success = true;
        this.message = CommonResultCode.SUCCESS.message;
    }

    public BaseResult(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

}

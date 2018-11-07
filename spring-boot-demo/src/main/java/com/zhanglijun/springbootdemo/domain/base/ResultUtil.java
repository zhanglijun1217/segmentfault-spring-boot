package com.zhanglijun.springbootdemo.domain.base;

/**
 * 返回值工具
 * @author 夸克
 * @date 23/10/2018 19:22
 */
public class ResultUtil {

    /**
     * build 异常场景
     * @param code
     * @param message
     * @return
     */
    public static BaseResult buildExResponse(Integer code, String message) {
        return new BaseResult(code, message, false);
    }
}

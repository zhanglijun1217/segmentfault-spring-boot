package com.zhanglijun.springbootdemo.domain.base;

import lombok.Data;

/**
 * @author 夸克
 * @create 2018/8/17 17:58
 */
@Data
public class Response<T> extends BaseResult {

    private T data;
}

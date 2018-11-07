package com.zhanglijun.springbootdemo.domain.session;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 夸克
 * @date 2018/9/4 23:03
 */
@Data
@Accessors(chain = true)
public class SessionUser {

    private Long id;

    private String name;

}

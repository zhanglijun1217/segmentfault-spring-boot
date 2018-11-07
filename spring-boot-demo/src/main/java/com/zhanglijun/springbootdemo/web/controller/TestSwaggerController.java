package com.zhanglijun.springbootdemo.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 夸克
 * @date 2018/9/24 15:29
 */
@RestController
@Api(value = "测试swagger", description = "测试swagger api")
public class TestSwaggerController {

    @ApiOperation(value = "返回url中的参数", notes = "返回url中的参数")
    @ApiImplicitParam(name = "id", value = "id值", paramType = "path", required = true, dataType = "Integer")
    @GetMapping(path = "/getUrlParam/{id}")
    public Integer getUrlParam(@PathVariable(value = "id") Integer id) {
        return id;
    }

}

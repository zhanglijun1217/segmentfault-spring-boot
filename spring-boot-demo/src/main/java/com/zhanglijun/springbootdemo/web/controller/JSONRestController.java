package com.zhanglijun.springbootdemo.web.controller;

import com.zhanglijun.springbootdemo.User;
import com.zhanglijun.springbootdemo.domain.session.SessionUser;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author 夸克
 * @date 2018/9/16 01:58
 */
@RestController
public class JSONRestController {

    @Bean
    public User currentUser() {
        User user = new User();
        user.setAge(22);
        user.setName("李明");
        return user;
    }

    @Resource
    private User user;


    @GetMapping(value = "/jsonResult", produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionUser getSessionUser() {
        return new SessionUser().setId(1L).setName("zlj");
    }


    @GetMapping(path = "/json/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public User user() {
        // 使用hateoas 进行资源映射的链接 是自己内部的 暴露这个描述
        user.add(linkTo(methodOn(JSONRestController.class).setUserName(user.getName())).withSelfRel());
        return user;
    }

    @GetMapping(value = "/json/user/set/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public User setUserName(@RequestParam(value = "userName") String userName) {
        user.setName(userName);
        linkTo(methodOn(JSONRestController.class).setUserName("1111")).withSelfRel();
        return user;
    }

}

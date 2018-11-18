package com.segmentfault.springbootlesson7.demo;

import com.segmentfault.springbootlesson7.entity.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 夸克
 * @date 2018/11/18 16:01
 */
@RestController
public class MyBatisStarterController {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;


    @GetMapping(value = "/user")
    public User selectOneUser(@RequestParam(value = "id", defaultValue = "1") Long id) {
        User user = sqlSessionTemplate.selectOne("com.segmentfault.springbootlesson7.mapper.UserMapper.selectOneUser", id);

        return user;
    }
}

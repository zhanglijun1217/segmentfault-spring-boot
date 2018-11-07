package com.zhanglijun.springbootdemo.web.controller;

import com.zhanglijun.springbootdemo.domain.anno.EnableValidate;
import com.zhanglijun.springbootdemo.domain.validate.ValidateBO;
import com.zhanglijun.springbootdemo.domain.validate.ValidateByGroupBO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 夸克
 * @create 2018/8/17 10:44
 */
@RestController
@EnableValidate
public class ValidateController {

    /**
     * validate一些校验注解使用
     * 在要校验的参数中需要加入@Validated注解，表示需要spring对其进行validate校验，校验之后的结果会放在后边的bindingResult中
     * 如果多个参数需要被校验，则多个参数都需要添加@Validated注解并且有对应的BindingResult
     * @param validateBO
     * @return
     */
    @GetMapping(value = "/validate")
    public String validate(ValidateBO validateBO) {
        return "success";
    }

    @PostMapping(value = "/validateByPost")
    public String validateByPost(@RequestBody ValidateBO validateBO) {
        return "success";
    }


    /**
     * 喝酒这个去校验了年龄值，因为只有adult这个组才去校验年龄
     * @param validateByGroupBO
     * @param bindingResult
     * @return
     */
    @GetMapping(value = "/drink")
    public String drink(@Validated({ValidateByGroupBO.Adult.class}) ValidateByGroupBO validateByGroupBO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 处理错误
            return "false";
        }
        return "success";
    }

    /**
     * 生活不需要去校验adult的分组 就不去校验对应的age的最小值
     * @param validateByGroupBO
     * @param bindingResult
     * @return
     */
    @GetMapping(value = "live")
    public String live(ValidateByGroupBO validateByGroupBO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 错误处理
            return "false";
        }
        return "success";
    }


    @GetMapping(value = "/aopValidate")
    public String aopValidate(ValidateBO validateBO) {
        return "success";
    }

}

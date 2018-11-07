package com.zhanglijun.springbootdemo.web.controller;

import com.zhanglijun.springbootdemo.User;
import com.zhanglijun.springbootdemo.domain.validate.ValidateBO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author 夸克
 * @create 2018/8/19 21:36
 */
@RestController
@Slf4j
public class HelloController {

    @GetMapping(value = "/hello")
    public Callable<String> sayHello() {
      return () -> "controller";
    }



    // =========================== rest HTML 传输 =========================
    @RequestMapping(path = {"/html/demo", "html/demo2"}) // requestMapping中的路由路径 path和value都是可以的
//    @PostMapping(value = "/html/demo3") // 一个方法只能有一个mapping注解进行映射
    //
    public String htmlDemo() {
        return "<html><body>hello world</body></html>";
    }

    @GetMapping(value = "/html/demo/{message}")
    public String message(@PathVariable String message) {
        return "<html><body>hello world, "+ message + "</body></html>";
    }

    @GetMapping(value = "/html/demo/param")
    public String param(@RequestParam(value = "p", required = false) String param,
                        // 使用servl et规范也可以去在访问时加上param2=value 那么会注入到HttpServletRequest中
                        HttpServletRequest request) {
        String param2 = request.getParameter("param2");
        return "<html><body>hello world, " + param + "parameter2 value :" + param2 +"</body></html>";
    }

    @GetMapping(value = "/html/demo/requestHeader")
    public String requestHeader(@RequestHeader(value = "Accept") String accept) {
        return "<html><body>hello world," + accept + "</body></html>";
    }


    // =========================== 测试自定义注解 =======================
    @PostMapping(path = "/testMyDateFormat")
    public Date testMyDateTimeFormat(@RequestBody User user) {
        return user.getATime();
    }


    // ========================== 测试切面 =============================

    /**
     * 校验validate切面 表单提交
     * @param validateBO
     * @return
     */
    @PostMapping(path = "/validate/bean", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testValidateBean(@RequestBody ValidateBO validateBO) {
        return "校验表单参数切面成功";
    }

    /**
     * 校验get直接解析实体参数
     * @param validateBO
     * @return
     */
    @GetMapping(path = "/validate/urlParam", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testValidateUrlParam(ValidateBO validateBO) {
        return "校验get直接解析实体参数成功";
    }

    /**
     * 校验url解析参数成功
     * @param a
     * @param s
     * @return
     */
    @GetMapping(path = "validate/param", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testValidateParam(@Max(value = 1,message = "a不能超过1")  @RequestParam(value = "a") Integer a,
                                    @NotEmpty(message = "s不能为空") @RequestParam(value = "s") String  s) {
        return "success";
    }


    //========================= 测试web log切面 =====================

    @GetMapping(path = "/testAspect", produces = MediaType.APPLICATION_JSON_VALUE)
    public void testAspect(HttpServletResponse response) {
        log.info("测试web log 切面 fastJson序列化response对象会报错");
        throw new IllegalArgumentException("测试@AfterThrowing");
    }


}

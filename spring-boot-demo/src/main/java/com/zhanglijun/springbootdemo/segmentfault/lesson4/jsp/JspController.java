package com.zhanglijun.springbootdemo.segmentfault.lesson4.jsp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * jsp访问Controller
 *
 * @author 夸克
 * @date 22/10/2018 00:26
 */
@Controller
public class JspController {


    @RequestMapping(value = "/index")
    public String index(Model model) {
        model.addAttribute("message", "zlj");

        return "index";
    }
}


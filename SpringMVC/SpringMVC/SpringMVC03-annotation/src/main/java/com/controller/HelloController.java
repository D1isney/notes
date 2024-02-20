package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/HelloController")
//  写这个 这个类就不会被视图解析器解析，返回的就是String
//@RestController
public class HelloController {

    //  真实访问地址：项目名/HelloController/hello
    @RequestMapping("/hello")
    public String hello(Model model) {
        //  封装数据
        //  向模型中添加msg与值，可以在jsp页面中取出并渲染
        model.addAttribute("msg","Hello,SpringMVCAnnotation！");
        //  web-inf/jsp/hello.jsp
        return "hello"; //  会被视图解析器处理；
    }

}

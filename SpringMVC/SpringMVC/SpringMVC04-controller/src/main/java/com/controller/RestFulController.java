package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestFulController {

    /**
     * http://localhost:8080/add1?a=1&b=2
     * 原来的方式
     */
    @RequestMapping("/add1")
    public String test1(int a, int b, Model model) {
        model.addAttribute("msg", "结果为：" + (a + b));
        return "test";
    }

    /**
     * http://localhost:8080/add2/1/2
     * RestFul风格
     */
    @RequestMapping("/add2/{a}/{b}")
    public String test2(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "结果为：" + (a + b));
        return "test";
    }

    /**
     * http://localhost:8080/add3/1/2
     * RestFul风格
     * <p>
     * 类型 状态报告
     * <p>
     * 消息 Request method 'GET' not supported
     * <p>
     * 描述 请求行中接收的方法由源服务器知道，但目标资源不支持
     */
    @RequestMapping(value = "/add3/{a}/{b}", method = RequestMethod.DELETE)
    /**
     * 等价于
     */
    //    @DeleteMapping(name = "/add3/{a}/{b}",)
    public String test3(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "DeleteMapping：" + (a + b));
        return "test";
    }



    //  上面是delete方式提交的，这里是Get方式提交的，同一个url，凡是执行了方式不同
    @GetMapping(value = "/add3/{a}/{b}")
    public String test4(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "GetMapping：" + (a + b));
        return "test";
    }

}

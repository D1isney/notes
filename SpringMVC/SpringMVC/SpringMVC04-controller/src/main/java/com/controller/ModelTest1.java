package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ModelTest1 {
    @RequestMapping("/m1/t1")
    public String test1(HttpServletRequest request, HttpServletResponse response) {
        //  转发
        return "/index.jsp";
    }

    @RequestMapping("/m1/t2")
    public String test2(HttpServletRequest request, HttpServletResponse response) {
        //  转发二
        return "forward:/index.jsp";
    }

    @RequestMapping("/m1/t3")
    public String test3(HttpServletRequest request, HttpServletResponse response) {
        //  重定向
        return "redirect:/index.jsp";
    }

    @RequestMapping("/m1/t4")
    public String test4(HttpServletRequest request, HttpServletResponse response) {
        //  转发
        return "test";
    }
    @RequestMapping("/m1/t5")
    public String test5(HttpServletRequest request, HttpServletResponse response) {
        //  重定向
        return "redirect:/test";
    }

}

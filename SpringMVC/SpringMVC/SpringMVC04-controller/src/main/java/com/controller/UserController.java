package com.controller;

import com.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/t1")
    public String test1(String name, Model model){
        //1.接受前端参数
        System.out.println(name);

        //2.将返回的结果传递给前端
        model.addAttribute("msg",name);

        //3.跳转视图
        return "test";
    }

    @GetMapping("/t2")
    //  指定传进来的值的名字，userName匹配到name上
    public String test2(@RequestParam("userName") String name, Model model){
        //1.接受前端参数
        System.out.println(name);

        //2.将返回的结果传递给前端
        model.addAttribute("msg",name);

        //3.跳转视图
        return "test";
    }


    // 前端接受的是一个对象:  id name age

    /**
     * 1、接收前端用户传递的参数，判断参数的名字，假设名字直接在方法上，可以直接使用
     * 2、假设传递的是一个对象User，匹配User对象中的字段名；如果名字一致则OK，否则，匹配不到
     */
    @GetMapping("/t3")
    public String test3(User user,Model model){
        model.addAttribute("msg",user);
        return "test";
    }


    /**
     * LinkedHashMap
     * Model：只有寥寥几个方法只适合存储数据，简化了新手对于Model对象操作和理解；
     * ModelMap：继承了LinkedHashMap，所以它拥有LinkedHashMap的方法和特性；
     * ModelAndView：可以在存储数据的同时，可以进行设置返回的逻辑视图，进行控制展示层的跳转；
     */
    @GetMapping("/t4")
    public String test4(User user, ModelMap modelMap){
        modelMap.addAttribute("msg",user);
        return "test";
    }

    @GetMapping("/t5")
    public ModelAndView test5(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg","test5");
        mv.setViewName("test");
        return mv;
    }

    @GetMapping("/t6")
    public String test6(User user, Model model){
        model.addAttribute("msg",user);
        return "test";
    }


}

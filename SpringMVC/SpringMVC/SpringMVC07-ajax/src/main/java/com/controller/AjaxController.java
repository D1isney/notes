package com.controller;

import com.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {
    @RequestMapping("/t1")
    public String test() {
        return "test";
    }

    @RequestMapping("/userName")
    public void test1(String name, HttpServletResponse response) throws IOException {
        System.out.println("userName:param=> " + name);
        if ("Disney".equals(name)) {
            response.getWriter().println("true");
        } else {
            response.getWriter().println("false");
        }
    }

    @RequestMapping("/test2")
    public List<User> test2() {
        ArrayList<User> list = new ArrayList<User>();
        //  添加数据
        list.add(new User("Java", 22, "男"));
        list.add(new User("MySQL", 21, "女"));
        list.add(new User("Web", 23, "男"));

        return list;
    }


    @RequestMapping("/a3")
    public String a3(String name, String pwd) {
        String msg = "";
        if (name != null) {
//            这些数据应该在数据库查询
            if ("admin".equals(name)) {
                msg = "OK";
            }else{
                msg = "用户名有误";
            }
        }
        if (pwd != null) {
//            这些数据应该在数据库查询
            if ("123456".equals(pwd)) {
                msg = "OK";
            }else{
                msg = "密码有误";
            }
        }
        return msg;
    }
}

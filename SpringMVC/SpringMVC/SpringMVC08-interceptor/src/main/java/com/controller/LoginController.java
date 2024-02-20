package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(HttpSession session, String username, String password, Model model){
        //  把用户的信息存在session 中
        session.setAttribute("userLoginInfo",username);
        model.addAttribute("username",username);
        return "main";
    }
    @RequestMapping("/goOut")
    public String goOut(HttpSession session){
        session.removeAttribute("userLoginInfo");
        return "login";
    }

    @RequestMapping("/goLogin")
    public String goLogin(){
        return "login";
    }
    @RequestMapping("/main")
    public String main(){
        return "main";
    }

}

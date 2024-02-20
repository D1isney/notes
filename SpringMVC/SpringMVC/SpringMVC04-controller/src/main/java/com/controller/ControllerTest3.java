package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/t")
public class ControllerTest3 {

    @RequestMapping("/t3")
    public String test3(Model model){
        model.addAttribute("msg","ControllerTest3");
        return "test";
    }
}

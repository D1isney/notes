package com.wms.controller;
 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 */
@RestController
public class HelloController {
 
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
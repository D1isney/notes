package com.wms.controller;

import com.wms.exception.EException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionTest {

    @GetMapping("/test")
    public void test() {
        throw new EException("测试异常");
    }
}

package com.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimitController {

    @GetMapping(value = "/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试";
    }

}

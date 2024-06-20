package com.cloud.controller;

import com.cloud.service.FlowLimitService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "-----testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "-----testB";
    }

    @Resource
    private FlowLimitService flowLimitService;

    @GetMapping("/testC")
    public String testC() {
        flowLimitService.common();
        return "-----testC";
    }

    @GetMapping("/testD")
    public String testD() {
        flowLimitService.common();
        return "-----testD";
    }

    @GetMapping(value = "/testE")
    public String testE() {
        System.out.println(System.currentTimeMillis() + "              testE,流控效果，等待排队");
        return "-----testE";
    }

    @GetMapping("/testF")
    public String testF() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("测试：新增熔断规则，慢调用比例");
        return "-----testF";
    }

    @GetMapping("/testG")
    public String testG() {
        System.out.println("测试：新增熔断规则，异常比例");
        int age = 10 / 0;
        return "-----testG";
    }

    @GetMapping("/testH")
    public String testH() {
        System.out.println("测试：新增熔断规则，异常数");
        int age = 10 / 0;
        return "-----testH";
    }

}

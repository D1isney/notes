package com.cloud.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class PayCircuitController {

//    @GetMapping(value = "/pay/circuit/{id}")
//    public String myCircuit(@PathVariable("id") Integer id) {
//        if (id == -4) throw new RuntimeException("----circuit id 不能负数");
//        if (id == 9999) {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return "Hello Circuit! inputID：" + id + "\t" + IdUtil.simpleUUID();
//    }

    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id) {
        if (id == -4) throw new RuntimeException("----bulkhead id 不能负数");
        if (id == 9999) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "Hello Bulkhead! inputID：" + id + "\t" + IdUtil.simpleUUID();
    }

    //  限流
    @GetMapping(value = "/pay/rateLimit/{id}")
    public String myRateLimit(@PathVariable("id") Integer id) {
        return "Hello myRateLimit! inputID：" + id + "\t" + IdUtil.simpleUUID();
    }

}

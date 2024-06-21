package com.cloud.controller;

import com.cloud.apis.PayFeignSentinelApi;
import com.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderNaCosController {

    @Resource
    private RestTemplate restTemplate;


    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @GetMapping("/consumer/pay/nacos/{id}")
    public String consumer(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(serverURL + "/pay/nacos/" + id, String.class) + "\t" + "  我是OrderNacosController83调用者";
    }

    // ==========
    @Resource
    private PayFeignSentinelApi payFeignSentinelApi;

    @GetMapping(value = "/consumer/pay/nacos/get/{orderNo}")
    public ResultData<?> getPayByOrderNo(@PathVariable("orderNo") String orderNo) {
        return payFeignSentinelApi.getPayByOrderNo(orderNo);
    }
}

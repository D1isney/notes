package com.cloud.controller;

import com.cloud.entities.PayDTO;
import com.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    //    public static final String PaymentSrv_URL = "http://localhost:8001";
//    注册中心叫什么，这里就叫什么
    public static final String PaymentSrv_URL = "http://cloud-payment-service8001";

    @Resource
    private RestTemplate restTemplate;

    //增加
    @GetMapping(value = "/consumer/pay/add")
    public ResultData<?> addOrder(PayDTO payDTO) {
        return restTemplate.postForObject(PaymentSrv_URL + "/pay/add", payDTO, ResultData.class);
    }

    //删除
    @DeleteMapping(value = "/consumer/pay/delete/{id}")
    public void deleteOrder(@PathVariable("id") Integer id) {
        restTemplate.delete(PaymentSrv_URL + "/pay/delete/" + id, ResultData.class, id);
    }

    //修改
    @PutMapping(value = "/consumer/pay/update")
    public void updateOrder(@RequestBody PayDTO payDTO) {
        restTemplate.put(PaymentSrv_URL + "/pay/update", ResultData.class, payDTO);
    }

    //查询
    @GetMapping(value = "/consumer/pay/get/{id}")
    public ResultData<?> getPayInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/" + id, ResultData.class, id);
    }


}

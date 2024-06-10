package com.cloud.controller;

import com.cloud.entities.PayDTO;
import com.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {
    //    public static final String PaymentSrv_URL = "http://localhost:8001";
//    注册中心叫什么，这里就叫什么
    public static final String PaymentSrv_URL = "http://cloud-payment-service";

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
    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul() {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
    }

    @GetMapping("/consumer/discovery")
    public String discovery() {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }
        System.out.println("================");
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
        }
        return instances.get(0).getServiceId() + ":" + instances.get(0).getPort();
    }

}

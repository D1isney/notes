package com.cloud.controller;

import cn.hutool.core.date.DateUtil;
import com.cloud.apis.PayFeignApi;
import com.cloud.entities.PayDTO;
import com.cloud.resp.ResultData;
import com.cloud.resp.ReturnCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Resource
    private PayFeignApi payFeignApi;

    @PostMapping(value = "/feign/pay/add")
    public ResultData<?> addOrder(@RequestBody PayDTO payDTO) {
        System.out.println("第一步：模拟本地addOrder新增订单成功，第二部：在开启addPay支付微服务远程调用");
        return payFeignApi.addPay(payDTO);
    }

    @GetMapping(value = "/feign/pay/get/{id}")
    public ResultData<?> getPay(@PathVariable("id") Integer id) {
        System.out.println("----支付服务远程调用，按照ID查询订单支付流水信息");
        ResultData<?> data = null;
        try {
            System.out.println("----调用开始：" + DateUtil.now());
            data = payFeignApi.getPayInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----调用结束：" + DateUtil.now());
            return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
        }
        return data;
    }

    @GetMapping(value = "/feign/pay/myLB")
    public String myLB() {
        return payFeignApi.myLB();
    }
}

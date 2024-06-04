package com.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cloud.entities.Pay;
import com.cloud.entities.PayDTO;
import com.cloud.service.PayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("/pay/add")
    public String addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return "成功插入记录，返回值：" + i;
    }

    @DeleteMapping(value = "/pay/delete/{id}")
    public Integer deletePay(@PathVariable("id") Integer id) {
        return payService.delete(id);
    }


    @PutMapping(value = "/pay/update")
    public String updatePay(@RequestBody PayDTO patDTO) {
        Pay pay = new Pay();

        BeanUtil.copyProperties(patDTO, pay);
        int i = payService.update(pay);
        return "成功修改记录，返回值：" + i;
    }

    @GetMapping("/pay/get/{id}")
    public Pay getById(@PathVariable("id") Integer id) {
        return payService.getById(id);
    }

    @GetMapping("/pau/getAll")
    public List<Pay> getAllPay(){
        return payService.getAll();
    }

}

package com.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cloud.entities.Pay;
import com.cloud.entities.PayDTO;
import com.cloud.resp.ResultData;
import com.cloud.resp.ReturnCodeEnum;
import com.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "支付微服务模块", description = "支付CRUD")
public class PayController {

    @Resource
    private PayService payService;
    @Value("${server.port}")
    private String port;

    @PostMapping("/pay/add")
    @Operation(summary = "新增", description = "新增支付流水方法，json串做参数")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return ResultData.success("成功插入记录，返回值：" + i);
    }

    @DeleteMapping(value = "/pay/delete/{id}")
    @Operation(summary = "删除", description = "删除支付方法")
    public ResultData<Integer> deletePay(@PathVariable("id") Integer id) {
        return ResultData.success(payService.delete(id));
    }

    @PutMapping(value = "/pay/update")
    @Operation(summary = "修改", description = "修改支付流水方法")
    public ResultData<String> updatePay(@RequestBody PayDTO patDTO) {
        Pay pay = new Pay();
        BeanUtil.copyProperties(patDTO, pay);
        int i = payService.update(pay);
        return ResultData.success("成功修改记录，返回值：" + i);
    }

    @GetMapping("/pay/get/{id}")
    @Operation(summary = "按照ID查流水", description = "查询支付流水方法")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        if (id == -4) throw new RuntimeException("传进来不能是负数");
        return ResultData.success(payService.getById(id));
    }

    //  手写异常捕捉
    @GetMapping(value = "/pay/error")
    public ResultData<Integer> getPayError() {
        Integer integer = 200;
        try {
            System.out.println("come in payError test");
            int age = 10 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
        }
        return ResultData.success(integer);
    }

    @GetMapping("/pau/getAll")
    public List<Pay> getAllPay() {
        return payService.getAll();
    }

    @GetMapping(value = "/pay/get/info")
    public String getInfoByConsul(@Value("${disney.info}") String info) {
        return "Info：" + info + "\t" + port;
    }

}

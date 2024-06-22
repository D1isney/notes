package com.cloud.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.entities.PayDTO;
import com.cloud.resp.ResultData;
import com.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Slf4j
public class PayAlibabaController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/pay/nacos/{id}")
    public String getPayInfo(@PathVariable("id") Integer id) {
        return "nacos registry,ServerPort：" + serverPort + "\t" + "Id：" + id;
    }

    //  OpenFeign + Sentinel 进行服务降级和流量监控的整合处理case
    @GetMapping("/pay/nacos/get/{orderNo}")
    @SentinelResource(value = "getPayByOrderNo", blockHandler = "handlerBlockHandler")
    public ResultData<?> getPayByOrderNo(@PathVariable("orderNo") String orderNo) {
        PayDTO payDTO = new PayDTO();
        payDTO.setId(1024);
        payDTO.setOrderNo(orderNo);
        payDTO.setAmount(BigDecimal.valueOf(9.9));
        payDTO.setPayNo("pat:" + IdUtil.fastUUID());

        return ResultData.success("查询返回值：" + payDTO);
    }

    public ResultData<?> handlerBlockHandler(@PathVariable("orderNo") String orderNo, BlockException e) {
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(), "getPayByOrderNo服务不可用，" +
                "触发Sentinel流控配置规则" + "\t" + "!"
        );
    }
}

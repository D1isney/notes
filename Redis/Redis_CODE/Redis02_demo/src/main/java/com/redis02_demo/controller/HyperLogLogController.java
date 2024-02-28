package com.redis02_demo.controller;

import com.redis02_demo.service.HyperLogLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "淘宝亿级UV的Redis统计方案")
@RestController
@Slf4j
public class HyperLogLogController {

    @Resource
    HyperLogLogService hyperLogLogService;


    @ApiOperation("获得IP去重后的uv统计访问量")
    @RequestMapping(value = "/uv",method = RequestMethod.GET)
    public long uv(){
        return hyperLogLogService.uv();
    }

}

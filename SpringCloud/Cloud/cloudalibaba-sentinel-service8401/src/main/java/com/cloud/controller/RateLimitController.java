package com.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimitController {

    @GetMapping(value = "/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试";
    }

    @GetMapping(value = "/rateLimit/byResource")
    @SentinelResource(value = "byResourceSentinelResource", blockHandler = "handlerBlockHandler")
    public String byResource() {
        return "按照资源名称SentinelResource限流测试";
    }

    public String handlerBlockHandler(BlockException blockException) {
        return "服务不可用触发了@SentinelResource";
    }

    @GetMapping("/rateLimit/doAction/{p1}")
    @SentinelResource(value = "doActionSentinelResource", blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0) {
            throw new RuntimeException("p1 is 0");
        }
        return "doAction";
    }

    public String doActionFallback(@PathVariable("p1") Integer p1, BlockException e) {
        log.error("sentinel配置自定义限流了：" + e);
        return "程序逻辑异常了" + "\t" + e.getMessage();
    }

    public String doActionFallback(@PathVariable("p1") Integer p1, Throwable e) {
        log.error("逻辑程序异常了：" + e);
        return "程序逻辑异常了" + "\t" + e.getMessage();
    }


    @GetMapping(value = "/test/HotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2
    ) {
        return "------testHotKey------";
    }

    public String dealHandler_testHotKey(String p1, String p2, BlockException e) {
        return "----dealHandler_testHotKey";
    }



}

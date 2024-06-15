package com.cloud.controller;

import com.cloud.apis.PayFeignApi;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class OrderCircuitController {

    @Resource
    private PayFeignApi payFeignApi;

    //  服务熔断
    @CircuitBreaker(name = "cloud-payment-service", fallbackMethod = "myCircuitFallback")
    @GetMapping(value = "/feign/pay/circuit/{id}")
    public String myCircuitBreaker(@PathVariable("id") Integer id) {
        return payFeignApi.myCircuit(id);
    }

    public String myCircuitFallback(Integer id, Throwable t) {
        //  这里是容错处理逻辑，返回备用结果
        return "myCircuitFallback，系统繁忙，请稍后重试";
    }


    //  舱壁（服务）隔离
    //  semaphore
//    @GetMapping(value = "/feign/pay/bulkhead/{id}")
//    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkheadFallback", type = Bulkhead.Type.SEMAPHORE)
//    public String myBulkhead(@PathVariable("id") Integer id) {
//        return payFeignApi.myBulkhead(id);
//    }

    //  threadPool
    @GetMapping(value = "/feign/pay/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkheadPoolFallback", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> myBulkheadPool(@PathVariable("id") Integer id) {
        System.out.println(Thread.currentThread().getName() + "\t" + "---开始进入");
        //
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "---准备离开");

        return CompletableFuture.supplyAsync(() -> payFeignApi.myBulkhead(id) + "\t" + "myBulkheadPool");
    }

    public CompletableFuture<String> myBulkheadPoolFallback(Integer id, Throwable t) {
        return CompletableFuture.supplyAsync(() -> "myBulkheadPoolFallback,系统繁忙，请稍后重试");
    }

    public String myBulkheadFallback(Integer id, Throwable t) {
        //  这里是容错处理逻辑，返回备用结果
        return "myBulkheadFallback，隔板超出最大数量限制，系统繁忙，请稍后重试";
    }


    @GetMapping(value = "/feign/pay/rateLimit/{id}")
    @RateLimiter(name = "cloud-payment-service", fallbackMethod = "myRateLimitFallback")
    public String myBulkheadRateLimit(@PathVariable("id") Integer id) {
        return payFeignApi.myRateLimit(id);
    }

    public String myRateLimitFallback(Integer id, Throwable t) {
        return "你被限流了，禁止访问";
    }


}

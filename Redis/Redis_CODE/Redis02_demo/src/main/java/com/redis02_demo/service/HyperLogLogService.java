package com.redis02_demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HyperLogLogService {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 模拟后台有用户点击淘宝首页，每个用户来自不同的IP地址
     */
    @PostConstruct
    public void initIP() {

        new Thread(() -> {
            String IP = null;
            for (int i = 0; i < 200; i++) {
                Random random = new Random();
                IP =
                        random.nextInt(256) + "."
                                + random.nextInt(256) + "."
                                + random.nextInt(256) + "."
                                + random.nextInt(256);

                Long hll = redisTemplate.opsForHyperLogLog().add("hll", IP);
                System.out.println("IP"+IP+",该IP地址访问首页的次数"+hll);
//                log.info("ip={},该IP地址访问首页的次数{}", IP, hll);
                //  暂停三秒钟
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();
    }


    public Long uv() {
        return redisTemplate.opsForHyperLogLog().size("hll");
    }

}

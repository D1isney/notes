package com.redis04_lock.service;

import com.redis04_lock.utils.DistributedLockFactory;
import com.redis04_lock.utils.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Service
@Slf4j
public class InventoryService02 {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private DistributedLockFactory distributedLockFactory;
    public String sale() {
        String resMessgae = "";

        Lock lock = distributedLockFactory.getDistributedLock("REDIS");
        lock.lock();
        try {
            // 1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory01");
            // 2 判断库存书否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存，每次减少一个库存
            if (inventoryNum > 0) {
                stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum;
                System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
                testReEntry();
            } else {
                resMessgae = "商品已售罄。";
                System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
            }
        } finally {
            lock.unlock();
        }
        return resMessgae;
    }

    private void testReEntry() {

        Lock lock = distributedLockFactory.getDistributedLock("redis");
        lock.lock();
        try {
            System.out.println("========测试可重入锁=========");
        }finally {
            lock.unlock();
        }


    }
}

package com.redis04_lock.service;

import com.redis04_lock.utils.DistributedLockFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class InventoryService03 {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private DistributedLockFactory distributedLockFactory;


    //  实现自动续期，后台自定义扫描程序，如果规定时间内没有完成业务逻辑，会调用家中自动续期脚本
    public String sale(){
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
                //  暂停120秒钟线程，演示自动续期功能
//                try {
//                    TimeUnit.SECONDS.sleep(120);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            } else {
                resMessgae = "商品已售罄。";
                System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
            }
        } finally {
            lock.unlock();
        }
        return resMessgae;
    }


}

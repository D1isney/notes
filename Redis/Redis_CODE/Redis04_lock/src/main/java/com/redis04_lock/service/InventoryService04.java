package com.redis04_lock.service;

import com.redis04_lock.utils.DistributedLockFactory;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService04 {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private DistributedLockFactory distributedLockFactory;


    //  引入 Redisson对应的官网推荐RedLock算法
    @Autowired
    private Redisson redisson;
    public String sale(){
        String resMessgae = "";
        RLock redissonLock = redisson.getLock("DisneyRedisLock");
        redissonLock.lock();
        try {
            // 1 抢锁成功，查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory01");
            // 2 判断库存书否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存，每次减少一个库存
            if (inventoryNum > 0) {
                stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum + "\t" + "，服务端口号：" + port;
                System.out.println(resMessgae);
            } else {
                resMessgae = "商品已售罄。" + "\t" + "，服务端口号：" + port;
                System.out.println(resMessgae);
            }
        } finally {
            // 改进点，只能删除属于自己的key，不能删除别人的
            if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
                redissonLock.unlock();
            }
        }
        return resMessgae;
    }
}

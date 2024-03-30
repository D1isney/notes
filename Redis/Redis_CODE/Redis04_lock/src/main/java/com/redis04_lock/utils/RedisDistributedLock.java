package com.redis04_lock.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

//  自己的Redis分布式锁
//@Component 引入DistributedLockFactory工厂模式，从工厂获得即可
public class RedisDistributedLock implements Lock {

    private StringRedisTemplate stringRedisTemplate;

    //KEYS[1]
    private String lockName;
    //ARGV[2]
    private String uuidValue;
    //ARGV[2]
    private long expireTime;

    public RedisDistributedLock(StringRedisTemplate stringRedisTemplate, String lockName) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
        this.uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
        this.expireTime = 25L;
    }
    public RedisDistributedLock(StringRedisTemplate stringRedisTemplate, String lockName,String uuid) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
        this.uuidValue = uuid + ":" + Thread.currentThread().getId();
        this.expireTime = 25L;
    }

    @Override
    public void lock() {
        System.out.println(stringRedisTemplate);
        tryLock();

    }

    @Override
    public boolean tryLock() {
        try {
            tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (time == -1L) {
            String script =
                    "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then     "
                            + "redis.call('hincrby', KEYS[1], ARGV[1], 1) "
                            + "redis.call('expire', KEYS[1], ARGV[2])"
                            + "     return 1 "
                            + "else "
                            + "     return 0 "
                            + "end";

            System.out.println("tryLock -> lockName：" + lockName + "\t"+"uuidValue："+uuidValue);

            Boolean execute = stringRedisTemplate.execute(
                    new DefaultRedisScript<>(script, Boolean.class),
                    Arrays.asList(lockName),
                    uuidValue,
                    String.valueOf(expireTime));
            while (!execute) {
                //暂停60毫秒
                try {
                    TimeUnit.MILLISECONDS.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then   "
                + "return nil   "
                + "elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then     "
                + "return redis.call('del', KEYS[1]) " +
                "else "
                + "    return 0 " +
                "end";

        System.out.println("unlock -> lockName：" + lockName + "\t"+"uuidValue："+uuidValue);
        Long execute = stringRedisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Arrays.asList(lockName),
                uuidValue,
                String.valueOf(expireTime));

        if (execute == null) {
            throw new RuntimeException("this lock doesn't exists !!");
        }
    }

    //  ---暂时用不到这两个，暂时不用重写

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

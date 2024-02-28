package com.redis02_demo.utils;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
    public static final String REDIS_IP_ADDR = "192.168.129.135";
    public static final String REDIS_PWD="zzq121700";
    public static JedisPool jedisPool;
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig,REDIS_IP_ADDR,6399,10000,REDIS_PWD);
    }
    public static Jedis getJedis() throws Exception {
        if(null!= jedisPool){
            return jedisPool.getResource();
        }
        throw new Exception("JedisPool is not ok");
    }

}

package com.redis01_jedis.demo;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisDemo {

    public static void main(String[] args) {
        //  1、connection获得，通过指定ip和端口号
        Jedis jedis = new Jedis("192.168.129.132", 6379);

        //  2、指定访问服务器的密码
        jedis.auth("zzq121700");

        //  3、获得了jedis客户端，可以像jdbc一样，访问redis
        System.out.println(jedis.ping());

        //  keys
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);

        //String
        jedis.set("k3", "v3");
        System.out.println(jedis.get("k3"));
        System.out.println(jedis.ttl("k3"));
        jedis.expire("k3",20L);

        //list
        jedis.lpush("list", "k1", "k2", "k3");
        List<String> list = jedis.lrange("list", 0, -1);
        for (String element : list) {
            System.out.println(element);
        }

        //hash
        Map<String,String> map1 = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        Map<String,String> map3 = new HashMap<>();
        map1.put("蛋蛋","Disney");
        jedis.hset("map",map1);
        System.out.println(jedis.hgetAll("map"));


        //set
        jedis.sadd("set","k1","k2");
        System.out.println(jedis.smembers("set"));

        //zSet
        jedis.zadd("zSet1",80,"k1");
        jedis.zadd("zSet1",60,"k2");
        jedis.zadd("zSet1",90,"k3");
        System.out.println(jedis.zcount("zSet1", 70, 90));
        System.out.println(jedis.zcard("zSet1"));


    }
}

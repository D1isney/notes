package com.redis01_jedis.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;

public class LettuceDemo {

    public static void main(String[] args) {

        //1、使用构建起链式编程来builder RedisURI
        RedisURI redis = RedisURI.builder()
                .redis("192.168.129.132")
                .withPort(6379)
                .withAuthentication("default", "zzq121700")
                .build();


        //2、创建连接客服端
        RedisClient client = RedisClient.create(redis);
        StatefulRedisConnection connect = client.connect();


        //3、创建操作的command，通过connect
        RedisCommands commands = connect.sync();
        //  keys
        List keys = commands.keys("*");
        System.out.println(keys);

        //  string
        commands.set("k1","v1");
        System.out.println("==========》"+commands.get("k1"));


        //4、各种关闭释放资源
        try {
            connect.close();
            client.shutdown();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

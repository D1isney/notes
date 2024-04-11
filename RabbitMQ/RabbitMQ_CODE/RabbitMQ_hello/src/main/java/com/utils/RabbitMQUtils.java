package com.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 创建连接池工具类
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        // 创还能连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.129.135");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}

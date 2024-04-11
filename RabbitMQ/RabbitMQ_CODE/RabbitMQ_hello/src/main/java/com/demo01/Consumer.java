package com.demo01;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者 接收信息的
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    //  接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //  创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP，连接队列
        factory.setHost("192.168.129.135");
        // 用户名
        factory.setUsername("admin");
        // 密码
        factory.setPassword("123456");

        //  创建新的连接
        Connection connection = factory.newConnection();
        //  创建信道
        Channel channel = connection.createChannel();

        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
//            String message = new String();
            System.out.println(message);
            System.out.println(new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费中断！");
        };

        /**
         * 消费者接收消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
         * 3.消费未成功的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);


    }
}

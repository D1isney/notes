package com.demo05;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    消息的接收 1
 */
public class ReceiveLogs01 {

    //  交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个叫交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");


        //  声明一个队列 临时的 队列名称是随机的 消费者断开与队列的连接的时候，队列就自动删除
        String queue = channel.queueDeclare().getQueue();

        /*
            绑定交换机与队列
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等到接受消息，把接收到的消息打印到屏幕上......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });
    }

}

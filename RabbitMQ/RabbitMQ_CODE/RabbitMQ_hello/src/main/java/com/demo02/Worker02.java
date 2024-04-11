package com.demo02;


import com.demo02.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程
 * 相当于消费者2
 */
public class Worker02 {
    // 队列的名称
    public static final String QUEUE_NAME = "hello";

    //  接收信息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 拿到信道
        Channel channel = RabbitMQUtils.getChannel();

        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑！");
        };
        //  接收消息
        /**
         * 消费者接收消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
         * 3.消费未成功回调
         * 4.消费者取消消费的回调
         */
        System.out.println("Consumer2等待接收消息 《========= ");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}

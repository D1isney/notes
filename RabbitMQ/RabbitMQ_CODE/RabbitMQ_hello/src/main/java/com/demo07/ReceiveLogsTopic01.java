package com.demo07;

import com.rabbitmq.client.*;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    声明主题交换机 以相关队列
    消费者C1
 */
public class ReceiveLogsTopic01 {
    //  交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //  接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        //  声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //  声明队列
        String queueName = "Q1";

        // 交换机队列声明
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息.......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：" + queueName + " 绑定键：" +message.getEnvelope().getRoutingKey());
        };
        CancelCallback callback = (consumerTag) -> {

        };

        //  接收消息
        channel.basicConsume(queueName, true, deliverCallback, callback);
    }
}

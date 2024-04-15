package com.demo09;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer_Queue {

    public static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback callback = consumerTag -> {

        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, callback);
    }
}

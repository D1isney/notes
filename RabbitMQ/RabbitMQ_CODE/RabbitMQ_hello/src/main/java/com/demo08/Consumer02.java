package com.demo08;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/*
    消费者2
 */
public class Consumer02 {
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("等待接收消息=========》");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接收的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback callback = consumerTag -> {

        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, callback);
    }

}

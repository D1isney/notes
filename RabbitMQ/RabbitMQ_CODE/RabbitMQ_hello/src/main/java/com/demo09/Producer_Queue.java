package com.demo09;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

//  优先级队列
public class Producer_Queue {

    public static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        Map<String, Object> arguments = new HashMap<>();
        //  官方允许的是0 - 255 之间，不要设置过大，浪费CPU与内存
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String info = "info" + i;
            if (i == 5) {
                AMQP.BasicProperties properties
                        = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, info.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, info.getBytes());
            }
        }
    }
}

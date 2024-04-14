package com.demo08;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/*
    死信队列生产者
 */
public class Producer02 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        for (int i = 1; i < 11; i++) {
            String message = "info：" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }

    }
}

package com.demo08;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    死信队列生产者
 */
public class Producer01 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        //  延迟消息（死信消息） 设置TTL的时间
        AMQP.BasicProperties properties
                = new AMQP.BasicProperties()
                .builder()
                .expiration("10000")
                .build();

        for (int i = 1; i < 11; i++) {
            String message = "info：" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes("UTF-8"));
        }

    }
}

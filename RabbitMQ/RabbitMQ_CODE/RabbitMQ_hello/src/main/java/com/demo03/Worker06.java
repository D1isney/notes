package com.demo03;

import com.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息手动应答
 */
public class Worker06 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "durable_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 需要让Queue进行持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        System.out.println("C4等待接收消息，处理时间较长======================");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            SleepUtils.sleep(30);
            System.out.println("接收到消息：" + message);
            // 1.消息标记tag
            // 2.false代表只应答接收到的那个消息，true为应答所有消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 设置不公平分发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        //  手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, callback, consumerTag -> {
            System.out.println(consumerTag + "：消费者取消消费接口逻辑");
        });
    }
}

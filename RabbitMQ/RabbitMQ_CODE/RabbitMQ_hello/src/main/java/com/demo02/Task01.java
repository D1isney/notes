package com.demo02;

import com.demo02.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * 可以发送大量的消息
 */
public class Task01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    //  发送大量消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 队列的声明
        /**
         * 生成一个队列
         * channel.queueDeclare(队列名，是否需要持久化，是否排它，是否自动删除，队列参数);
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费 false：只能一个消费者消费
         * 4.是否自动删除 最后一个消费者断开连接以后，该队列依据是否自动删除，true自动删除 false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 从控制台当中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 信道发布消息
             * channel.basicPublish();
             * 1.发送到哪个交换机
             * 2.路由的Key值是哪个 本次的队列的名称
             * 3，其他参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }
}

package com.demo03;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失的，放回队列中重新消费
 */
public class Task02 {
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个队列
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        //  从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}

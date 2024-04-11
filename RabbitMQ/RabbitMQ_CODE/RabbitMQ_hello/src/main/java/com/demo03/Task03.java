package com.demo03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Task03 {
//    private static final String DURABLE_QUEUE_NAME = "ack_queue";
    private static final String DURABLE_QUEUE_NAME = "durable_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 需要让Queue进行持久化
        boolean durable = true;
        //  声明一个队列
        channel.queueDeclare(DURABLE_QUEUE_NAME, durable, false, false, null);
        //  从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
//            channel.basicPublish("", DURABLE_QUEUE_NAME, null, message.getBytes("UTF-8"));
            // 消息持久化
            channel.basicPublish("",DURABLE_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}

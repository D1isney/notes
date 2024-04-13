package com.demo08;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/*
   死信队列实战
   消费者3
 */
public class Consumer03 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //  普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";


    //  死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //  死信交换机名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //  声明队列
        //   Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        //  正常队列，设置过期之后的死信交换机是谁
        //  x-dead-letter-exchange 固定写法
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //  设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //  TTL  单位毫秒 一般生产者设置
        //  arguments.put("x-message-ttl", 1000);

        //  设置正常队列长度的限制，只能装6个小希，超过的就会变成死信
        arguments.put("x-max-length", 6);


        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);


        //  binding
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息=========》");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer03接收的消息：" + new String(message.getBody(), "UTF-8"));
        };
        CancelCallback callback = consumerTag -> {

        };

        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, callback);
    }


}

package com.demo07;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/*
    生产者
 */
public class EmitLogTopic {
    //  交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        Map<String, String> bindingKeyMap = getStringStringMap();

        for (Map.Entry<String, String> entry : bindingKeyMap.entrySet()) {
            String routingKey = entry.getKey();
            String message = entry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + routingKey + "=====>" + message);
        }
    }

    private static Map<String, String> getStringStringMap() {
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列Q1Q2接收");
        bindingKeyMap.put("lazy.orange.elephant", "被队列Q1Q2接收");
        bindingKeyMap.put("quick.orange.fox", "被队列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox", "被队列Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列Q2接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何板顶不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是4个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是4个单词但匹配Q2");
        return bindingKeyMap;
    }
}

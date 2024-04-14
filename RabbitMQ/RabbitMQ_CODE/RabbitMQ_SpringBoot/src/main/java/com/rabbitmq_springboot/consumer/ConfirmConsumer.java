package com.rabbitmq_springboot.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

//  接收消息
@Component
@Slf4j
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("接收到的队列消息confirm.queue消息：{}", msg);
    }
}

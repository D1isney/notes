package com.rabbitmq_springboot.consumer;


import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

//  报警消费者
@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWaringMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("报警，路由不过去信息：{}", msg);
    }
}

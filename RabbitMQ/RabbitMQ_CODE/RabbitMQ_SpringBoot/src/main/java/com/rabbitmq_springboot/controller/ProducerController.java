package com.rabbitmq_springboot.controller;

import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //  发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message);
        log.info("发送消息内容{}", message);
    }
}

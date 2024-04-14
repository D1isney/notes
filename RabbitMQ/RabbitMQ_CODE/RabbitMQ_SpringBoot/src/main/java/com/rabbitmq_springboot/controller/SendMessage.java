package com.rabbitmq_springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 * 10s 40s
 */
@Slf4j
@RequestMapping("/ttl")
@RestController()
public class SendMessage {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //  开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个TTL队列：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40s的队列：" + message);
        log.info("发送成功！");
    }
}

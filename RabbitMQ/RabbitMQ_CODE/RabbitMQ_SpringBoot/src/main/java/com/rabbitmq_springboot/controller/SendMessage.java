package com.rabbitmq_springboot.controller;

import com.rabbitmq_springboot.config.DelayedQueueConfig;
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

    //  开始发消息
    //  既要发消息 还要发TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttl}")
    public void sendExpirationMsg(@PathVariable String message, @PathVariable String ttl) {
        log.info("当前时间：{},发送一条时长{}毫秒TTL信息给队列QC：{}"
                , new Date().toString()
                , ttl
                , message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            //  发送消息的时候延迟时长
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });
    }


    //  基于插件的发消息
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info("当前时间：{},发送一条时长{}毫秒信息给延迟队列delayed queue：{}",
                new Date().toString(),
                delayTime,
                message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            //  发送消息的时候延迟时长 单位毫秒
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }

}

package com.rabbitmq_springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //  注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    //  交换机确认回调方法
    /*
        1、发消息 交换机接收到了回调
        参数1、correlationData 保存回调信息的ID及相关信息
        参数2：ack 交换机收到消息 true
        参数3：失败的原因   cause   null
        2、发消息 交换机接收失败了 回调
        参数1、correlationData 保存回调信息的ID及相关信息
        参数2：ack 交换机收到消息 false
        参数3：失败的原因   cause   问题点
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了消息，ID：{}的消息", id);
        } else {
            log.info("交换机还未收到消息，ID:{}的消息。由于原因：{}", id, cause);
        }
    }

    //  消息回退
    //  只有消息发送不到目的地就会回退
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息{},被交换机{}，回退了，回退原因：{}，路由Key：{}"
                , new String(message.getBody(), StandardCharsets.UTF_8)
                , exchange, replyText, replyCode);
    }

}

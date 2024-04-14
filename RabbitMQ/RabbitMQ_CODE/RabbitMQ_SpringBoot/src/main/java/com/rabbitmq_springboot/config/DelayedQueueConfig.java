package com.rabbitmq_springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {
    //  交换机
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //  队列
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //  routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //  声明交换机
    /*
    	public CustomExchange(String name, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		super(name, durable, autoDelete, arguments);
		this.type = type;
	}
	1、交换机名称
	2、交换机类型
	3、是否需要持久化
	4、是否需要自动删除
	5、参数
     */
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");

        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message",
                true,
                false,
                arguments);
    }

    //  声明队列
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //  Binding
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange
    ) {
        return BindingBuilder
                .bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }
}

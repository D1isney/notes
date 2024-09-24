package com.websocketdemo01.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class MyWsConfig implements WebSocketConfigurer {
    @Resource
    private MyWsHandler myWsHandler;
    @Resource
    private MyWsInterceptor myWsInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //  handler、监听的地址
        registry.addHandler(myWsHandler,"/myWs1").addInterceptors(myWsInterceptor).setAllowedOrigins("*");
    }
}

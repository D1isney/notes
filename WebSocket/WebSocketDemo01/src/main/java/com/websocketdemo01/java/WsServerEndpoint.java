package com.websocketdemo01.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/myWs")
@Slf4j
public class WsServerEndpoint {

    //  保留在Map或者List里面 Map比较好
    //  ConcurrentHashMap现成安全的
    static Map<String,Session> sessionMap = new ConcurrentHashMap<>();

    //  连接建立时执行的操作
    @OnOpen
    public void onOpen(Session session) {
        //  保留这个Session
        sessionMap.put(session.getId(),session);
        log.info("WebSocket is Open");
    }

    //  收到了客户端消息执行的操作
    @OnMessage
    public String onMessage(String message) {
        log.info("Message received: {}", message);
        return "收到消息"+message;
    }

    //  连接关闭时的执行的操作
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("WebSocket is Closed");
    }


    //  每隔两秒执行
    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        for(String key:sessionMap.keySet()){
            try {
                sessionMap.get(key).getBasicRemote().sendText("发行消息");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

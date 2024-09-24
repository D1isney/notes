package com.websocketdemo01.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/*
    WebSocket 主处理程序
 */
@Component
@Slf4j
public class MyWsHandler extends AbstractWebSocketHandler {
    private static Map<String, SessionBean> sessionBeanMap;
    private static AtomicInteger clientIdMaker;
    static {
        sessionBeanMap = new ConcurrentHashMap<>();
        //  都是线程安全的
        clientIdMaker = new AtomicInteger(0);
    }

    //  建立连接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionBean sessionBean = new SessionBean(session,clientIdMaker.getAndIncrement());
        sessionBeanMap.put(session.getId(), sessionBean);
        log.info("{}：建立了连接！", sessionBeanMap.get(session.getId()).getClientId());
    }

    // 收到消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info("{}：{}", sessionBeanMap.get(session.getId()).getClientId(), message.getPayload());
    }

    //  传输异常
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        if (session.isOpen()){
            session.close();
        }
        sessionBeanMap.remove(session.getId());
    }

    //  连接关闭
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        log.info("{}：连接关闭！", sessionBeanMap.get(session.getId()).getClientId());
    }

    //  每隔两秒执行
    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        for(String key:sessionBeanMap.keySet()){
            try {
                sessionBeanMap.get(key).getWebSocketSession().sendMessage(new TextMessage("Message"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

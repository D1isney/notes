package com.wms.connect.websocket;

import com.wms.filter.login.Member;
import com.wms.utils.FastJsonUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@Getter
@Setter
public abstract class WebSocketServer {
    private static int onlineCount = 0;

    private String token;
    private Session session;
    private static ConcurrentHashMap<Object, WebsocketHolder> webSocketServerMap = new ConcurrentHashMap<>();

    public WebsocketHolder getHolder(String token, WebSocketServer webSocketServer) {
        WebsocketHolder websocketHolder = new WebsocketHolder();
        websocketHolder.setWebSocketServer(webSocketServer);
        return websocketHolder;
    }

    public Object getKey(String token) {
        return token;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.token = token;
        this.session = session;
        WebsocketHolder holder = getHolder(token, this);
        webSocketServerMap.put(getKey(token), holder);
        addOnline();
        log.info("有新窗口开始监听:{},当前在线人数为{}", holder.member.getUsername(), getOnlineCount());
        try {
            sendInfo("openSuccess:" + webSocketServerMap.keySet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSocketServerMap.remove(getKey(token));
            removeOnline();
            log.info("有一连接关闭！当前在线人数为{}", getOnlineCount());
            sendInfo("openSuccess:" + webSocketServerMap.keySet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        if ("ping".equals(message)) {
            sendInfo(token, "pong");
        }
        if (message.contains(":")) {
            String[] split = message.split(":");
            sendInfo(split[0], "receivedMessage:" + token + ":" + split[1]);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (error instanceof EOFException) {
            return;
        }
        if (error instanceof IOException && error.getMessage().contains("已建立的连接")) {
            return;
        }
        log.error("发生错误", error);
    }


    public static void sendInfo(String message) throws IOException {
        for (Object sid : webSocketServerMap.keySet()) {
            WebSocketServer webSocketServer = webSocketServerMap.get(sid).getWebSocketServer();
            webSocketServer.sendMessage(message);
        }
    }

    public static void sendInfo(Object token, String message) throws IOException {
        WebsocketHolder holder = webSocketServerMap.get(token);
        if (holder != null && holder.getWebSocketServer() != null) {
            holder.getWebSocketServer().sendMessage(message);
        }
    }

    public static void sendObject(Object obj) throws IOException {
        sendInfo(FastJsonUtils.convertObjectToJSON(obj));
    }

    public static void send(Object o) {
        try {
            sendObject(o);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendInfoByUserId(Long userId, Object message) throws IOException {
        WebsocketHolder websocketHolder = webSocketServerMap.get(userId);
        if (websocketHolder != null) {
            websocketHolder.getWebSocketServer().sendMessage(FastJsonUtils.convertObjectToJSON(message));
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session) {
            if (session.isOpen()) {
                this.session.getBasicRemote().sendText(message);
            }
        }
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WebsocketHolder {
        private Member member;
        private WebSocketServer webSocketServer;
    }

    public static synchronized void addOnline() {
        onlineCount++;
    }

    public static synchronized void removeOnline() {
        onlineCount--;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
}

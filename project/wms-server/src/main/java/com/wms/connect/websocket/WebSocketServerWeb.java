package com.wms.connect.websocket;

import com.wms.enums.WebSocketEnum;
import com.wms.exception.EException;
import com.wms.filter.login.Member;
import com.wms.utils.R;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServerWeb extends WebSocketServer implements ApplicationContextAware {

    //    @Resource
    private static WebTokenProvider tokenConvert;

    @Override
    public Object getKey(String token) {
        return getMember(token).getId();
    }

    @Override
    public WebsocketHolder getHolder(String token, WebSocketServer webSocketServer) {
        WebsocketHolder websocketHolder = new WebsocketHolder();
        websocketHolder.setWebSocketServer(webSocketServer);
        websocketHolder.setMember(getMember(token));
        return websocketHolder;
    }

    public Member getMember(String token) {
        return tokenConvert.authentication(token);
    }

    public static void send(WebSocketEnum webSocketEnum) {
        try {
//            R<?> r = new R<>();
            Push push = new Push();
            push.setType(webSocketEnum.getType());
            push.setData(webSocketEnum.getData());
            push.setMessage(webSocketEnum.getMessage());
            push.setCode(webSocketEnum.getCode());
            sendObject(push);
        } catch (IOException e) {
            throw new EException(e.getMessage());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        tokenConvert = applicationContext.getBean(WebTokenProvider.class);
    }
}

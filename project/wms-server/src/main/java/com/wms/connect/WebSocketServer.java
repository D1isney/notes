package com.wms.connect;

import com.wms.filter.login.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Getter
@Setter
public class WebSocketServer {
    private static int onlineCount = 0;

    private String token;


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WebsocketHolder{
        private Member member;
        private WebSocketServer webSocketServer;
    }

    public static synchronized void addOnline(){
        onlineCount++;
    }
    public static synchronized void removeOnline(){
        onlineCount--;
    }
}

package com.websocketdemo01.spring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionBean {
    private WebSocketSession webSocketSession;
    private Integer clientId;
}

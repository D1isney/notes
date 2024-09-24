# TCP/IP通信协议

## 1、常见的推送方式

> 1. 轮询
> 2. 长轮询
> 3. WebSocket
> 4. SSE

### 1.1、轮询

浏览器以指定的时间间隔向服务器发出HTTP请求，服务器实时返回数据给浏览器

缺点：数据可能会延迟、给服务器造成压力



### 1.2、长轮询

浏览器发送ajax请求，服务端接收到请求后，会阻塞请求直到有数据或者超时才返回



### 1.3、SSE

server-sent event：服务器发送事件

SSE在服务器和客户端之间打开一个单向通道

服务端响应的不再是一次性的数据包，而是text/event-stream类型的数据流信息

服务器有数据变更时将数据流式传输到客户端



### 1.4、WebSocket

> 全双工（Full Duplex）：允许数据在两个方向上同时传输
>
> 半双工（Half Duplex）：允许数据在两个方向上传输，但是同一个时间段内只允许一个方向上传输。
>
> HTTP协议：不属于全双工，严格意义上也不属于半双工

WebSocket是一种在基于TCP连接上进行全双工通信的协议



## 2、WebSocket API

### 2.1、客户端【浏览器】API

WebSocket对象创建

> 格式：协议://ip地址/访问路径
>
> 协议：协议名称为ws

```js
let ws = new WebSocket(URL);
```

WebSocket对象相关事件

| 事件    | 事件处理程序 | 描述                               |
| ------- | ------------ | ---------------------------------- |
| open    | ws.onopen    | 连接建立时触发                     |
| message | ws.onmessage | 客户端接收到服务器发送的数据时触发 |
| close   | ws.onclose   | 连接关闭时触发                     |

WebSocket对象提供的方法

| 方法名称 | 描述                                        |
| -------- | ------------------------------------------- |
| send()   | 通过WebSocket对象调用该方法发送数据给服务端 |

eg：

```html
<script>
	let ws = new WebSocket("ws://localhost/chat");
    ws.onopen = function(){};
    ws.onmessage = function(evt){
        //	通过evt.data 可以获取服务器发送的数据
    };
    ws.onclose = function(){};
</script>
```



### 2.2、服务端 API

> Tomcat的7.0.5版本开始支持WebSocket，并且实现了Java WebSocket规范。
>
> Java WebSocket应用由一系列的Endpoint组成。Endpoint是一个Java对象，代表WebSocket连接的一端，对于服务端，我们可以视为处理具体WebSocket消息的接口。
>
> 我们可以通过两种方式定义Endpoint：
>
> 1. 第一种是编程式，即继承类javax.websocket.Endpoint并实现其方法。
> 2. 第二种是注解式，即定义一个POJO，并添加@ServerEndpoint相关注解。
>
> Endpoint实例在WebSocket握手时创建，并在客户端与服务端连接过程中有效，最后在连接关闭时结束。在Endpoint接口中明确了与其生命周期相关的方法，规范实现者确保生命周期的各个阶段调用实例的相关方法。生命周期如下：
>
> | 方法      | 描述                                                         | 注解     |
> | --------- | ------------------------------------------------------------ | -------- |
> | onOpen()  | 当开启一个新的会话时调用，该方法是客户端与服务端握手成功后调用的方法 | @OnOpen  |
> | onClose() | 当会话关闭时调用                                             | @OnClose |
> | onError() | 当连接过程中异常时调用                                       | @OnError |



### 2.3、服务端如何接受客户端发送的数据呢？

- 编程式：通过添加MessageHandel消息处理器来接受消息
- 注解式：在定义Endpoint时，通过@OnMessage注解指定接收消息的方法



### 2.4、服务端如何推送数据给客户端呢？

发送消息则由RemoteRndpoint完成，其实由Session维护

发送消息有两种方式

- 通过session.getBasicRemote获取同步消息发送的实例，然后调用其sendXxx()方法发送消息
- 通过session.getAsyncRemote获取一步消息发送实例，然后调用其sendXxx()方法发送消息



eg：

```java
@ServerEndpoint("/chat")
@Component
public class ChatEndponit{
    @OnOpen
    //	连接建立时被调用
    public void onOpen(Session session,EndpointConfig config){}
    @OnMessage
   	//	 接收到客户端发送的数据时被调用
    public void onMessage(String message){}
    @OnClose
    //	连接关闭时被调用
    public void onClose(Session session){}
}
```





## 3、WebSocket概述

WebSocket是一种在单个TCP连接上进行全双工通信的协议。WebSocket通信协议于2011年被IETF定为标准RFC 6455，并由RFC7936补充规范。WebSocket API也被定为标准。

WebSocket使得客户端和服务器之间的数据交互变得更加简单，允许服务端主动向客户端推送数据。在WebSocket API中，浏览器和服务器只需要完成一次握手，两者之间就可以创建持久性的连接，并进行双向数据传输。



## 4、WebSocket如何通信

1. 浏览器发送http请求，请求建立WebSocket连接
2. 服务器响应同意协议更改
3. 相互发送数据



## 5、底层原理

WebSocket协议建立在TCP协议基础上的，所以服务端也容易实现，不同的语言都有支持。

TCP协议是全双工协议，HTTP协议基于它，但设计成了单向的。

WebSocket没有同源限制。



## 6、使用Spring封装

> Spring实现了WebSocket功能

实现业务功能：基于Java注解、基于Spring提供的上层封装

服务终端类：

### 6.1、用Java注解

1. 监听连接@ServerEndpoint
2. 连接成功@OnOpen
3. 连接关闭@OnClose
4. 收到消息等状态@OnMessage

配置类：

1. 把Spring中的ServerEndpoint对象注入进来

```java
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
```

```java
package com.websocketdemo01.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

```java
package com.websocketdemo01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//  开启定时任务
@EnableScheduling
public class WebSocketDemo01Application {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketDemo01Application.class, args);
    }

}
```

```http
ws://127.0.0.1:8080/myWs
```



### 6.2、Spring提供的类和接口

> HttpSessionshakeInterceptor（抽象类）：握手拦截器，在握手前后添加操作。
>
> AbstractWebSocketHandler（抽象类）：WebSocket处理程序，监听连接器前，连接中，连接后。
>
> WebSocketConfigure（接口）：配置程序，比如配置监听哪个接口，上面的握手拦截器，处理程序的使用。

```java
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
```

```java
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
```

```java
package com.websocketdemo01.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/*
    握手拦截器
 */
@Component
@Slf4j
public class MyWsInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info(request.getRemoteAddress().toString()+"开始握手");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info(request.getRemoteAddress().toString()+"完成握手");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
```

```java
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
```



## 7、浅谈WebSocket应用场景

1. 弹幕
2. 股票基金报价
3. 网页总数统计
4. 网页聊天
5. 消息订阅
6. 多玩家消息，实时性比较高
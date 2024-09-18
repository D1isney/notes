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
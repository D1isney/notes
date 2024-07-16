# Netty



# 1、BIO

在JDK1.4出来之前，建立网络连接的时候采用BIO模式，需要现在服务端启动一个ServerSocket，然后在客户端启动Socket来对服务端进行通信，默认情况下服务端需要对每个请求建立一堆线程等待请求，而客户端发送请求后，先咨询服务端是否有现成响应，如果没有则会一直等待或者遭到拒绝请求，如果有的话，客户端线程会请求结束后才继续执行。

```java
package com.netty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("等待连接....");
            Socket socket = serverSocket.accept();
//              阻塞IO
//            handler(socket);
            //  线程成本太高 C10K问题 以及 C10M问题
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            System.out.println("有客户端连接了.....");
        }
    }

    private static void handler(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备Read....");
        int read = socket.getInputStream().read(bytes);
        System.out.println("读取完毕");
        if (read != -1) {
            System.out.println("接收到的数据：" + new String(bytes, 0, read));
        }
    }
}
```



# 2、NIO

NIO本身是基于事件驱动思想来完成的，其主要想解决的是BIO的大并发问题： 在使用同步I/O的网络应用中，如果要同时处理多个客户端请求，或是在客户端要同时和多个服务器进行通讯，就必须使用多线程来处理。也就是说，将每一个客户端请求分配给一个线程来单独处理。这样做虽然可以达到我们的要求，但同时又会带来另外一个问题。由于每创建一个线程，就要为这个线程分配一定的内存空间（也叫工作存储器），而且操作系统本身对线程的总数也有一定的限制。如果客户端的请求过多，服务端程序可能会因为不堪重负而拒绝客户端的请求，甚至服务器可能会因此而瘫痪。

    NIO基于Reactor，当socket有流可读或可写入socket时，操作系统会相应的通知应用程序进行处理，应用再将流读取到缓冲区或写入操作系统。  也就是说，这个时候，已经不是一个连接就要对应一个处理线程了，而是有效的请求，对应一个线程，当连接没有数据时，是没有工作线程来处理的。

BIO与NIO一个比较重要的不同，是我们使用BIO的时候往往会引入多线程，每个连接一个单独的线程；而NIO则是使用单线程或者只使用少量的多线程，多个连接共用一个线程。

NIO的最重要的地方是当一个连接创建后，不需要对应一个线程，这个连接会被注册到多路复用器上面，所以所有的连接只需要一个线程就可以搞定，当这个线程中的多路复用器进行轮询的时候，发现连接上有请求的话，才开启一个线程进行处理，也就是一个请求一个线程模式。

      在NIO的处理方式中，当一个请求来的话，开启线程进行处理，可能会等待后端应用的资源(JDBC连接等)，其实这个线程就被阻塞了，当并发上来的话，还是会有BIO一样的问题。

　　HTTP/1.1出现后，有了Http长连接，这样除了超时和指明特定关闭的http header外，这个链接是一直打开的状态的，这样在NIO处理中可以进一步的进化，在后端资源中可以实现资源池或者队列，当请求来的话，开启的线程把请求和请求数据传送给后端资源池或者队列里面就返回，并且在全局的地方保持住这个现场(哪个连接的哪个请求等)，这样前面的线程还是可以去接受其他的请求，而后端的应用的处理只需要执行队列里面的就可以了，这样请求处理和后端应用是异步的.当后端处理完，到全局地方得到现场，产生响应，这个就实现了异步处理。

```java
package com.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    //  保存客户端连接
    static List<SocketChannel> channelArrayList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 9000));

        //  设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);
        System.out.println("服务启动成功！");

        while (true) {
            //  非阻塞模式accept方法不会阻塞，否则会阻塞
            //  NIO的非阻塞是由操作系统内部实现的，底层用了Linux内核的accept函数
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) { //   如果有客户端进行连接
                System.out.println("连接成功！");
                //  设置SocketChannel为非阻塞
                socketChannel.configureBlocking(false);
                //  保存客户端连接在List中
                channelArrayList.add(socketChannel);
            }

            //  遍历连接进行数据读取
            Iterator<SocketChannel> iterator = channelArrayList.iterator();
            while (iterator.hasNext()) {
                SocketChannel sc = iterator.next();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                //  非阻塞模式read方法不会阻塞，否则会阻塞
                int len = sc.read(byteBuffer);
                //  如果有数据，就把数据打印出来
                if (len > 0) {
                    System.out.println("接收到的数据：" + new String(byteBuffer.array()));
                } else if (len == -1) { // 如果客户端断开连接，把socket从集合中去掉
                    iterator.remove();
                    System.out.println("客户端断开连接......");
                }
            }
        }
    }
}
```

IO多路复用

```java
package com.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioSelectServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 9000));

        //  设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);
        //  打开Selector处理Channel，即创建epoll
        Selector selector = Selector.open();
        //  把ServerSocketChannel注册到Selector上，并且Selector对客户端accept连接操作感兴趣
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功！");

        while (true) {
            //  阻塞等待需要处理的事件发生
            selector.select();

            //  获取Selector中注册的全部事件的SelectionKey实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            //  遍历selectionKeys对事件进行处理
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //  如果是OP_ACCEPT事件，则进行连接获取和时间注册
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    //  这里值注册了读事件，如果需要给客户端发送数据可以注册写事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功！");
                } else if (key.isReadable()) { //   如果是OP_READ事件，则进行读取和打印
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(byteBuffer);
                    //  如果有数据，把数据打印出来
                    if (len > 0) {
                        System.out.println("接收到的信息：" + new String(byteBuffer.array(), 0, len));
                    } else if (len == -1) {
                        System.out.println("客户端断开连接.....");
                        socketChannel.close();
                    }
                }
                //  从事件集合里删除本次处理的key，防止下次select重复处理
                iterator.remove();
            }
        }
    }
}
```



# 3、NIO底层Epoll实现源码剖析


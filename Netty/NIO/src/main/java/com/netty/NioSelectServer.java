package com.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioSelectServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));

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

package com.redis05_epoll;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootTest
class Redis05EpollApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    public void RedisClient01() throws IOException {
        System.out.println("--------RedisClient01 start");
        Socket socket = new Socket("127.0.0.1", 6379);
        System.out.println("--------RedisClient01 connection over");
    }

    @Test
    public void RedisClient02() throws IOException {
        System.out.println("--------RedisClient02 start");
        Socket socket = new Socket("127.0.0.1", 6379);
        System.out.println("--------RedisClient02 connection over");
    }

    @Test
    public void RedisServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(6379);
        while (true) {
            System.out.println("模拟RedisServer启动------111 等待连接");
            Socket accept = serverSocket.accept();
            System.out.println("------222 成功连接：" + IdUtil.simpleUUID());
        }
    }


}

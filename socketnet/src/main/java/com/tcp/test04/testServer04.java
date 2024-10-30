package com.tcp.test04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer04 {
    public static void main(String[] args) throws IOException {
        //  客户端：将本地文件上传到服务器。接收服务器的反馈
        //  服务器：接收客户端上传的文件，上床完毕之后给出反馈

        ServerSocket ss = new ServerSocket(10086);

        Socket socket = null;
        while (true) {
            socket = ss.accept();
            new Thread((new MyRunnable(socket))).start();
        }


    }
}

package com.tcp.test05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class testServer05 {
    public static void main(String[] args) throws IOException {
        //  客户端：将本地文件上传到服务器。接收服务器的反馈
        //  服务器：接收客户端上传的文件，上床完毕之后给出反馈

        ServerSocket ss = new ServerSocket(10086);

        ThreadPoolExecutor t = new ThreadPoolExecutor(3,3,50000, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());

        Socket socket = null;
        while (true) {
            socket = ss.accept();
            Thread thread = new Thread((new MyRunnable(socket)));
            t.submit(thread);
        }


    }
}

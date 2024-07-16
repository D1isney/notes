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
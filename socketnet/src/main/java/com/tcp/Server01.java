package com.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server01 {
    public static void main(String[] args) throws IOException {
        //  1、创建对象
        ServerSocket serverSocket = new ServerSocket(10086);

        //  2、监听客户端的连接
        Socket accept = serverSocket.accept();

        //  3、从丽娜姐通道中获取输入流读取书
        InputStream inputStream = accept.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);

        int b;
        while ((b = isr.read()) != -1) {
            System.out.print((char) b);
        }

        //  所有数据读完
        //  4、释放资源
        accept.close();
        serverSocket.close();
    }
}

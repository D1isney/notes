package com.tcp.test02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer02 {
    public static void main(String[] args) throws IOException {
        //  客户端：发送一条数据，接收服务端反馈的信息并打印
        //  服务器：接收数据并打印，再给客户端反馈消息

        ServerSocket serverSocket = new ServerSocket(10086);
        Socket accept = serverSocket.accept();

        InputStream inputStream = accept.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(inputStreamReader);

        int b;
        while ((b = buf.read()) != -1) {
            System.out.print((char) b);
        }

        OutputStream outputStream = accept.getOutputStream();
        outputStream.write("收到收到".getBytes());


        serverSocket.close();

    }
}

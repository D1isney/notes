package com.tcp.test01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer01 {
    public static void main(String[] args) throws IOException {
        //  客户端：多次发送数据
        //  服务器：多次接收数据，并且打印

        ServerSocket sc = new ServerSocket(10086);
        Socket socket = sc.accept();
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(inputStreamReader);
        int b = 0;
        while ((b =buf.read()) != -1) {
            System.out.print((char) b);
        }


    }
}

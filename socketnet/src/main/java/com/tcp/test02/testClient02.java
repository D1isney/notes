package com.tcp.test02;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class testClient02 {
    public static void main(String[] args) throws IOException {
        //  客户端：发送一条数据，接收服务端反馈的信息并打印
        //  服务器：接收数据并打印，再给客户端反馈消息

        Socket socket = new Socket("127.0.0.1", 10086);

        //  写出数据
        String str = "2024年10月29日22:18:01";
        socket.getOutputStream().write(str.getBytes());
        //  结束标记
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        int b;
        while ((b = inputStreamReader.read()) != -1) {
            System.out.println((char) b);
        }

        socket.close();

    }
}

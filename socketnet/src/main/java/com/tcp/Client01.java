package com.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client01 {
    public static void main(String[] args) throws IOException {
        // TCP  协议，发送数据
        //  1、创建Socket对象
        //  细节：在创建对象的时候会连接服务端
        //  如果连接不上，代码会报错
        Socket socket = new Socket("127.0.0.1", 10086);

        //  2、可以从丽娜姐通道中获取输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("Hello".getBytes());

        //  3、释放资源
        outputStream.flush();
        socket.close();

    }
}

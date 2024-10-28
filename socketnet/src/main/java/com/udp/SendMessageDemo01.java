package com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendMessageDemo01 {
    public static void main(String[] args) throws IOException {
        //  1、创建DatagramSocket（快递公司）
        //  绑定端口，以后通过这个端口往外发送
        //  空参：所有可用的端口中随机一个进行使用
        //  有参：指定端口号进行绑定
        DatagramSocket ds = new DatagramSocket();

        //  2、打包数据
        String str = "Hello";
        byte[] bytes = str.getBytes();
        InetAddress byName = InetAddress.getByName("127.0.0.1");
        int port = 10086;
        DatagramPacket dp = new DatagramPacket(bytes,bytes.length,byName,port);

        //  发送数据
        ds.send(dp);

        //  释放资源
        ds.close();

    }
}

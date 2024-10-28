package com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReceiveMessageDemo01 {
    public static void main(String[] args) throws IOException {
        //  1、创建DatagramSocket对象
        //  在接收的时候，一定要绑定端口，而且绑定的端口一定跟发送的端口保持一致
        DatagramSocket ds = new DatagramSocket(10086);

        //  2、接收数据
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        //  该方法是阻塞的
        //  程序执行到这一块会一直等，等到收到消息为止
        ds.receive(dp);

        //  3、解析数据报
        byte[] data = dp.getData();
        int length = dp.getLength();
        InetAddress address = dp.getAddress();
        int port = dp.getPort();
        System.out.println("接收到数据" + new String(data, 0, length));
        System.out.println("该数据是从" + address + "这台电脑中的" + port + "发送出来的");

        ds.close();
    }
}

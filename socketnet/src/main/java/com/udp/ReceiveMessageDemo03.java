package com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReceiveMessageDemo03 {
    public static void main(String[] args) throws IOException {
        //  接收端
        MulticastSocket socket = new MulticastSocket(100861);

        //  将当前本机，添加到224.0.0.1的这一组中
        InetAddress byName = InetAddress.getByName("224.0.0.1");
        socket.joinGroup(byName);
        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        socket.receive(dp);

        byte[] data = dp.getData();
        int length = dp.getLength();
        String ip = dp.getAddress().getHostAddress();
        String name = dp.getAddress().getHostName();
        System.out.println("ip为：" + ip + "，主机名：" + name + "的人，发送了数据：" + new String(data, 0, length));
        socket.close();
    }
}

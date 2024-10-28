package com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendMessageDemo03 {
    public static void main(String[] args) throws IOException {

        //  组播发送端
        MulticastSocket socket = new MulticastSocket();

        String s = "Hello";
        byte[] buf = s.getBytes();
        //  组播地址
        InetAddress byName = InetAddress.getByName("224.0.0.1");
        int port = 100861;

        DatagramPacket dp = new DatagramPacket(buf, buf.length, byName, port);
        socket.send(dp);
        socket.close();
    }
}

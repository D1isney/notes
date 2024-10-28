package com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReceiveMessageDemo02 {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(10086);
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] data = packet.getData();
            int length = packet.getLength();

            System.out.println("数据：" + new String(data, 0, length));
            System.out.println("来自：" + address + ":" + port);
        }
    }
}

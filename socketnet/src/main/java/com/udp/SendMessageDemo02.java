package com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Scanner;

public class SendMessageDemo02 {
    public static void main(String[] args) throws IOException {
        /*
            按照下面的要求实现程序
                UDP发送数据：数据来自于键盘录入，直到输入的数据是886，发送数据结束
                UDP接收数据：因为接收端不知道发送端什么时候停止发送，故采用死循环接收
         */
        DatagramSocket socket = new DatagramSocket();
        Scanner scanner = new Scanner(System.in);
        String next = scanner.nextLine();
        DatagramPacket dp = null;
        while (!Objects.equals(next, "886")) {
            byte[] bytes = next.getBytes();
            InetAddress byName = InetAddress.getByName("127.0.0.1");
            int port = 10086;
            dp = new DatagramPacket(bytes, bytes.length, byName, port);
            socket.send(dp);
            next = scanner.nextLine();
        }
        socket.close();
    }
}

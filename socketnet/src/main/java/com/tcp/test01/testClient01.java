package com.tcp.test01;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class testClient01 {
    public static void main(String[] args) throws IOException {
        //  客户端：多次发送数据
        //  服务器：多次接收数据，并且打印

        Socket socket = new Socket("127.0.0.1", 10086);
        OutputStream outputStream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入要说的话：");
        String str = scanner.nextLine();
        while (!Objects.equals(str, "886")) {
            outputStream.write(str.getBytes());
            System.out.print("输入要说的话：");
            str = scanner.nextLine();
            System.out.println();
        }
        outputStream.close();
        socket.close();
    }
}

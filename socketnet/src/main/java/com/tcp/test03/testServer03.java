package com.tcp.test03;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer03 {
    public static void main(String[] args) throws IOException {
        //  客户端：将本地文件上传到服务器。接收服务器的反馈
        //  服务器：接收客户端上传的文件，上床完毕之后给出反馈

        ServerSocket ss = new ServerSocket(10086);

        Socket socket = ss.accept();
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        String path = "K:\\GitHub\\notes\\socketnet\\server\\1.png";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        int len;
        byte[] buffer = new byte[1024];
        while ((len = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("上传成功");
        bw.newLine();
        bw.flush();

        socket.close();
        ss.close();

    }
}

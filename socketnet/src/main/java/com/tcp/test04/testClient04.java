package com.tcp.test04;

import java.io.*;
import java.net.Socket;

public class testClient04 {
    public static void main(String[] args) throws IOException {
        //  客户端：将本地文件上传到服务器。接收服务器的反馈
        //  服务器：接收客户端上传的文件，上床完毕之后给出反馈

        Socket socket = new Socket("127.0.0.1", 10086);

        String path = "K:\\GitHub\\notes\\socketnet\\client\\1.png";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));

        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        byte[] buf = new byte[1024];
        int len;
        while ((len = bis.read(buf)) != -1) {
            bos.write(buf, 0, len);
        }

        //  往服务器写出结束
        socket.shutdownOutput();

        //  接收回写
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = br.readLine();
        System.out.println(s);



        socket.close();
    }
}

# 网络编程

## 1、什么是网络编程

> 在网络通信协议下，不同计算机上运行的程序，进行的数据传输。

- 应用场景：即时通讯、网络对战、金融证券、国际贸易、邮件等等。

  不管是什么场景，都是计算机跟计算机之间通过网络进行数据传输。

- Java中可以使用java.net包下的技术轻松开发出常见的网络应用程序。

> 常见的软件架构有哪些？
>
> CS\BS

> 通信的软件架构CS\BS的各有什么区别和优缺点
>
> - CS：客户端服务端模式需要开发客户端
> - BS：浏览器服务端模式不需要开发客户端
> - CS：适合定制专业化的办公软件：如IDEA、网游
> - BS：适合移动互联网应用，可以在任何地方随时访问的系统

## 2、网络编程三要素

1. IP

   设备在网络中的地址，是唯一的标识。

2. 端口号

   应用程序在设备中唯一的标识。

3. 协议

   数据在网络中传输的规则，常见的协议有UDP、TCP、HTT{、HTTPS、FTP

> IP
>
> 全程：Internet Protocol，是互联网协议地址，也称IP地址。
>
> 是分配给上网设备的数字标签。
>
> 通俗：上网设备在网络中的地址，是唯一的。
>
> 常见的IP分类为：**IPv4、IPv6**
>
> IPv4：全程Internet Protocol version 4，互联网通信协议第四版。
>
> 采用32位地址长度，分为4组。=》 点分十进制表示法 127.0.0.1
>
> IPv6：全程Internet Protocol version 6，互联网通信协议第六版。
>
> 由于互联网的蓬勃发展，IP地址的需求量越来越大，而IPv4的模式下IP的总数是有限的。
>
> 采用128位地址长度，分为8组。 =》 冒分16进制表示法

IPv4的地址分类形式：

- 公网地址（万维网使用）和私有地址（局域网使用）
- 192.168.开头的就是私有地址，范围即为192.168.0.0 - 192.168.255.255，专门为组织机构内部使用，以此节省IP
- 特殊IP地址：127.0.0.1，也可以是localhost: 是回送地址也称本地回环地址，也称本机IP，永远只会寻找当前所在本机



InetAddrtess的使用：

- Inet4Address、Inet6Address

  ```java
  package com.InetAddress01;
  
  import java.net.InetAddress;
  import java.net.UnknownHostException;
  
  public class MyInetAddress01 {
      public static void main(String[] args) throws UnknownHostException {
          //  1、获取InetAddress对象
          //  通过IP
          InetAddress byName = InetAddress.getByName("192.168.0.109");
          //  通过主机名
          InetAddress byName1 = InetAddress.getByName("Disney");
  
          String hostName = byName1.getHostName();
          System.out.println(hostName); //Disney
          String hostAddress = byName1.getHostAddress();
          System.out.println(hostAddress);//192.168.0.109
      }
  }
  ```

- 端口号

  > 应用程序在设备中的标识。
  >
  > 端口号：由两个字节表示的整数，取值范围：0 ~ 65535
  >
  > 其中0 ~ 1023之间的端口号用于一些知名的网络服务或应用。
  >
  > 注意：一个端口号只能被一个应用程序使用。

- 协议

  > 计算机网络中，连接和通信的规则被称为网络通信协议。
  >
  > - OSI参考模型：世界互联协议标准，全球通信规范，单模型过于理想化，未能在因特网上进行广泛推广。
  > - TCP/IP参考模型（或TCP/IP协议）：事实上的国际标准。
  >
  > | OSI参考模型 | TCP参考模型       | TCP/IP参考模型各层对应协议 | 面向哪些                                                     |
  > | ----------- | ----------------- | -------------------------- | ------------------------------------------------------------ |
  > | 应用层      | 应用层            | HTTP、FTP、Telnet、DNS...  | 一般是应用程序需要关注的，如浏览器、邮箱。程序员一般在这一层开发 |
  > | 表示层      |                   |                            |                                                              |
  > | 会话层      |                   |                            |                                                              |
  > | 传输层      | 传输层            | TCP、UDP、...              | 选择传输使用的TCP、UDP协议                                   |
  > | 网络层      | 网络层            | IP、ICMP、ARP              | 封装自己的IP，对方的IP等信息                                 |
  > | 数据链路层  | 物理 + 数据链路层 | 硬件设备。0101 0001        | 转换成二进制利用物理设备传输                                 |
  > | 物理层      |                   |                            |                                                              |



## 3、UDP通信程序

> - 用户数据报协议UDP（User Datagram Protocol）
> - UDP是面向无连接通信协议。
> - 速度快，有大小限制一次最多发送64K，数据不安全，易丢失数据。

1. 创建发送端DatagramSocket对象
2. 数据打包（DatagramPacket）
3. 发送数据
4. 释放资源

```java
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
```



1. 创建接收端的DatagramSocket对象
2. 接收打包好的数据
3. 解析数据包
4. 释放资源

```java
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
```

> UDP聊天室Demo

```java
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
```

```java
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
```

> UDP的三种通信方式
>
> - 单播
> - 组播
> - 广播



## 4、TCP通信程序

> - 传输控制协议TCP（Transmission Control Protocol）
> - TCP协议是面向连接的通信协议。
> - 速度慢，没有大小限制，数据安全。
>
> TCP通信协议是一种可靠的网络协议，它在通信的两端各建立一个Socket对象
>
> 通信之前保证连接已经建立
>
> 通过Socket产生IO流来进行网络通信

```java
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
```

```java
package com.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server01 {
    public static void main(String[] args) throws IOException {
        //  1、创建对象
        ServerSocket serverSocket = new ServerSocket(10086);

        //  2、监听客户端的连接
        Socket accept = serverSocket.accept();

        //  3、从丽娜姐通道中获取输入流读取书
        InputStream inputStream = accept.getInputStream();
        int b;
        while ((b = inputStream.read()) != -1) {
            System.out.print((char) b);
        }

        //  所有数据读完
        //  4、释放资源
        accept.close();
        serverSocket.close();
    }
}
```

> 三次握手
>
> - 确保连接建立
>   1. 客户端向服务器发送连接请求（等待服务器确认）
>   2. 服务器向客户端返回一个响应（告诉客户端收到了请求）
>   3. 客户端向服务器再次发出确认信息（连接建立）
>
> 四次挥手
>
> - 确保连接断开，且数据处理完毕
>   1. 客户端向服务器发出取消连接请求
>   2. 服务器向客户端返回一个响应（表示收到客户端取消请求）
>   3. 服务器向客户端发送确认取消信息
>   4. 客户端再次发送确认信息（连接取消）
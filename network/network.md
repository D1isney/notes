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

- 123







## 3、UDP通信程序



## 4、TCP通信程序

## 5、练习
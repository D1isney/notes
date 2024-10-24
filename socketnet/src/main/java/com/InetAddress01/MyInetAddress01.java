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

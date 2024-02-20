package com.demo01;

public class Proxy implements Rent{
    private Host host;

    public Proxy() {
    }

    public Proxy(Host host) {
        this.host = host;
    }

    @Override
    public void rent() {
        seeHouse();
        host.rent();
        free();
    }

    //  看房
    public void seeHouse(){
        System.out.println("中介带看房");
    }

    public void free(){
        System.out.println("收中介费");
    }
}

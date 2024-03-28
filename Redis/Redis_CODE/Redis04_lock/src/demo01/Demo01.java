package demo01;

public class Demo01 {
    final Object obj = new Object();
    public void entry01(){
        new Thread(()->{
            synchronized (obj){
                System.out.println(Thread.currentThread().getName()+"\t"+"外层调用");
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
                    synchronized (obj) {
                        System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
                    }
                }
            }
        },"t1").start();
    }

    public static void main(String[] args) {
        Demo01 demo01 = new Demo01();
        demo01.entry01();
    }
}
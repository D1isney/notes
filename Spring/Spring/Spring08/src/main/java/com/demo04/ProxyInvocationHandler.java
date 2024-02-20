package com.demo04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyInvocationHandler implements InvocationHandler {

    //  被代理的接口
    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    //  生成得到代理类
    public Object getProxy() {
        return Proxy
                .newProxyInstance(
                        this.getClass().getClassLoader(),
                        target.getClass().getInterfaces(),
                        this
                );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log(method.getName());
        //  动态代理的本质，就是使用反射机制实现
        Object invoke = method.invoke(target, args);
        return invoke;
    }

    public void log(String str) {
        System.out.println("【执行了】" + str + "方法！");
    }


}

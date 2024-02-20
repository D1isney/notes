package com.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

//  方式三：使用注解方式实现AOP
@Aspect //  标注这个类是一个切面
//@Component    //  或者用这种方法注册Bean
public class AnnotationPointCut {

    @Before("execution(* com.service.UserServiceImpl.*(..))")
    public void BeforeLog(){
        System.out.println("AnnotationPointCut方法执行前");
    }
    @After("execution(* com.service.UserServiceImpl.*(..))")
    public void AfterLog(){
        System.out.println("AnnotationPointCut方法执行后");
    }

    //  在环绕增强中，我们可以给定一个参数，代表我们要获取处理切入的点
    @Around("execution(* com.service.UserServiceImpl.*(..))")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("AnnotationPointCut Around Before");

        //  获得签名
        Signature signature = proceedingJoinPoint.getSignature();
        System.out.println("signature："+signature);

        //  执行方法
        Object proceed = proceedingJoinPoint.proceed();

        System.out.println("AnnotationPointCut Around After");
    }
}

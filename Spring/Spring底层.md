# Spring底层

## 1、容器接口

### 1.1、BeanFactory能做什么

> 什么是BeanFactory
>
> - 它是ApplicationContext的父接口
> - 它才是Spring的核心容器，主要的ApplicationContext实现都【结合】了它的功能

> BeanFactory能做什么
>
> - 表面上只有getBean
> - 实际上控制反转、基本的依赖注入、直至Bean的生命周期的各种功能，都是由它的实现类提供

### 1.2、ApplicationContext有哪些扩展功能

- 国际化
- 通配符获取资源
- 获取配置信息
- 事件发布



## 2、容器实现

### 2.1、BeanFactory实现的特点

```java
package com.thorough.beanFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

public class BeanFactoryDemo01 {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AbstractBeanDefinition beanDefinition
                = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);

        //  BeanFactory不能解析注解
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        //  给BeanFactory添加常用的后置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //  加完后置处理器之后，BeanFactory就就可以解析
        //  @Configuration
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println("-------------------");
        //  拿到BeanFactory所有的后置处理器
        Map<String, BeanFactoryPostProcessor> beansOfType
                = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        //  执行后置处理器，这样就可以拿到@bean这些注解
        //  主要不冲了一些bean的定义
        beansOfType.values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println("-------------");
        //  拿到bean1
//        Bean1 bean1 = beanFactory.getBean(Bean1.class);
//        System.out.println(bean1.getBean2());


        //  bean的后处理器，针对bean的生命周期的各个阶段提供扩展，例如：@Autowired @Resource
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);
        //  bean创建前就实例化好对象，而不是使用懒加载的形式
        beanFactory.preInstantiateSingletons();
        System.out.println("-------------");
        //  拿到bean1
        Bean1 bean11 = beanFactory.getBean(Bean1.class);
        System.out.println(bean11.getBean2());

    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }


    public static class Bean1 {
        public Bean1() {
            System.out.println("Bean1 Constructor");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

    }

    public static class Bean2 {
        public Bean2() {
            System.out.println("Bean2 Constructor");
        }
    }
}
```

- BeanFactory不会主动调用BeanFactory后处理器
- BeanFactory不会主动添加Bean后处理器
- BeanFactory不会主动初始化单例
- BeanFactory不会解析beanFactory 还不会解析 ${ } 与 #{ }
- bean后处理器会有排序的逻辑

```java
package com.thorough.beanFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;



public class BeanFactoryDemo02 {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AbstractBeanDefinition beanDefinition
                = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        Map<String, BeanFactoryPostProcessor> beansOfType
                = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beansOfType.values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);
        //  bean创建前就实例化好对象，而不是使用懒加载的形式
        beanFactory.preInstantiateSingletons();

        System.out.println("-------------<><>------------------------------");
        System.out.println(beanFactory.getBean(Bean1.class).getInter());

    }

    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
        @Bean
        public Bean3 bean3(){
            return new Bean3();
        }
        @Bean
        public Bean4 bean4(){
            return new Bean4();
        }

    }


    interface Inter{

    }

    static class Bean1{
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1() {
            log.info("Bean1 created");
        }
        @Autowired
        private Bean2 bean2;

        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;

        public Inter getInter() {
            return bean3;
        }
    }
    static class Bean2{
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2() {
            log.info("Bean2 created");
        }
    }
    static class Bean3 implements Inter{
        private static final Logger log = LoggerFactory.getLogger(Bean3.class);

        public Bean3() {
            log.info("Bean3 created");
        }
    }

    static class Bean4 implements Inter{
        private static final Logger log = LoggerFactory.getLogger(Bean4.class);

        public Bean4() {
            log.info("Bean4 created");
        }
    }

}
```



### 2.2、ApplicationContext的常见实现和用法

ClassPathXmlApplication

```java
package com.thorough.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationDemo01 {
    public static void main(String[] args) {
        testClassPathXMLApplication();
    }

    //  基于Classpath下xml格式的配置文件来创建
    private static void testClassPathXMLApplication() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationDemo01.xml");

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("name: " + beanDefinitionName);
        }

        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    static class Bean1 {
    }

    static class Bean2 {
        private Bean1 bean1;

        public Bean1 getBean1() {
            return bean1;
        }

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }
    }
}
```

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="bean1" class="com.thorough.application.ApplicationDemo01.Bean1"/>
    <bean id="bean2" class="com.thorough.application.ApplicationDemo01.Bean2">
        <property name="bean1" ref="bean1"/>
    </bean>
</beans>
```





### 2.3、内嵌容器、注册DispatcherServlet
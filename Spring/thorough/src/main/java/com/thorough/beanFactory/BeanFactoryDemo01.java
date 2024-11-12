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

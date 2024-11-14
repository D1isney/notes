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



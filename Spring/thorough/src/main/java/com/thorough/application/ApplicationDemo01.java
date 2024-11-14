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

package com.thorough;

import com.thorough.event.UserRegisterEvent;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class ThoroughApplication {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        ConfigurableApplicationContext run = SpringApplication.run(ThoroughApplication.class, args);

        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);

        ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
        Map<String, Object> o = (Map<String, Object>) singletonObjects.get(beanFactory);
//        o.forEach((k, v) -> {
//            System.out.println(k + " = " + v);
//        });

        o.entrySet().stream().filter(e -> e.getKey().startsWith("component")).forEach(
                e -> {
                    System.out.println(e.getKey() + " = " + e.getValue());
                }
        );
        String hi = run.getMessage("hi", null, Locale.CHINA);
        System.out.println(hi);

        Resource[] resources = run.getResources("classpath:application.properties");
        for (Resource resource : resources) {
            System.out.println(resource);
        }

        ConfigurableEnvironment environment = run.getEnvironment();
        String javaHome = environment.getProperty("java_home");
        String property = environment.getProperty("server.port");
        System.out.println(javaHome);
        System.out.println(property);


        run.publishEvent(new UserRegisterEvent(run));



    }

}

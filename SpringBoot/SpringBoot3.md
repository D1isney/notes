# Spring Boot 3

网址：https://docs.spring.io/spring-boot/docs/3.0.5/reference/html/

## 一、环境要求

| 环境&工具          | 版本（or later） |
| ------------------ | ---------------- |
| Spring Boot        | 3.0.4 +          |
| IDEA               | 2021.2.1 +       |
| Java               | 17 +             |
| Maven              | 3.5 +            |
| Tomcat             | 10.0 +           |
| Servlet            | 5.0 +            |
| GraalVM Community  | 22.3 +           |
| Native Build Tools | 0.9.19 +         |



## 二、Spring Boot是什么

Spring Boot 帮我们简单、快速地创建一个独立的、生产级别的Spring应用。

大多数Spring Boot应用只需要编写少量配置即可快速整合Spring平台以及第三方技术。

**特性：**

- <font style="color:red">快速创建</font>独立Spring应用
- 直接<font style="color:red">嵌入</font>Tomcat、Jetty Or Undertow（无需部署war包）
  - 以前：打成war包，Linux安装Java、Tomcat、MySQL等，将war包放到Tomcat的webapps下
  - 现在：打成jar包，Linux安装Java，java -jar启动即可
- 提供可选的<font style="color:red">starter</font>，简化应用整合
  - **场景启动器**：web、json、邮件、oss（对象存储）、异步、定时任务、缓存等等
  - 以前：导包一堆，控制版本号
  - 现在：为每一种场景准备了一个依赖坐标
- <font style="color:red">按需自动配置</font>Spring以及第三方库
  - 如果这个场景需要使用（生效），这个场景的所有配置都会自动配置好
  - **约定大约配置**：每个场景都有很多默认的配置
  - 自定义：配置文件中修改几项即可
- 提供<font style="color:red">生产级特性</font>：如监控指标、健康检查、外部化配置等
  - 监控指标、健康检查（K8S）、外部化配置
- 无代码生成、<font style="color:red">无XML</font>

总结：简化开发、简化配置、简化整合、简化部署、简化监控、简化运维。



## 三、快速体验

> 场景：浏览器发送/hello请求，返回“Hello,Spring Boot 3 !”

1. 创建一个Maven项目

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com.po</groupId>
       <artifactId>boot03-01-demo</artifactId>
       <version>1.0-SNAPSHOT</version>
   
       <properties>
           <maven.compiler.source>17</maven.compiler.source>
           <maven.compiler.target>17</maven.compiler.target>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
       </properties>
   
   <!--    所有Spring Boot项目都必须集成自 spring-boot-starter-parent-->
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>3.0.5</version>
       </parent>
       <dependencies>
   <!--        web开发的场景启动器-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

2. 创建一个启动类

   ```java
   package com.po;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   
   @SpringBootApplication
   public class Boot03_01_DemoApplication {
       public static void main(String[] args) {
           SpringApplication.run(Boot03_01_DemoApplication.class, args);
       }
   }
   ```

3. 创建一个Controller

   ```java
   package com.po.controller;
   
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.ResponseBody;
   
   @Controller
   public class HelloController {
       @ResponseBody
       @GetMapping("/hello")
       public String hello(){
           return "Hello,Spring Boot 3!";
       }
   }
   ```

4. 添加打包插件打包

   ```xml
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
                   <version>2.6.13</version>
               </plugin>
           </plugins>
       </build>
   ```

5. 测试

   > 访问：http://localhost:8080/hello

6. 打包

   把项目打包成可执行的jar包

   ```shell
   mvn clear package
   ```

   ```shell
   java -jar xxx.jar
   ```



## 四、
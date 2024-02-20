

# SpringMVC



# 1、SpringMVC简介

`Spring MVC属于SpringFrameWork的后续产品，已经融合在Spring Web Flow里面。Spring 框架提供了构建 Web 应用程序的全功能 MVC 模块。使用 Spring 可插入的 MVC 架构，从而在使用Spring进行WEB开发时，可以选择使用Spring的Spring MVC框架或集成其他MVC开发框架，如Struts1(现在一般不用)，Struts 2(一般老项目使用)等等。`

## 1.1、什么是MVC？

**Model（模型）：**数据模型，提供要展示的数据，因此包含数据和行为，可以认为是领域模型或JavaBean组件。

**View（视图）：**负责进行模型的展示，一般就是我们见到的用户界面，客户想看到的东西。

**Controller（控制器）：**接收用户请求，委托个模型进行处理（状态改变），处理完毕后把返回的模型数据返回给视图，由视图负责展示。也就是说控制器做了个调度员的工作。

## 1.2、SpringMVC的优点

1. 轻量级，简单易学
2. 高效 , 基于请求响应的MVC框架
3. 与Spring兼容性好，无缝结合
4. 约定优于配置
5. 功能强大：RESTful、数据验证、格式化、本地化、主题等
6. 简洁灵活

Spring的web框架围绕DispatcherServlet[ 调度Servlet ] 设计。

DispatcherServlet的作用是将请求分发到不同的处理器。从Spring 2.5开始，使用Java 5或者以上版本的用户可以采用基于注解形式进行开发，十分简洁；

正因为SpringMVC好 , 简单 , 便捷 , 易学 , 天生和Spring无缝集成(使用SpringIoC和Aop) , 使用约定优于配置 . 能够进行简单的junit测试 . 支持Restful风格 .异常处理 , 本地化 , 国际化 , 数据验证 , 类型转换 , 拦截器 等等......所以我们要学习 .



## 1.3、Servlet与SpringMVC

| 使用Servlet技术开发Web程序流程                 | 使用SpringMVC技术开发Web程序流程               |
| ---------------------------------------------- | ---------------------------------------------- |
| 1、创建web工程（Maven结构）                    | 1、创建web工程（Maven结构）                    |
| 2、设置tomcat服务器，加载Web工程（tomcat插件） | 2、设置tomcat服务器，加载Web工程（tomcat插件） |
| 3、导入坐标（Servlet）                         | 3、导入坐标（**SpringMVC**+Servlet）           |
| 4、定义处理请求的功能类（UserServlet）         | 4、定义处理请求的功能类（**UserController**）  |
| 5、设置请求映射（配置映射关系）                | 5、**设置请求映射（配置映射关系）**            |
|                                                | 6、**将SpringMVC设定加载到Tomcat容器中**       |





# 2、快速入门

1. 创建Maven项目

2. 导入依赖

   ```XML
       <!--    导入依赖-->
       <dependencies>
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.12</version>
           </dependency>
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-webmvc</artifactId>
               <version>5.1.9.RELEASE</version>
           </dependency>
           <dependency>
               <groupId>javax.servlet</groupId>
               <artifactId>servlet-api</artifactId>
               <version>2.5</version>
           </dependency>
           <dependency>
               <groupId>javax.servlet.jsp</groupId>
               <artifactId>jsp-api</artifactId>
               <version>2.2</version>
           </dependency>
           <dependency>
               <groupId>javax.servlet</groupId>
               <artifactId>jstl</artifactId>
               <version>1.2</version>
           </dependency>
       </dependencies>
   ```

3. Add Framework Support

4. 添加Web项目

5. 书写Servlet

6. 配置web.xml

7. 书写jsp

8. 配置Tomcat，并开启测试



## 2.1、第一个SpringMVC项目

**Servlet：**

```java
package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //  1.获取前端参数
        String method = req.getParameter("method");
        if(method.equals("add")){
            req.getSession().setAttribute("msg","执行了add方法");
        }
        if (method.equals("delete")){
            req.getSession().setAttribute("msg","执行了delete方法");
        }
        //  2.调用业务层

        //  3.视图转发或者重定向
        req.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

**Web.XML**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>com.servlet.HelloServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>

    <!--    设置Session的时间周期-->
    <!--    <session-config>-->
    <!--        <session-timeout>15</session-timeout>-->
    <!--    </session-config>-->

    <!--    配置默认页-->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

</web-app>
```

**调用接口：**

```markdown
    http://localhost:8080/hello?method=add
	http://localhost:8080/hello?method=delete
```



MVC框架要做哪些事情

1. 将URL映射到Java类或Java类的方法
2. 封装用户提交的数据
3. 处理请求 -- 调用相关的业务处理 -- 封装相应数据
4. 将响应的数据进行渲染 .jsp / html 等表示层数据



## 2.2、中心控制器【重点】

Spring的Web框架围绕DispatcherServlet设计。DispatcherServlet的作用是将请求分发到不同的处理器。从Spring2.5开始，使用Java5或者以上版本的用户可以采用基于注解的Controller声明方式。

SpringMVC框架像许多其他MVC框架一样，**以请求为驱动，围绕一个中心Servlet分派请求及提供其他功能，DispatcherServlet，是一个实际的Servlet（它继承自HttpServlet基类）。**

**Web.xml：**

```xml
   <!--1.注册DispatcherServlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--关联一个springmvc的配置文件:【servlet-name】-servlet.xml-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <!--启动级别-1-->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--/ 匹配所有的请求；（不包括.jsp）-->
    <!--/* 匹配所有的请求；（包括.jsp）-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

**SpringMVC-Servlet.xml：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--    添加 处理映射器-->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>

    <!--    添加 处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>

    <!--    添加 视图解析器-->
    <!--视图解析器:DispatcherServlet给他的ModelAndView-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
        <!--前缀-->
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <!--后缀-->
        <property name="suffix" value=".jsp"/>
    </bean>


    <!--Handler-->
    <bean id="/hello" class="com.controller.HelloController"/>
</beans>
```

**Controller：**

```java
package com.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//注意：这里我们先导入Controller接口
public class HelloController implements Controller {

   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
       //ModelAndView 模型和视图
       ModelAndView mv = new ModelAndView();

       //封装对象，放在ModelAndView中。Model
       mv.addObject("msg","HelloSpringMVC!");
       //封装要跳转的视图，放在ModelAndView中
       mv.setViewName("/hello"); //: /WEB-INF/jsp/hello.jsp
       return mv;
  }  
}
```



**可能遇到的问题：访问出现404**

1. 查看控制台输出，看看是否少了什么jar包
2. 如果jar包存在，显示无法输出，在IDEA项目中Artifacts，添加lib依赖
3. 重启Tomcat



**DispatcherServlet底层执行原理：**

1. Servlet容器配置：在Web应用启动时，Servlet容器（如Tomcat）加载并初始化DispatcherServlet。这通常通过在web.xml文件中配置Servlet映射来完成。
2. Servlet初始化：DispatcherServlet初始化时，会加载Spring应用上下文（ApplicationContext）。这个上下文包含了应用程序的所有配置信息，如控制器、拦截器、视图解析器等等。
3. 请求处理：当有HTTP请求进入应用时，DispatcherServlet拦截该请求。它根据请求的URL找到对应的Controller，并调用相关的处理方式。
4. Handler Mapping：DispatcherServlet使用Handler Mapper（处理器映射）来确定请求应该由哪个Controller处理。这个映射关系可以通过注解、XML配置或其他地方定义。
5. Handler Adapter：一旦找到了匹配的Controller，DispatcherServlet使用Handler Adapter（处理器适配器）来调用Controller的方法，并传递请求的参数。
6. 执行业务逻辑：Controller中的方法执行业务逻辑，可能涉及数据库操作、调用服务等。
7. 视图解析：执行完业务逻辑后，Controller返回一个逻辑视图名，DispatcherServlet使用View Resolver（视图解析器）来解析该逻辑视图名，得到实际的视图。
8. 渲染视图：DispatcherServlet将得到的视图渲染为最终的HTTP相应，返回给客户端。

总体而言，DispatcherServlet充当了请求的中央调度器，协调各个组件完成请求的处理过程。这种分层结构使得应用更易于扩展和维护。





## 2.3、使用注解创建SpringMVC

1. 新建Module
2. 导入SpringMVC的依赖
3. 配置web.xml，注册DispatcherServlet
4. 编写SpringMVC的配置文件
5. 添加 处理器映射器
6. 添加 处理器适配器
7. 添加 视图解析器
8. 编写Controller，添加注解@Controller



**web.xml：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!--1.注册DispatcherServlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--关联一个springmvc的配置文件:【servlet-name】-servlet.xml-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <!--启动级别-1-->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--/ 匹配所有的请求；（不包括.jsp）-->
    <!--/* 匹配所有的请求；（包括.jsp）-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```



**springmvc-servlet.xml：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <context:component-scan base-package="com.controller"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler />
    <!--
    支持mvc注解驱动
        在spring中一般采用@RequestMapping注解来完成映射关系
        要想使@RequestMapping注解生效
        必须向上下文中注册DefaultAnnotationHandlerMapping
        和一个AnnotationMethodHandlerAdapter实例
        这两个实例分别在类级别和方法级别处理。
        而annotation-driven配置帮助我们自动完成上述两个实例的注入。
     -->
    <mvc:annotation-driven />

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>

</beans>
```



**HelloController：**

```java
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/HelloController")
//  写这个 这个类就不会被视图解析器解析，返回的就是String
//@RestController
public class HelloController {

    //  真实访问地址：项目名/HelloController/hello
    @RequestMapping("/hello")
    public String hello(Model model) {
        //  封装数据
        //  向模型中添加msg与值，可以在jsp页面中取出并渲染
        model.addAttribute("msg","Hello,SpringMVCAnnotation！");
        //  web-inf/jsp/hello.jsp
        return "hello"; //  会被视图解析器处理；
    }
}
```



## 2.4、Controller的理解 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <context:component-scan base-package="com.controller"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler />
    <!--
    支持mvc注解驱动
        在spring中一般采用@RequestMapping注解来完成映射关系
        要想使@RequestMapping注解生效
        必须向上下文中注册DefaultAnnotationHandlerMapping
        和一个AnnotationMethodHandlerAdapter实例
        这两个实例分别在类级别和方法级别处理。
        而annotation-driven配置帮助我们自动完成上述两个实例的注入。
     -->
    <mvc:annotation-driven />

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>

    <bean name="/t1" class="com.controller.ControllerTest1"/>
</beans>
```

Controller常用的两种实现方式：

1. 实现Controller接口
2. 注解Controller

**实现Controller接口：**

```java
package com.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//  只要实现了Controller 接口的类，说明这就是一个控制器了
public class ControllerTest1 implements Controller {

    //	返回值是一个ModelAndView
    //	使用接口方式需要去XML注册Bean
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv = new ModelAndView();
        //	KV信息值
        mv.addObject("msg","ControllerTest1");
        //	设置视图
        mv.setViewName("test");
        return mv;
    }
}
```

**使用注解Controller：**

```java
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//  代表这个类会被Spring托管
@Controller
public class ControllerTest2 {

    @RequestMapping("/t2")
    public String test2(Model model){
        model.addAttribute("msg","ControllerTest2");

        return "test";
    }
}
```

使用注解，可以不用去注册Bean，@Controller就是让Spring去托管这个类，@RequestMapping注解用于映射url到控制器类或一个特定的处理程序方法。可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。

但是需要去XML扫描包

```XML
<!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
<context:component-scan base-package="com.controller"/>
```

# 3、RestFul风格

**概念：**

```markdown
    Restful就是一个资源定位及资源操作的风格。不是标准也不是协议，只是一种风格。基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存等机制。
```

资源：互联网所有的事务都可以被抽象为资源

四种资源操作方式：

1. Post：增加
2. Delete：删除
3. Put：修改
4. Get：查询

**传统方式操作资源：**通过不同的参数来实现不同的效果

```markdown
	http://127.0.0.1/item/queryItem.action?id=1 查询,GET

	http://127.0.0.1/item/saveItem.action 新增,POST

	http://127.0.0.1/item/updateItem.action 更新,POST

	http://127.0.0.1/item/deleteItem.action?id=1 删除,GET或POST
```

**RestFul操作资源：**可以通过不同的请求方式来实现不同的效果

```markdown
	http://127.0.0.1/item/1 查询,GET

	http://127.0.0.1/item 新增,POST

	http://127.0.0.1/item 更新,PUT

	http://127.0.0.1/item/1 删除,DELETE
```

**Java：**

```java
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestFulController {

    /**
     * http://localhost:8080/add1?a=1&b=2
     * 原来的方式
     */
    @RequestMapping("/add1")
    public String test1(int a, int b, Model model) {
        model.addAttribute("msg", "结果为：" + (a + b));
        return "test";
    }

    /**
     * http://localhost:8080/add2/1/2
     * RestFul风格
     */
    @RequestMapping("/add2/{a}/{b}")
    public String test2(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "结果为：" + (a + b));
        return "test";
    }

    /**
     * http://localhost:8080/add3/1/2
     * RestFul风格
     * <p>
     * 类型 状态报告
     * <p>
     * 消息 Request method 'GET' not supported
     * <p>
     * 描述 请求行中接收的方法由源服务器知道，但目标资源不支持
     */
    @RequestMapping(value = "/add3/{a}/{b}", method = RequestMethod.DELETE)
    /**
     * 等价于
     */
    //    @DeleteMapping(name = "/add3/{a}/{b}",)
    public String test3(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "DeleteMapping：" + (a + b));
        return "test";
    }



    //  上面是delete方式提交的，这里是Get方式提交的，同一个url，凡是执行了方式不同
    @GetMapping(value = "/add3/{a}/{b}")
    public String test4(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "GetMapping：" + (a + b));
        return "test";
    }

}
```

**使用路径变量的好处？**

- 使得路径变得更加简洁；
- 获得参数更加方便，框架会自动进行类型转换。
- 通过路径变量的类型可以约束访问参数，如果类型不一样，则访问不到对应的请求方法



**使用method属性指定请求类型**

用于约束请求的类型，可以收窄求情范围。指定请求类型。

```java
    /**
     * http://localhost:8080/add3/1/2
     * RestFul风格
     * <p>
     * 类型 状态报告
     * <p>
     * 消息 Request method 'GET' not supported
     * <p>
     * 描述 请求行中接收的方法由源服务器知道，但目标资源不支持
     */
    @RequestMapping(value = "/add3/{a}/{b}", method = RequestMethod.DELETE)
    /**
     * 等价于
     */
    //    @DeleteMapping(name = "/add3/{a}/{b}",)
    public String test3(@PathVariable int a, @PathVariable int b, Model model) {
        model.addAttribute("msg", "DeleteMapping：" + (a + b));
        return "test";
    }
```



# 4、转发&重定向

## 4.1、转发和重定向的定义

- **转发：**是指服务器接收到一个请求后，将请求转发给另一个资源进行处理，并将该资源的处理结果返回给客户端。在这个过程中，转发后的资源对客户端是不可见的，客户端只知道自己访问了一个资源，而不知道这个资源是被转发得到的。
- **重定向：**是指服务器接收到一个请求后，发现该请求需要访问另一个资源才能得到响应，于是告诉客户端重新发送一个请求，访问另一个资源。在这个过程中，客户端会重新发送一个请求，访问另一个资源。因此客户端会知道自己访问了两个资源。

## 4.2、转发和重定向的区别

1. 可见性
2. 速度 ----- 转发比重定向快
3. 资源路径
4. 请求次数
5. 客户端处理



## 4.3、通过SpringMVC来实现转发和重定向

**无视图解析器：**

```java
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ModelTest1 {
    @RequestMapping("/m1/t1")
    public String test1(HttpServletRequest request, HttpServletResponse response) {
        //  转发
        return "/index.jsp";
    }

    @RequestMapping("/m1/t2")
    public String test2(HttpServletRequest request, HttpServletResponse response) {
        //  转发二
        return "forward:/index.jsp";
    }

    @RequestMapping("/m1/t3")
    public String test3(HttpServletRequest request, HttpServletResponse response) {
        //  重定向
        return "redirect:/index.jsp";
    }
}
```

**有视图解析器：**

```xml
    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>
```

```java
    @RequestMapping("/m1/t4")
    public String test4(HttpServletRequest request, HttpServletResponse response) {
        //  转发
        return "test";
    }
    @RequestMapping("/m1/t5")
    public String test5(HttpServletRequest request, HttpServletResponse response) {
        //  重定向
        return "redirect:/test";
    }
```





## 4.4、总结

`转发和重定向都是网页跳转的方式，他们之间的区别主要在于可见性、速度、资源路径、请求次数和客户端处理等方面。转发对客户端是不可见的，速度比重定向快，可以访问相对路径和绝对路径的资源，只需要发送一次请求，客户端不需要进行任何处理。而重定向对客户端是可见的，速度较慢，只能访问绝对路径的资源，需要发送两次请求，客户端需要进行相应的处理。`



# 5、数据处理

## 5.1、提交的域名称和处理方法的参数一致

```java
@GetMapping("/t1")
public String test1(String name, Model model){
    //1.接受前端参数
    System.out.println(name);

    //2.将返回的结果传递给前端
    model.addAttribute("msg",name);

    //3.跳转视图
    return "test";
}
```

http://localhost:8080/t1?name=蛋蛋

后台输出：蛋蛋

## 5.2、提交的域名称和处理方法的参数不一致

```java
@GetMapping("/t2")
//  指定传进来的值的名字，userName匹配到name上
public String test2(@RequestParam("userName") String name, Model model){
    //1.接受前端参数
    System.out.println(name);

    //2.将返回的结果传递给前端
    model.addAttribute("msg",name);

    //3.跳转视图
    return "test";
}
```

http://localhost:8080/t1?userName=蛋蛋

后台输出：蛋蛋

## 5.3、提交一个对象

```java
package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private int age;
}
```

```java
// 前端接受的是一个对象:  id name age

/**
 * 1、接收前端用户传递的参数，判断参数的名字，假设名字直接在方法上，可以直接使用
 * 2、假设传递的是一个对象User，匹配User对象中的字段名；如果名字一致则OK，否则，匹配不到
 */
@GetMapping("/t3")
public String test3(User user,Model model){
    model.addAttribute("msg",user);
    return "test";
}
```

http://localhost:8080/t1?id=1&name=蛋蛋&age=22

后台输出：User(id=1, name=蛋蛋, age=22)



# 6、数据显示到前端

1. 通过ModelAndView

   ```java
   @GetMapping("/t5")
   public ModelAndView test5(HttpServletRequest servletRequest, HttpServletResponse servletResponse){
       ModelAndView mv = new ModelAndView();
       mv.addObject("msg","test5");
       mv.setViewName("test");
       return mv;
   }
   ```

2. 通过ModelMap

   ```java
       @GetMapping("/t4")
       public String test4(User user, ModelMap modelMap){
           modelMap.addAttribute("msg",user);
           return "test";
       }java
   ```

3. 通过Model

   ```java
   @GetMapping("/t6")
   public String test6(User user, Model model){
       model.addAttribute("msg",user);
       return "test";
   }
   ```



**对比：**

```markdown
* LinkedHashMap
* Model：只有寥寥几个方法只适合存储数据，简化了新手对于Model对象操作和理解；
* ModelMap：继承了LinkedHashMap，所以它拥有LinkedHashMap的方法和特性；
* ModelAndView：可以在存储数据的同时，可以进行设置返回的逻辑视图，进行控制展示层的跳转；
```





# 7、乱码问题

1. 在首页编写一个提交的表单

   ```jsp
   <%--
     Created by IntelliJ IDEA.
     User: qe249
     Date: 2023/11/21
     Time: 17:16
     To change this template use File | Settings | File Templates.
   --%>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
   <form action="/e/t1" method="post">
       <input type="text" name="name">
       <br>
       <input type="text">
       <br>
       <input type="submit">
   </form>
   </body>
   </html>
   ```

2. 后台对应的处理类

   ```java
   package com.controller;
   
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.web.bind.annotation.PostMapping;
   
   @Controller
   public class EncodingController {
       @PostMapping("e/t1")
       public String test1(String name, Model model){
           model.addAttribute("msg",name);
           return "test";
       }
   }
   ```

**经过测试：**

èè



**解决问题：**

**web.xml：**

```xml
   <!--
        SpringMVC提供
        解决乱码问题
    -->
    <filter>
        <filter-name>encoding1</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encoding1</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```



**自定义过滤器：**

```xml
<filter>
    <filter-name>encoding</filter-name>
    <filter-class>com.filter.GenericEncodingFilter</filter-class>
</filter>
 <filter-mapping>
     <filter-name>encoding</filter-name>
     <url-pattern>/*</url-pattern>
 </filter-mapping>
```

```java
package com.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
* 解决get和post请求 全部乱码的过滤器
*/
public class GenericEncodingFilter implements Filter {

   @Override
   public void destroy() {
  }

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       //处理response的字符编码
       HttpServletResponse myResponse=(HttpServletResponse) response;
       myResponse.setContentType("text/html;charset=UTF-8");

       // 转型为与协议相关对象
       HttpServletRequest httpServletRequest = (HttpServletRequest) request;
       // 对request包装增强
       HttpServletRequest myrequest = new MyRequest(httpServletRequest);
       chain.doFilter(myrequest, response);
  }

   @Override
   public void init(FilterConfig filterConfig) throws ServletException {
  }

}

//自定义request对象，HttpServletRequest的包装类
class MyRequest extends HttpServletRequestWrapper {

   private HttpServletRequest request;
   //是否编码的标记
   private boolean hasEncode;
   //定义一个可以传入HttpServletRequest对象的构造函数，以便对其进行装饰
   public MyRequest(HttpServletRequest request) {
       super(request);// super必须写
       this.request = request;
  }

   // 对需要增强方法 进行覆盖
   @Override
   public Map getParameterMap() {
       // 先获得请求方式
       String method = request.getMethod();
       if (method.equalsIgnoreCase("post")) {
           // post请求
           try {
               // 处理post乱码
               request.setCharacterEncoding("utf-8");
               return request.getParameterMap();
          } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
          }
      } else if (method.equalsIgnoreCase("get")) {
           // get请求
           Map<String, String[]> parameterMap = request.getParameterMap();
           if (!hasEncode) { // 确保get手动编码逻辑只运行一次
               for (String parameterName : parameterMap.keySet()) {
                   String[] values = parameterMap.get(parameterName);
                   if (values != null) {
                       for (int i = 0; i < values.length; i++) {
                           try {
                               // 处理get乱码
                               values[i] = new String(values[i]
                                      .getBytes("ISO-8859-1"), "utf-8");
                          } catch (UnsupportedEncodingException e) {
                               e.printStackTrace();
                          }
                      }
                  }
              }
               hasEncode = true;
          }
           return parameterMap;
      }
       return super.getParameterMap();
  }

   //取一个值
   @Override
   public String getParameter(String name) {
       Map<String, String[]> parameterMap = getParameterMap();
       String[] values = parameterMap.get(name);
       if (values == null) {
           return null;
      }
       return values[0]; // 取回参数的第一个值
  }

   //取所有值
   @Override
   public String[] getParameterValues(String name) {
       Map<String, String[]> parameterMap = getParameterMap();
       String[] values = parameterMap.get(name);
       return values;
  }
}
```





# 8、JSON

## 8.1、概念

`JSON（JavaScript Object Notation, JS对象简谱）是一种轻量级的数据交换格式。它基于 ECMAScript（European Computer Manufacturers Association, 欧洲计算机协会制定的js规范）的一个子集，采用完全独立于编程语言的文本格式来存储和表示数据。简洁和清晰的层次结构使得 JSON 成为理想的数据交换语言。 易于人阅读和编写，同时也易于机器解析和生成，并有效地提升网络传输效率。`

## 8.2、什么是JSON

JSON 是一种纯字符串形式的数据，它本身不提供任何方法（函数），非常适合在网络中进行传输。JavaScript、PHP、Java、Python、C++ 等编程语言中都内置了处理 JSON 数据的方法。

JSON 是基于 JavaScript（Standard ECMA-262 3rd Edition - December 1999）的一个子集，是一种开放的、轻量级的数据交换格式，采用独立于编程语言的文本格式来存储和表示数据，易于程序员阅读与编写，同时也易于计算机解析和生成，通常用于在 Web 客户端（浏览器）与 Web 服务器端之间传递数据。

在 JSON 中，使用以下两种方式来表示数据：

- Object（对象）：键/值对（名称/值）的集合，使用花括号`{ }`定义。在每个键/值对中，以键开头，后跟一个冒号`:`，最后是值。多个键/值对之间使用逗号`,`分隔，例如`{"name":"Disney","url":"http://www.xx.com"}`；
- Array（数组）：值的有序集合，使用方括号`[ ]`定义，数组中每个值之间使用逗号`,`进行分隔。

```json
{
    "Name":"Disney",
    "Url":"http://www.xx.com/",
    "Tutorial":"JSON",
    "Article":[
        "JSON 是什么？",
        "JSONP 是什么？",
        "JSON 语法规则"
    ]
}
```



## 8.3、为什么要使用JSON？

JSON 并不是唯一能够实现在互联网中传输数据的方式，除此之外还有一种 XML 格式。JSON 和 XML 能够执行许多相同的任务，那么我们为什么要使用 JSON，而不是 XML 呢？

之所以使用 JSON，最主要的原因是 JavaScript。众所周知，JavaScript 是 Web 开发中不可或缺的技术之一，而 JSON 是基于 JavaScript 的一个子集，JavaScript 默认就支持 JSON，而且只要您学会了 JavaScript，就可以轻松地使用 JSON，不需要学习额外的知识。

另一个原因是 JSON 比 XML 的可读性更高，而且 JSON 更加简洁，更容易理解。

与 XML 相比，JSON 具有以下优点：

- 结构简单、紧凑：与 XML 相比，JSON 遵循简单、紧凑的风格，有利于程序员编辑和阅读，而 XML 相对比较复杂；
- 更快：JSON 的解析速度比 XML 更快（因为 XML 与 HTML 很像，在解析大型 XML 文件时需要消耗额外的内存），存储同样的数据，JSON 格式所占的存储空间更小；
- 可读性高：JSON 的结构有利于程序员阅读。



## 8.4、JSON的不足

任何事物都不可能十全十美，JSON 也不例外，比如：

- 只有一种数字类型：JSON 中只支持 IEEE-754 双精度浮点格式，因此您无法使用 JSON 来存储许多编程语言中多样化的数字类型；
- 没有日期类型：在 JSON 中您只能通过日期的字符串（例如：1970-01-01）或者时间戳（例如：1632366361）来表示日期；
- 没有注释：在 JSON 中无法添加注释；
- 冗长：虽然 JSON 比 XML 更加简洁，但它并不是最简洁的数据交换格式，对于数据量庞大或用途特殊的服务，您需要使用更加高效的数据格式。

## 8.5、JSON 应该如何存储？

JSON 数据可以存储在 .json 格式的文件中（与 .txt 格式类似，都属于纯文本文件），也可以将 JSON 数据以字符串的形式存储在数据库、Cookie、Session 中。

要使用存储好的 JSON 数据也非常简单，不同的编程语言中提供了不同的方法来检索和解析 JSON 数据，例如 JavaScript 中的 JSON.parse() 和 JSON.stringify()、PHP 中的 json_decode() 和 json_encode()。

## 8.6、什么时候会使用 JSON

简单了解了 JSON 之后，我们再来看看什么时候适合使用 JSON。

#### 1) 定义接口

JSON 使用最多的地方莫过于 Web 开发领域了，现在的数据接口基本上都是返回 JSON 格式的数据，比如：

- 使用 Ajax 异步加载的数据；
- RPC 远程调用；
- 前后端分离，后端返回的数据；
- 开发 API，例如百度、高德的一些开放接口。


这些接口一般都会提供一个接口文档，说明接口调用的方法、需要的参数以及返回数据的介绍等。

#### 2) 序列化

程序在运行时所有的变量都是存储在内存中的，如果程序重启或者服务器宕机，这些数据就会丢失。一般情况下运行时变量并不是很重要，丢了就丢了，但有些数据则需要保存下来，供下次程序启动或其它程序使用。

我们可以将这些数据保存到数据库中，也可以保存到一个文件中，这个将内存中数据保存起来的过程称为序列化。序列化在 Python 中称为 pickling，在其他语言中也被称为 serialization、marshalling、flattening 等等，都是一个意思。

通常情况下，序列化是将程序中的对象直接转换为可保存或者可传输的数据，但这样会保存对象的类型信息，无法做到跨语言使用，例如我们使用 Python 将数据序列化到硬盘，然后使用 Java 来读取这份数据，这时由于不同编程语言的数据类型不同，就会造成读取失败。如果在序列化之前，先将对象信息转换为 JSON 格式，则不会出现此类问题。

#### 3) 生成 Token

Token 的形式多种多样，JSON、字符串、数字等都可以用来生成 Token，JSON 格式的 Token 最有代表性的是 [JWT](https://jwt.io/introduction)（JSON Web Tokens）。

随着技术的发展，分布式 Web 应用越来越普及，通过 Session 管理用户登录状态的成本越来越高，因此慢慢发展为使用 Token 做登录身份校验，然后通过 Token 去取 Redis 中缓存的用户信息。随着之后 JWT 的出现，校验方式变得更加简单便捷，无需再通过 Redis 缓存，而是直接根据 Token 读取保存的用户信息。

#### 4) 配置文件

我们还可以使用 JSON 来作为程序的配置文件，最具代表型的是 [npm](https://www.npmjs.com/)（Node.js 的包管理工具）的 package.json 包管理配置文件，如下所示：

```json
{
    "name": "server",
    "version": "0.0.0",
    "private": true,
    "main": "server.js",
    "scripts": {
        "start": "node ./bin/www"
    },
    "dependencies": {
        "cookie-parser": "~1.4.3",
        "debug": "~2.6.9",
        "express": "~4.16.0",
        "http-errors": "~1.6.2",
        "jade": "~1.11.0",
        "morgan": "~1.9.0"
    }
}
```

> 提示：虽然 JSON 可以用来定义配置文件，但由于 JSON 中不能添加注释，使得配置文件的可读性较差。



## 8.7、JSON和JavaScript对象互转

- 要实现从JSON字符串转换为JavaScript 对象，使用 JSON.parse() 方法：

  ```js
  var obj = JSON.parse('{"a": "Hello", "b": "World"}');
  //结果是 {a: 'Hello', b: 'World'}
  ```

- 要实现从JavaScript对象转换为JSON字符串，使用JSON.stringify()方法；

  ```js
  var json = JSON.stringify({a: 'Hello', b: 'World'});
  //结果是 '{"a": "Hello", "b": "World"}'
  ```

- 测试

  ```json
          // 编写一个JavaScript对象
          let user = {
              name: "蛋蛋",
              age: 23,
              sex: '男'
          }
          //  将js对象转换为JSON对象
          let json = JSON.stringify(user);
  
          //  将JSON对象转换为js对象
          let js = JSON.parse(json);
  
          console.log(json);
          console.log(js);
  ```



## 8.8、Java中返回一个JSON对象

### 1）使用Jackson

```xml
<!-- 
	导入Jackson包
-->
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.9.8</version>
</dependency>
```

**配置web.xml**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!--1.注册servlet-->
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--通过初始化参数指定SpringMVC配置文件的位置，进行关联-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <!-- 启动顺序，数字越小，启动越早 -->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--所有请求都会被springmvc拦截 -->
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

    <filter>
        <filter-name>encoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encoding</filter-name>
        <url-pattern>/</url-pattern>
    </filter-mapping>
</web-app>
```

**SpringMVC.xml**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <context:component-scan base-package="com.controller"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler />

    <mvc:annotation-driven />

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>

    <!--
        JSON乱码问题配置
    -->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
</beans>
```

**POJO：**

```java
package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private int age;
    private String sex;
}
```

**Controller：**

```java
package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pojo.User;
import com.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Controller
//  不走视图解析器
@RestController
public class UserController {


    @RequestMapping("/json1")
    //  解决编码错误的问题
    //    @RequestMapping(value = "/json1",produces = "application/json;charset=utf-8")
//    @ResponseBody //不会走视图解析器，会直接返回一个字符串
    public String json1() throws JsonProcessingException {

        //  创建一个对象
        User user = new User("Disney", 22, "男");

        //  jackson , ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(user);

        return str;
    }


    /**
     * 返回集合
     */
    @RequestMapping("/json2")
//    @ResponseBody
    public String json2() throws JsonProcessingException {

        List<User> userList = new ArrayList<>();
        //  创建多个对象
        User user1 = new User("Disney1", 22, "男");
        User user2 = new User("Disney2", 22, "男");
        User user3 = new User("Disney3", 22, "男");
        User user4 = new User("Disney4", 22, "男");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        //  jackson , ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(userList);

        return JsonUtils.getJson(userList);
        /**
         *
         * [
         *  {"name":"Disney1","age":22,"sex":"男"},
         *  {"name":"Disney2","age":22,"sex":"男"},
         *  {"name":"Disney3","age":22,"sex":"男"},
         *  {"name":"Disney4","age":22,"sex":"男"}
         * ]
         *
         */
    }


    @RequestMapping("/json3")
    public String json3() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        //  Timestamp  时间戳  默认格式

        //  第一种
        //  自定义的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        //  2023-11-21 22:17:13


        //  第二种
        //  使用ObjectMapper 来格式化
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //  自行注入格式
        mapper.setDateFormat(format);

        return mapper.writeValueAsString(date);
    }
}
```

**拓展：**

封装JSON类

```java
package com.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

public class JsonUtils {

    /**
     *
     * @param object
     * @param dateFormat
     * @return
     */
    public static String getJson(Object object,String dateFormat){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        mapper.setDateFormat(format);
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param object
     * @return
     */
    public static String getJson(Object object){
        return getJson(object,"yyyy-MM-dd HH:mm:ss");
    }
}
```



### 2）FastJson

`fastjson.jar是阿里开发的一款专门用于Java开发的包，可以方便的实现json对象与JavaBean对象的转换，实现JavaBean对象与json字符串的转换，实现json对象与json字符串的转换。实现json的转换方法很多，最后的实现结果都是一样的。`

> FastJson是开源的，Github地址:https://github.com/alibaba/fastjson。
>
> pom依赖：

```xml
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>fastjson</artifactId>
   <version>1.2.60</version>
</dependency>
```

**JSONObject  代表 json 对象** 

- JSONObject实现了Map接口, 猜想 JSONObject底层操作是由Map实现的。
- JSONObject对应json对象，通过各种形式的get()方法可以获取json对象中的数据，也可利用诸如size()，isEmpty()等方法获取"键：值"对的个数和判断是否为空。其本质是通过实现Map接口并调用接口中的方法完成的。

**JSONArray  代表 json 对象数组**

- 内部是有List接口中的方法来完成操作的。

**JSON代表 JSONObject和JSONArray的转化**

- JSON类源码分析与使用
- 仔细观察这些方法，主要是实现json对象，json对象数组，javabean对象，json字符串之间的相互转化。



```java
@RequestMapping("/json4")
public String json4() throws JsonProcessingException {
    
    List<User> userList = new ArrayList<>();
    //  创建多个对象
    User user1 = new User("Disney1", 22, "男");
    User user2 = new User("Disney2", 22, "男");
    User user3 = new User("Disney3", 22, "男");
    User user4 = new User("Disney4", 22, "男");
    userList.add(user1);
    userList.add(user2);
    userList.add(user3);
    userList.add(user4);

    String str = JSON.toJSONString(userList);

    return str;
}
```





## 8.9、总结

JSON是一种轻量级的数据交换格式，它是基于JavaScript的一个子集，采用完全独立于编程语言的格式来表示数据，可以跨语言、跨平台使用。简洁清晰的层次结构使得JSON逐渐替代了XML，成为了理想的数据交换格式，广泛应用于Web开发领域。





# 9、整合SSM

> **建表：**

```sql
CREATE TABLE `books` (
`bookID` INT(10) NOT NULL AUTO_INCREMENT COMMENT '书id',
`bookName` VARCHAR(100) NOT NULL COMMENT '书名',
`bookCounts` INT(11) NOT NULL COMMENT '数量',
`detail` VARCHAR(200) NOT NULL COMMENT '描述',
KEY `bookID` (`bookID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT  INTO `books`(`bookID`,`bookName`,`bookCounts`,`detail`)VALUES
(1,'Java',1,'从入门到放弃'),
(2,'MySQL',10,'从删库到跑路'),
(3,'Linux',5,'从进门到进牢');
```

> 构建环境

1. 新建项目，添加Web支持

2. 导入Pom依赖

   ```XML
   <dependencies>
      <!--Junit-->
      <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.12</version>
      </dependency>
      <!--数据库驱动-->
      <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <version>8.0.11</version>
      </dependency>
      <!-- 数据库连接池 -->
      <dependency>
          <groupId>com.mchange</groupId>
          <artifactId>c3p0</artifactId>
          <version>0.9.5.2</version>
      </dependency>
   
      <!--Servlet - JSP -->
      <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>servlet-api</artifactId>
          <version>2.5</version>
      </dependency>
      <dependency>
          <groupId>javax.servlet.jsp</groupId>
          <artifactId>jsp-api</artifactId>
          <version>2.2</version>
      </dependency>
      <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>jstl</artifactId>
          <version>1.2</version>
      </dependency>
   
      <!--Mybatis-->
      <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis</artifactId>
          <version>3.5.2</version>
      </dependency>
      <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis-spring</artifactId>
          <version>2.0.2</version>
      </dependency>
   
      <!--Spring-->
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-webmvc</artifactId>
          <version>5.1.9.RELEASE</version>
      </dependency>
      <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-jdbc</artifactId>
          <version>5.1.9.RELEASE</version>
      </dependency>
   </dependencies>
   ```

3. Maven资源过滤设置

   ```XML
   <build>
      <resources>
          <resource>
              <directory>src/main/java</directory>
              <includes>
                  <include>**/*.properties</include>
                  <include>**/*.xml</include>
              </includes>
              <filtering>false</filtering>
          </resource>
          <resource>
              <directory>src/main/resources</directory>
              <includes>
                  <include>**/*.properties</include>
                  <include>**/*.xml</include>
              </includes>
              <filtering>false</filtering>
          </resource>
      </resources>
   </build>
   ```

4. 建立基本结构和配置框架

   - com.pojo

   - com.dao

   - com.service

   - com.controller

   - mybatis-config.xml

     ```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
     <configuration>
     
     </configuration>
     ```

   - applicationContext.xml

     ```XML
     <?xml version="1.0" encoding="UTF-8"?>
     <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
     
     </beans>
     ```

------

> Mybatis层编写

1. 数据库配置文件database.properties

   > 如果使用的是MySQL8.0+，增加一个时区的配置  serverTimezone=Asia/Shanghai 
   >
   > useSSL：安全连接

   ```properties
   driver = com.mysql.cj.jdbc.Driver
   url = jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&allowMultiQueries=true&useSSL=true&serverTimezone=Asia/Shanghai&useSSL=true
   username = root
   password = 123456
   ```

2. IDEA关联数据库

3. 编写Mybatis的核心配置文件

   ```XML
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
      
      <typeAliases>
          <package name="com.pojo"/>
      </typeAliases>
      <mappers>
          <mapper resource="com/dao/BookMapper.xml"/>
      </mappers>
   
   </configuration>
   ```

4. 编写Books实体类

   > 使用了lombok插件

   ```java
   package com.pojo;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class Books {
       private int bookID;
       private String bookName;
       private int bookCounts;
       private String detail;
   }
   ```

5. 编写Dao层的Mapper接口

   ```java
   package com.dao;
   
   import com.pojo.Books;
   import org.apache.ibatis.annotations.Param;
   
   import java.util.List;
   
   public interface BookMapper {
       //  增加一本书
       int addBook(Books books);
   
       //  删除一本书
       int deleteBookById(@Param("bookId") int id);
   
       //  更新一本书
       int updateBook(Books books);
   
       //  查询一本书
       Books queryBookById(@Param("bookId") int id);
   
       //  查询全部书
       List<Books> queryAllBook();
   }
   ```

   

6. 编写接口对应的XML文件

   ```XML
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.dao.BookMapper">
       <insert id="addBook" parameterType="Books">
           insert into mybatis.books (bookName, bookCounts, detail)
           values (#{bookName}, #{bookCounts}, #{detail})
       </insert>
   
       <delete id="deleteBookById" parameterType="_int">
           delete
           from mybatis.books
           where bookID = #{bookId}
       </delete>
   
       <update id="updateBook" parameterType="Books">
           update mybatis.books
           set bookName   = #{bookName},
               bookCounts = #{bookCounts},
               detail=#{detail}
           where bookID = #{bookID}
       </update>
   
       <select id="queryBookById" resultType="Books">
           select * from mybatis.books where bookId = #{bookId}
       </select>
   
       <select id="queryAllBook" resultType="Books">
           select * from mybatis.books
       </select>
   </mapper>
   ```

7. 编写Service层

   - 接口

     ```java
     package com.service;
     
     import com.pojo.Books;
     import org.apache.ibatis.annotations.Param;
     
     import java.util.List;
     
     public interface BookService {
     
         //  增加一本书
         int addBook(Books books);
     
         //  删除一本书
         int deleteBookById( int id);
     
         //  更新一本书
         int updateBook(Books books);
     
         //  查询一本书
         Books queryBookById(int id);
     
         //  查询全部书
         List<Books> queryAllBook();
     }
     ```

   - 实现类

     ```java
     package com.service;
     
     import com.dao.BookMapper;
     import com.pojo.Books;
     
     import java.util.List;
     
     public class BookServiceImpl implements BookService{
     
         // Service调Dao层
         private BookMapper bookMapper;
         public void setBookMapper(BookMapper bookMapper) {
             this.bookMapper = bookMapper;
         }
     
         @Override
         public int addBook(Books books) {
             return bookMapper.addBook(books);
         }
     
         @Override
         public int deleteBookById(int id) {
             return bookMapper.deleteBookById(id);
         }
     
         @Override
         public int updateBook(Books books) {
             return bookMapper.updateBook(books);
         }
     
         @Override
         public Books queryBookById(int id) {
             return bookMapper.queryBookById(id);
         }
     
         @Override
         public List<Books> queryAllBook() {
             return bookMapper.queryAllBook();
         }
     }
     ```

------

> Spring层

1. 配置Spring整合Mybatis

2. spring-dao.xml

   ```XML
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          https://www.springframework.org/schema/context/spring-context.xsd">
   
       <!-- 配置整合mybatis -->
       <!-- 1.关联数据库文件 -->
       <context:property-placeholder location="classpath:database.properties"/>
   
       <!-- 2.数据库连接池 -->
       <!--数据库连接池
           dbcp 半自动化操作 不能自动连接
           c3p0 自动化操作（自动加载配置文件 并且设置到对象里面）
       -->
   <!--    <bean id="c3p0DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">-->
   <!--        &lt;!&ndash; 配置连接池属性 &ndash;&gt;-->
   <!--        <property name="driverClass" value="${driver}"/>-->
   <!--        <property name="jdbcUrl" value="${url}"/>-->
   <!--        <property name="user" value="${username}"/>-->
   <!--        <property name="password" value="${password}"/>-->
   
   <!--        &lt;!&ndash; c3p0连接池的私有属性 &ndash;&gt;-->
   <!--        <property name="maxPoolSize" value="30"/>-->
   <!--        <property name="minPoolSize" value="1"/>-->
   <!--        &lt;!&ndash; 关闭连接后不自动commit &ndash;&gt;-->
   <!--        <property name="autoCommitOnClose" value="false"/>-->
   <!--        &lt;!&ndash; 获取连接超时时间 &ndash;&gt;-->
   <!--        <property name="checkoutTimeout" value="3000"/>-->
   <!--        &lt;!&ndash; 当获取连接失败重试次数 &ndash;&gt;-->
   <!--        <property name="acquireRetryAttempts" value="2"/>-->
   <!--    </bean>-->
   
       <bean id="datasource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
           <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
           <property name="url"
                     value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true&amp;allowMultiQueries=true&amp;useSSL=true&amp;serverTimezone=Asia/Shanghai&amp;useSSL=false"/>
           <property name="username" value="root"/>
           <property name="password" value="123456"/>
       </bean>
   
       <!-- 3.配置SqlSessionFactory对象 -->
       <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
           <!-- 注入数据库连接池 -->
           <property name="dataSource" ref="datasource"/>
           <!-- 配置Mybatis全局配置文件:mybatis-config.xml -->
           <property name="configLocation" value="classpath:mybatis-config.xml"/>
       </bean>
   
       <!-- 4.配置扫描Dao接口包，动态实现Dao接口注入到spring容器中 -->
       <!--解释 ：https://www.cnblogs.com/jpfss/p/7799806.html-->
       <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
           <!-- 注入sqlSessionFactory -->
           <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
           <!-- 给出需要扫描Dao接口包 -->
           <property name="basePackage" value="com.dao"/>
       </bean>
   
   </beans>
   ```

3. Spring整合Service层

   ```XML
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
      http://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/context
      http://www.springframework.org/schema/context/spring-context.xsd">
   
       <!-- 扫描service相关的bean -->
       <context:component-scan base-package="com.service" />
   
       <!--BookServiceImpl注入到IOC容器中-->
       <bean id="BookServiceImpl" class="com.service.BookServiceImpl">
           <property name="bookMapper" ref="bookMapper"/>
       </bean>
   
       <!-- 配置事务管理器 -->
       <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
           <!-- 注入数据库连接池 -->
           <property name="dataSource" ref="datasource" />
       </bean>
   
   
   </beans>
   ```



> SpringMCV层

1. web.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
   
       <!--DispatchServlet-->
       <servlet>
           <servlet-name>springmvc</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:spring-mvc.xml</param-value>
           </init-param>
           <load-on-startup>1</load-on-startup>
       </servlet>
       <servlet-mapping>
           <servlet-name>springmvc</servlet-name>
           <url-pattern>/</url-pattern>
       </servlet-mapping>
       
   
       <!--乱码过滤-->
       <filter>
           <filter-name>encodingFilter</filter-name>
           <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
           <init-param>
               <param-name>encoding</param-name>
               <param-value>utf-8</param-value>
           </init-param>
       </filter>
       <filter-mapping>
           <filter-name>encodingFilter</filter-name>
           <url-pattern>/*</url-pattern>
       </filter-mapping>
   
   </web-app>
   ```

2. spring-mvc.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/mvc
           http://www.springframework.org/schema/mvc/spring-mvc.xsd">
   
       <!--1.注解驱动-->
       <mvc:annotation-driven/>
       <!--2.静态资源过滤-->
       <mvc:default-servlet-handler/>
       <!--3.扫描包：Controller-->
       <context:component-scan base-package="com.controller"/>
       <!--4.视图解析器-->
       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
           <property name="prefix" value="/WEB-INF/jsp"/>
           <property name="suffix" value=".jsp"/>
       </bean>
   
   </beans>
   ```

3. Spring配置整合文件，applicationContext.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd">
   
   
       <import resource="classpath:Spring-dao.xml"/>
       <import resource="classpath:spring-service.xml"/>
       <import resource="classpath:spring-mvc.xml"/>
   
   </beans>
   ```



> Controller和视图层

1. BookController类

   ```java
   package com.controller;
   
   import com.pojo.Books;
   import com.service.BookService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.beans.factory.annotation.Qualifier;
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestMapping;
   
   import java.util.List;
   
   @Controller
   @RequestMapping("/book")
   public class BookController {
   
       /*
        * Controller 调 Service 层
        * */
       @Autowired
       @Qualifier("BookServiceImpl")
       private BookService bookService;
   
       //  查新全部书籍，并且返回到一个书籍展示页面
       @RequestMapping("/allBook")
       public String list(Model model) {
           List<Books> list = bookService.queryAllBook();
           model.addAttribute("list", list);
           return "allBook";
       }
   
   
       //  跳转到增加书籍页面
       @RequestMapping("/toAddBook")
       public String toAddPage() {
           return "addBook";
       }
   
       //  添加书籍的请求
       @RequestMapping("/addBook")
       public String addBook(Books books) {
           bookService.addBook(books);
           //  重定向
           return "redirect:/book/allBook";
       }
   
   
       //  跳转到修改页面
       @RequestMapping("/tuUpdateBook")
       public String toUpdatePage(int id,Model model){
           Books books = bookService.queryBookById(id);
           model.addAttribute("Books",books);
           return "updateBook";
       }
   
       //  数据修改
       @RequestMapping("/updateBook")
       public String updateBoo(Books books){
           bookService.updateBook(books);
           return "redirect:/book/allBook";
       }
   
       @RequestMapping("/deleteBook/{bookID}")
       public String deleteBook(@PathVariable("bookID") int id){
           bookService.deleteBookById(id);
           return "redirect:/book/allBook";
       }
   }
   ```

2. 首页index.jsp

   ```jsp
   <%--
     Created by IntelliJ IDEA.
     User: qe249
     Date: 2023/11/21
     Time: 23:09
     To change this template use File | Settings | File Templates.
   --%>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>$Title$</title>
   </head>
   <style>
       h3 {
           width: 180px;
           height: 38px;
           margin: 100px auto;
           text-align: center;
           line-height: 38px;
         background-color: deepskyblue;
         border-radius: 10px;
       }
   
       a {
           text-decoration: none;
           color: #ccc;
           font-size: 18px;
       }
   </style>
   <body>
   <h3>
       <a href="${pageContext.request.contextPath}/book/allBook">进入书籍页面</a>
   </h3>
   </body>
   </html>
   ```

3. 书籍列表首页 allBook.jsp

   ```jsp
   <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   <%--
     Created by IntelliJ IDEA.
     User: qe249
     Date: 2023/11/23
     Time: 21:21
     To change this template use File | Settings | File Templates.
   --%>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>书籍展示页面</title>
       <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
   </head>
   <body>
   <div class="container">
       <div class="row_clearfix">
           <div class="col-ma-12 column">
               <div class="page-header">
                   <h1>
                       <small>书籍列表 ------ 显示所有数据</small>
                   </h1>
               </div>
           </div>
   
           <div class="row">
               <div class="col-md-4 column">
                   <a class="btn btn-primary" href="${pageContext.request.contextPath}/book/toAddBook">新增书籍</a>
               </div>
           </div>
       </div>
   
       <div class="row_clearfix">
           <div class="col-ma-12 column">
               <table class="table table-hover table-striped">
                   <thead>
                       <tr>
                           <th>书籍编号</th>
                           <th>书籍名称</th>
                           <th>书籍数量</th>
                           <th>书籍描述</th>
                           <th>操作</th>
                       </tr>
                   </thead>
   
                   <tbody>
                       <c:forEach var="book" items="${list}">
                           <tr>
                               <td>${book.bookID}</td>
                               <td>${book.bookName}</td>
                               <td>${book.bookCounts}</td>
                               <td>${book.detail}</td>
                               <td>
                                   <a href="${pageContext.request.contextPath}/book/tuUpdateBook?id=${book.bookID}">修改</a>
                                   &nbsp; | &nbsp;
                                   <a href="${pageContext.request.contextPath}/book/deleteBook?${book.bookID}">删除</a>
                               </td>
                           </tr>
                       </c:forEach>
                   </tbody>
               </table>
           </div>
       </div>
   </div>
   </body>
   </html>
   ```

4. 添加书籍页面addBook.jsp

   ```jsp
   <%--
     Created by IntelliJ IDEA.
     User: qe249
     Date: 2023/11/24
     Time: 13:55
     To change this template use File | Settings | File Templates.
   --%>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>增加书籍</title>
       <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
   </head>
   <body>
   <div class="container">
       <div class="row_clearfix">
           <div class="col-ma-12 column">
               <div class="page-header">
                   <h1>
                       <small>新增书籍</small>
                   </h1>
               </div>
           </div>
       </div>
   
       <form action="${pageContext.request.contextPath}/book/addBook" method="post">
           <div class="form-group">
               <label >书籍名称</label>
               <input type="text" name="bookName" class="form-control" required>
           </div>
           <div class="form-group">
               <label >书籍数量</label>
               <input type="text" name="bookCounts" class="form-control" required>
           </div>
           <div class="form-group">
               <label >书籍描述</label>
               <input type="text" name="detail" class="form-control" required>
           </div>
           <div class="form-group">
               <input type="submit" class="form-control" value="添加">
           </div>
       </form>
   
   </div>
   </body>
   </html>
   ```

5. 修改书籍页面updateBook.jsp

   ```jsp
   <%--
     Created by IntelliJ IDEA.
     User: qe249
     Date: 2023/11/25
     Time: 22:47
     To change this template use File | Settings | File Templates.
   --%>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>修改书籍</title>
       <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
   </head>
   <body>
   <div class="container">
       <div class="row_clearfix">
           <div class="col-ma-12 column">
               <div class="page-header">
                   <h1>
                       <small>修改书籍</small>
                   </h1>
               </div>
           </div>
       </div>
   
       <form action="${pageContext.request.contextPath}/book/updateBook" method="post">
           <div class="form-group">
               <label>书籍名称</label>
               <input type="text" name="bookID" class="form-control" value="${Books.bookID}" readonly required>
           </div>
           <div class="form-group">
               <label>书籍名称</label>
               <input type="text" name="bookName" class="form-control" value="${Books.bookName}" required>
           </div>
           <div class="form-group">
               <label>书籍数量</label>
               <input type="text" name="bookCounts" class="form-control" value="${Books.bookCounts}" required>
           </div>
           <div class="form-group">
               <label>书籍描述</label>
               <input type="text" name="detail" class="form-control" value="${Books.detail}" required>
           </div>
           <div class="form-group">
               <input type="submit" class="form-control" value="修改">
           </div>
       </form>
   
   </div>
   </body>
   </html>
   ```

   



# 10、Ajax

## 10.1、ajax介绍

`Ajax即Asynchronous Javascript And XML（异步JavaScript和XML）在 2005年被Jesse James Garrett提出的新术语，用来描述一种使用现有技术集合的‘新’方法，包括: HTML 或 XHTML, CSS, JavaScript, DOM, XML, XSLT, 以及最重要的XMLHttpRequest。 使用Ajax技术网页应用能够快速地将增量更新呈现在用户界面上，而不需要重载（刷新）整个页面，这使得程序能够更快地回应用户的操作。 `



## 10.2、JQuery

> https://jquery.com/

使用**jquery**实现**Ajax**请求：

Ajax用于无需刷新整个页面而进行浏览器与服务器的通信，服务器将不再返回整个页面，而是返回部分数据，通过JavaScript的DOM操作对节点进行更新。数据传输格式有xml、json等格式，但常用的是json格式。
我们可以使用JavaScript的对象XMLHttpRequest来实现原生Ajax，但这种方法比较复杂，不易编写。

$.ajax()

```markdown
    ①url：链接地址，字符串表示
    ②data：(可选) 要发送给服务器的数据，GET与POST都可以，将自动转换为请求字符串格式，以Key/value的键值对形式表示，会做为QueryString附加到请求URL中，格式为{A: ‘…’, B: ‘…’}
    ③type：“POST” 或 “GET”，请求类型
    ④timeout：请求超时时间，单位为毫秒，数值表示
    ⑤cache：是否缓存请求结果，bool表示
    ⑥contentType：内容类型，默认为"application/x-www-form-urlencoded"
    ⑦dataType：服务器响应的数据类型，字符串表示；当填写为json时，回调函数中无需再对数据反序列化为json
    ⑧success：请求成功后，服务器回调的函数
    ⑨error：请求失败后，服务器回调的函数
    ⑩complete：请求完成后调用的函数，无论请求是成功还是失败，都会调用该函数；如果设置了success与error函数，则该函数在它们之后被调用
    ⑪async：是否异步处理，bool表示，默认为true；设置该值为false后，JS不会向下执行，而是原地等待服务器返回数据，并完成相应的回调函数后，再向下执行
    ⑫username：访问认证请求中携带的用户名，字符串表示
    ⑬password：返回认证请求中携带的密码，字符串表示
```



## 10.3、简单的使用

**web.xml：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    
    <filter>
        <filter-name>encoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encoding</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

**编写一个AjaxController类：**

```java
package com.controller;

import com.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {
    @RequestMapping("/t1")
    public String test() {
        return "test";
    }

    @RequestMapping("/userName")
    public void test1(String name, HttpServletResponse response) throws IOException {
        System.out.println("userName:param=> " + name);
        if ("Disney".equals(name)) {
            response.getWriter().println("true");
        } else {
            response.getWriter().println("false");
        }
    }

    @RequestMapping("/test2")
    public List<User> test2() {
        ArrayList<User> list = new ArrayList<User>();
        //  添加数据
        list.add(new User("Java", 22, "男"));
        list.add(new User("MySQL", 21, "女"));
        list.add(new User("Web", 23, "男"));

        return list;
    }


    @RequestMapping("/a3")
    public String a3(String name, String pwd) {
        String msg = "";
        if (name != null) {
//            这些数据应该在数据库查询
            if ("admin".equals(name)) {
                msg = "OK";
            }else{
                msg = "用户名有误";
            }
        }
        if (pwd != null) {
//            这些数据应该在数据库查询
            if ("123456".equals(pwd)) {
                msg = "OK";
            }else{
                msg = "密码有误";
            }
        }
        return msg;
    }
}
```



**POJO类：**

```java
package com.pojo;

public class User {
    private String name;
    private int age;
    private String sex;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }

    public User(String name, int age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

```



**applicationContext.xml：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <context:component-scan base-package="com.controller"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler />
    <mvc:annotation-driven />

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>

    <!--
       JSON乱码问题配置
   -->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>

</beans>
```



**login.jsp：**

```html
<%--
  Created by IntelliJ IDEA.
  User: qe249
  Date: 2023/12/7
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/statics/js/jquery-3.7.1.js"></script>
    <script>
        function a1() {
            //请求后端
            $.post({
                url: "${pageContext.request.contextPath}/a3",
                data: {"name": $("#name").val()},
                success: function (data) {
                    if (data.toString() === 'ok') {
                        $("#userInfo").css('color', "green");
                    }else{
                        $("#userInfo").css('color', "#ccc");
                    }
                    $("#userInfo").html(data);
                }
            })
        }

        function a2() {
            $.post({
                url: "${pageContext.request.contextPath}/a3",
                data: {"pwd": $("#pwd").val()},
                success: function (data) {
                    if (data.toString() === 'ok') {
                        $("#pwdInfo").css('color', "#ccc");
                    }else{
                        $("#pwdInfo").css('color',"red")
                    }
                    $("#pwdInfo").html(data);
                }
            })
        }
    </script>
</head>
<body>
<p>
    用户名：<input type="text" id="name" onblur="a1()">
    <span id="userInfo"></span>
</p>
<p>
    密码：<input type="text" id="pwd" onblur="a2()">
    <span id="pwdInfo"></span>
</p>
</body>
</html>
```





## **10.4、拦截器**

> 概述

SpringMVC的处理器拦截器类似于Servlet开发中的过滤器Filter，用于处理器进行与处理和后处理。

**过滤器和拦截器的区别：**拦截器是AOP思想的具体应用

**过滤器：**

- ​	servlet规范中的一部分，任何Java Web工程都可以使用
- ​    url-pattern中配置了/*之后，可以对所有要访问的资源进行过滤

**拦截器：**

- ​	拦截器是SpringMVC框架的，只有使用了SpringMVC框架的工程才能使用
- ​    拦截器只会拦截访问的控制方法，如果访问的是jsp/html/css/image/js 是不会进行拦截的



> 自定义拦截器

```java
package com.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    //  return true;执行下一个拦截器 ，放行
    //  return false;不执行下一个拦截器 ，不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("====处理前====");

        return true;
    }

    //  拦截日志 可以不写
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //  拦截日志 可以不写
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```



**applicationContext.xml**

```xml
<!--    拦截器配置-->
<mvc:interceptors>
    <mvc:interceptor>
        <!--    包括这个请求下面的所有的请求-->
        <mvc:mapping path="/**"/>
        <bean class="com.config.MyInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```



**test**

```java
@GetMapping("/t1")
public String test(){
    System.out.println("前端进入了此方法");
    return "OK";
}
```

此方法就会先被拦截器给截取到，因为返回的是true，所以拦截器不做任何的处理



> 小Demo

**首页面：**

```jsp
<%--
  Created by IntelliJ IDEA.
  User: qe249
  Date: 2023/12/13
  Time: 17:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<h1>
    <a href="${pageContext.request.contextPath}/user/goLogin">登录页面</a>
    <a href="${pageContext.request.contextPath}/user/main">首页</a>
</h1>
</body>
</html>
```

**login.jsp：**

```jsp
<%--
  Created by IntelliJ IDEA.
  User: qe249
  Date: 2023/12/15
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>登录</h1>
<form action="${pageContext.request.contextPath}/user/login" method="post">
    用户名：<input type="text" name="username" />
    密码：<input type="text" name="password" />
    <input type="submit" value="提交">
</form>
</body>
</html>
```

**main.jsp：**

```jsp
<%--
  Created by IntelliJ IDEA.
  User: qe249
  Date: 2023/12/15
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>首页</h1>
<span>${username}</span>
<p>
    <a href="${pageContext.request.contextPath}/user/goOut">注销</a>
</p>
</body>
</html>
```

**applicationContext.xml：**

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <!--    包括这个请求下面的所有的请求-->
        <mvc:mapping path="/user/**"/>
        <bean class="com.config.LoginInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

**LoginInterceptor：**

```java
package com.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //  放行：判断什么情况下登录

        //  session有东西 证明已经登录了
        HttpSession session = request.getSession();
        if(session.getAttribute("userLoginInfo") != null){
            return true;
        }

        //  登录页面能放行
        if (request.getRequestURI().contains("login")){
            return true;
        }



        //  判断什么情况下没有登录
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
        return false;
    }
}
```



## 10.5、文件上传

**导包：**

```xml
<dependencies>
    <dependency>
        <groupId>commons-fileupload</groupId>
        <artifactId>commons-fileupload</artifactId>
        <version>1.3.3</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
    </dependency>
</dependencies>
```



**applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <context:component-scan base-package="com.controller"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler/>
    <mvc:annotation-driven/>

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <!-- 后缀 -->
        <property name="suffix" value=".jsp"/>
    </bean>

    <!--
       JSON乱码问题配置
   -->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>

    <!--    拦截器配置-->
<!--    <mvc:interceptors>-->
<!--        <mvc:interceptor>-->
<!--            &lt;!&ndash;    包括这个请求下面的所有的请求&ndash;&gt;-->
<!--            <mvc:mapping path="/**"/>-->
<!--            <bean class="com.config.MyInterceptor"/>-->
<!--        </mvc:interceptor>-->

<!--        <mvc:interceptor>-->
<!--            &lt;!&ndash;    包括这个请求下面的所有的请求&ndash;&gt;-->
<!--            <mvc:mapping path="/user/**"/>-->
<!--            <bean class="com.config.LoginInterceptor"/>-->
<!--        </mvc:interceptor>-->
<!--    </mvc:interceptors>-->

    <!--文件上传配置-->
    <bean id="multipartResolver"  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
        <property name="defaultEncoding" value="utf-8"/>
        <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
        <property name="maxUploadSize" value="10485760"/>
        <property name="maxInMemorySize" value="40960"/>
    </bean>

</beans>
```



**index.jsp：**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
    <input type="file" name="file"/>
    <input type="submit" value="upload">

</form>
<a href="${pageContext.request.contextPath}/download">点击下载</a>
</body>
</html>
```



**FileController：**

```java
package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class FileController {
    //@RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
    //批量上传CommonsMultipartFile则为数组即可
    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") CommonsMultipartFile file , HttpServletRequest request) throws IOException {

        //获取文件名 : file.getOriginalFilename();
        String uploadFileName = file.getOriginalFilename();

        //如果文件名为空，直接回到首页！
        if ("".equals(uploadFileName)){
            return "redirect:/index.jsp";
        }
        System.out.println("上传文件名 : "+uploadFileName);

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        //如果路径不存在，创建一个
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        System.out.println("上传文件保存地址："+realPath);

        InputStream is = file.getInputStream(); //文件输入流
        OutputStream os = new FileOutputStream(new File(realPath,uploadFileName)); //文件输出流

        //读取写出
        int len=0;
        byte[] buffer = new byte[1024];
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
            os.flush();
        }
        os.close();
        is.close();
        return "redirect:/index.jsp";
    }

    /*
     * 采用file.Transto 来保存上传的文件
     */
    @RequestMapping("/upload2")
    public String  fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        //上传文件地址
        System.out.println("上传文件保存地址："+realPath);

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

        return "redirect:/index.jsp";
    }


    @RequestMapping(value="/download")
    public String downloads(HttpServletResponse response , HttpServletRequest request) throws Exception{
        //要下载的图片地址
        String  path = request.getServletContext().getRealPath("/upload");
        String  fileName = "基础语法.jpg";

        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path,fileName);
        //2、 读取文件--输入流
        InputStream input=new FileInputStream(file);
        //3、 写出文件--输出流
        OutputStream out = response.getOutputStream();

        byte[] buff =new byte[1024];
        int index=0;
        //4、执行 写出操作
        while((index= input.read(buff))!= -1){
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }
}
```

> 在out包找到本项目的war包，然后在upload下放进 基本语法.jpg 不然无法下载
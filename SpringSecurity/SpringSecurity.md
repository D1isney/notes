# 1、Spring Security（安全）

`在web开发中，安全第一位！（过滤器、拦截器等）`

`功能性需求：否`

`做网站：安全应该在什么时候考虑？设计之初`

shiro与SpringSecurity：很像

# 2、概述

- Spring Security是一个功能强大且高度可定制的身份验证和访问控制框架。它是保护基于Spring的应用程序的事实标准。

- Spring Security是一个专注于为Java应用程序提供身份验证和授权的框架。与所有Spring项目一样，SpringSecurity的真实的强大之处在于可以轻松地扩展它以满足自定义需求。

**特征：**

- 对身份验证和授权的全面可扩展支持
- 防止会话固定、点击劫持、跨站点请求伪造等攻击
- Servlet API集成
- 与Spring Web MVC的可选集成



**身份认证：**

- 身份认证是验证`谁正在访问系统资源`，判断用户是否为合法用户。认证用户的常见方式是要求用户输入用户名。

**授权：**

- 用户进行身份认证后，系统会控制`谁能访问哪些资源`，这个过程叫做授权。用户无法访问没有权限的资源。

**防御常见攻击：**

- CSRF
- HTTP Headers
- HTTP Requests



## 2.1、身份认证（authentication）

官方代码实例：[Github-spring-projects/spring-securtiy-samples](https://github.com/spring-projects/spring-security-samples/tree/main)



**创建SpringBoot项目：**

- Spring Web
- Spring Security
- Thymeleaf



**创建indexController：**

```java
package com.security.springboot06security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
```



**创建index.html：**

```html
<html xmlns:th="https://www.thymeleaf.org">
<head>
    <title>Hello Security!</title>
</head>
<body>
<h1>Hello Security</h1>
<a th:href="@{/logout}">Log Out</a>
<a href="/logout">Log Out 2</a>
</body>
</html>
```



**启动项目测试Controller：**

```makefile
初始的用户名：User
控制台会生成初始的密码
Using generated security password: f9f8dd53-120c-4104-8aac-c89c540bbaca

```



**注意事项：**

@{/logout}的作用：可以自适应的去识别浏览地址

```properties
server.servlet.context-path=/security
```

当地址中添加/security

```html
<a href="/logout">Log Out 2</a>
```

访问就会报404错误



**Spring Security默认做了什么？**

- 保护应用程序URL，要求对应用程序的任何交互进行身份验证
- 程序启动时生成一个默认用户“user”
- 生成一个默认的随机密码，并将此密码记录在控制台上
- 生成默认的登录表单和注销页面
- 提供基于表单的登录和注销流程
- 对于Web请求，重定向到登录页面
- 对于服务请求，返回401未经授权
- 处理跨网站请求伪造（CSRF）攻击
- 处理会话劫持攻击
- 写入Strict-Transport-Security以确保HTTPS
- 写入X-Content-Type-Options以处理嗅探攻击
- 写入Cache Control头来保护经过身份验证的资源
- 写入X-Frame-Options以处理点击劫持攻击



## 2.2、Spring Security的底层原理

官方文档：[SpringSecurity的底层原理](https://docs.spring.io/spring-security/reference/servlet/architecture.html)

- DelegatingFilterProxy：过滤器代理，帮我们调用Spring容器当中所有注册的过滤器
- FilterChainProxy：能够帮助我们管理多个不同的过滤器链SecurityFilterChain
- SecurityFilterChain：能够帮我们处理复杂的业务处理



Spring Security之所以默认帮助我们做了那么多事情，它的底层原理是传统的**Servlet过滤器**

![image-20240204164856889](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204164856889.png)

Client  ->  FilterChain（Filter0、Filter1、Filter2）  ->  Servlet



**DelegatingFilterProxy**：委托过滤器代理（本身也是一个过滤）

![image-20240204164938087](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204164938087.png)

Client  ->  FilterChain（Filter0、DelegatingFilterProxy[Bean Filter0]、Filter2）  ->  Servlet

委托了DelegatingFilterProxy将Bean Filter注册在整个Servlet生命周期当中



**FilterChainProxy：**

![image-20240204165740275](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204165740275.png)

![image-20240204165834524](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204165834524.png)

![image-20240204165931767](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204165931767.png)





## 2.3、程序的启动和运行

**DefaultSecurityFilterChain：**

SecurityFilterChain接口的实现，加载了默认的16个Filter（文中少了一个跨域过滤器【CorsFilter】）

```markdown
2024-02-04T17:12:02.700+08:00  INFO 14268 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [
    org.springframework.security.web.session.DisableEncodeUrlFilter@321ca237, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@4c5a2baf, org.springframework.security.web.context.SecurityContextHolderFilter@3fa21d49, org.springframework.security.web.header.HeaderWriterFilter@1ddc6db2, org.springframework.security.web.csrf.CsrfFilter@a146b11, org.springframework.security.web.authentication.logout.LogoutFilter@3610f277, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@78d9f51b, org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@4ed9f7b1, org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@1ec22831, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@4087c7fc, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@6e31d989, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@65bb6275, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@63f855b, org.springframework.security.web.access.ExceptionTranslationFilter@32118208, org.springframework.security.web.access.intercept.AuthorizationFilter@77db3d02
]
```

![image-20240204171853462](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204171853462.png)



**SecurityProperties：**

初始化配置，配置了默认的用户名（user）和密码（uuid）

![image-20240204172435781](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240204172435781.png)

在application.properties中配置自定义用户名和密码

```properties
spring.security.user.name=user
spring.security.user.password=123
```



# 3、Spring Secuity自定义配置

## 3.1、基于内存的用户认证

**创建自定义配置：**

实际开发的过程中，我们需要应用程序更加灵活，可以在SpringSecurity中创建自定义配置文件

官方文档：[Java自定义配置](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html)

**UserDetailsService**用来管理用户信息，**InMemoryUserDetailsManager**是UserDetailsService的一个实现，用来管理基于内存的用户信息。



创还能一个WebSecurityConfig文件：

定义一个@Bean，类型是UserDetailsService，实现是InMemoryUserDetailsManager

```java
package com.security.springboot06security.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// 标识这个类为配置类
@Configuration
//	开启Security的自定义配置（Spring项目必须添加上，SpringBoot可以省略）
//@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		// 创建基于内存的用户信息管理器
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		//	使用manager管理UserDetails对象
		manager.createUser(

				//	创建UserDetails对象，用于管理用户名、用户密码、用户角色、用户权限等内容
				User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build()
		);
		
		return manager;
	}
}
```



**基于内存的用户认证流程：**

- 程序启动时：
  - 创建**InMemoryUserDetailsManager**对象
  - 创建**User**对象，封装用户密码
  - 使用InMemoryUserDetailsManager**将User存入内存**
- 校验用户时：
  - SpringSecurity自动使用**InMemoryUserDetailsManager**的**loadUserByUsername**方法从**内存中**获取User对象
  - 在**UsernamePasswordAuthenticationFilter**过滤器中的attemptAuthentication方法中将用户输入的用户名密码和从内存中获取到的用户信息进行比较，进行用户认证



## 3.2、基于数据库的数据源

**SQL：**



**引入依赖：**

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.31</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.1</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

**EG：pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.security</groupId>
    <artifactId>SpringBoot06-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>SpringBoot06-security</name>
    <description>SpringBoot06-security</description>
    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>3.0.2</spring-boot.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity6</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.31</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.4.1</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
                <configuration>
                    <mainClass>com.security.springboot06security.SpringBoot06SecurityApplication</mainClass>
                    <skip>true</skip>
                </configuration>
                <executions>
                    <execution>
                        <id>repackage</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```



**配置数据源：**

```properties
#后端启动端口号
server.port=8080
#链接数据库
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis?characterEncoding=UTF8&autoReconnect=true&serverTimezone=Asia/Shanghai&allowMultiQueries=true&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
#SQL日志
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```



**实体类：**

```java
package com.security.springboot06security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {

    @TableId(value = "id",type = IdType.AUTO)// 主键自增
    private Integer id;

    private String name;

    private String pwd;

    private Integer role;

}
```



**Mapper：**

```java
package com.security.springboot06security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.springboot06security.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```



**Service：**

```java
package com.security.springboot06security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.security.springboot06security.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {
}
```



**ServiceImpl：**

```java
package com.security.springboot06security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.springboot06security.entity.User;
import com.security.springboot06security.mapper.UserMapper;
import com.security.springboot06security.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
```



**Mapper.xml：**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.security.springboot06security.mapper.UserMapper">

</mapper>
```



**Controller：**

```java
package com.security.springboot06security.controller;

import com.security.springboot06security.entity.User;
import com.security.springboot06security.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    public UserService userService;

    @GetMapping("/list")
    public List<User> getList(){
        return userService.list();
    }
}
```



## 3.3、基于数据库的用户认证

**基于数据库的用户认证流程：**

- 程序启动时：
  - 创建**DBUserDetailsManager**对象，实现接口 UserDetailsManager, UserDetailsPasswordService
  - 在应用程序中初始化这个类的对象
- 校验用户时：
  - SpringSecurity自动使用**DBUserDetailsManager**的**loadUserByUsername**方法从**数据库中**获取User对象
  - 在**UsernamePasswordAuthenticationFilter**过滤器中的attemptAuthentication方法中将用户输入的用户名密码和从数据库中获取到的用户信息进行比较，进行用户认证



**定义DBUserDetailsManager：**

```java
package com.security.springboot06security.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.springboot06security.entity.User;
import com.security.springboot06security.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.ArrayList;
import java.util.Collection;

public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }



    /***
     * 从数据库中获取用户信息
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            Boolean flag = false;
            if (user.getRole() == 0) {
                flag = false;
            } else if (user.getRole() == 1) {
                flag = true;
            }
            Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
            return new org.springframework.security.core.userdetails.User(
                    user.getName(),
                    passwordEncoder.encode(user.getPwd()), // 使用密码编码器加密密码
                    flag,
                    true,
                    true,
                    true,
                    authorityCollection
            );
        }
    }

}
```

版本原因：从中添加了一个SecurityConfig，使用自己定义的 BCryptPasswordEncoder 作为密码编码器

```java
package com.security.springboot06security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCryptPasswordEncoder 作为密码编码器
        return new BCryptPasswordEncoder();
    }
}
```

**使用一：**

```java
package com.security.springboot06security.config;

import com.security.springboot06security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// 标识这个类为配置类
@Configuration
//  开启Security的自定义配置（Spring项目必须添加上，SpringBoot可以省略）
//@EnableWebSecurity
public class WebSecurityConfig {

//  @Bean
//  public UserDetailsService userDetailsService() {
//     // 创建基于内存的用户信息管理器
//     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//     // 使用manager管理UserDetails对象
//     manager.createUser(
//           // 创建UserDetails对象，用于管理用户名、用户密码、用户角色、用户权限等内容
//           User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build()
//     );
//
//     return manager;
//  }

    @Bean
    public UserDetailsService userDetailsService() {
       // 创建基于数据库的用户信息管理器
       DBUserDetailsManager manager = new DBUserDetailsManager();

       return manager;
    }
}
```

**使用二：**在方法前使用@Component

```java
@Component
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
}
```





## 3.4、SpringSecurity的默认配置

```java
package com.security.springboot06security.config;

import com.security.springboot06security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

// 标识这个类为配置类
@Configuration
//  开启Security的自定义配置（Spring项目必须添加上，SpringBoot可以省略）
//@EnableWebSecurity
public class WebSecurityConfig {

//  @Bean
//  public UserDetailsService userDetailsService() {
//     // 创建基于内存的用户信息管理器
//     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//     // 使用manager管理UserDetails对象
//     manager.createUser(
//           // 创建UserDetails对象，用于管理用户名、用户密码、用户角色、用户权限等内容
//           User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build()
//     );
//
//     return manager;
//  }



//  @Bean
//  public UserDetailsService userDetailsService() {
//     // 创建基于数据库的用户信息管理器
//     DBUserDetailsManager manager = new DBUserDetailsManager();
//
//     return manager;
//  }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // 开启授权保护
       http.authorizeRequests(
             authorize -> authorize
                   // 对所有请求开启授权保护
                   .anyRequest()
                   // 已认证的请求会被自动授权
                   .authenticated()
             )
             // 生成html表单
             .formLogin(withDefaults()) //  使用表单授权方式

             //  没有html页面的表单，只有弹窗的表单
             .httpBasic(withDefaults());    // 使用基本授权方式
       return http.build();
    }
}
```



## 3.5、添加用户功能

**Controller：**

UserController中添加方法

```java
@PostMapping("/add")
public void add(@RequestBody User user){
    userService.saveUserDetails(user);
}
```

**Service：**

```java
@Service
public interface UserService extends IService<User> {
    void saveUserDetails(User user);
}
```

**ServiceImpl：**

```java
    @Resource
    private DBUserDetailsManager dbUserDetailsManager;
    @Override
    public void saveUserDetails(User user) {
        UserDetails details =
                org.springframework.security.core.userdetails
                        .User.withDefaultPasswordEncoder()
                        .username(user.getName())
                        .password(user.getPwd())
//                        .roles("USER")
                        .build();

        dbUserDetailsManager.createUser(details);
    }
```

**DBUserDetailsManager：**

```java
/***
 * 向数据库插入新的数据
 * @param userDetails
 */
@Override
public void createUser(UserDetails userDetails) {
    User user = new User();
    user.setName(userDetails.getUsername());
    user.setPwd(userDetails.getPassword());
    user.setRole(1);
    userMapper.insert(user);
}
```



**使用Swagger测试：**

导入依赖

```xml
<!--        swagger测试-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.1.0</version>
</dependency>
```



Swagger测试网址：http://localhost:8080/security/doc.html

**调试：**

![image-20240205144555415](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205144555415.png)

登录页会有一个隐藏的input

```html
<input name="_csrf" type="hidden" value="Cm_XuEd_rm5UPc490xZ_rEIpgGtpwC6OrUHV3XjGANzKnEEBaQzg23ROnAt5WasK5ztLzXYfrVJbpUqjmnXt60mlNu6prycx" />
```

`报403错误，说明没有权限，默认开启了csrf防御功能，post请求中，SpringSecurity规定一定要有CSRF的字符串`

在开发过程中，可以临时关闭csrf攻击防御

```java
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // 开启授权保护
       http.authorizeRequests(
             authorize -> authorize
                   // 对所有请求开启授权保护
                   .anyRequest()
                   // 已认证的请求会被自动授权
                   .authenticated()
             )
             // 生成html表单
             .formLogin(withDefaults()) //  使用表单授权方式

             //  没有html页面的表单，只有弹窗的表单
             .httpBasic(withDefaults());    // 使用基本授权方式

       // 关闭csrf攻击防御
       http.csrf(csrf -> csrf.disable());
        
       return http.build();
    }
}
```



## 3.6、密码加密算法

参考文档：

**密码加密方式：**

- **<font color="red">明文密码：</font>**

  最初，密码以明文形式存储在数据库中。但是恶意用户可能会通过SQL注入等手段获取到明文密码，或者程序员将数据库数据泄露的情况也可能发生。

- **<font color="red">Hash算法：</font>**

  SpringSecurity的**PasswordEncoder**接口用于对密码进行**`单向转换`**，从而将密码安全地存储。对密码单向转换需要用到**哈希算法**，例如MD5，SHA-256、SHA-512等，哈希算法是单向的，**只能加密，不能解密**。

  因此，**数据库中存储的是单向转换后的密码**，SpringSecurity在进行用户身份验证时需要将用户输入的密码进行单向转换，然后与数据库的密码进行比较。

  因此，如果发生数据泄露，只有密码的单向哈希会被暴露。由于哈希是单向的，并且在给定哈希的情况下只能通过**暴力破解的方式猜测密码**。

- **<font color="red">彩虹表：</font>**

  恶意用户创建成为**`彩虹表`**的查找表。

  彩虹表就是一个庞大的、针对各种可能的字母组合预选生成的哈希值集合，有了它可以快速破解各类密码，需要的彩虹表就越大，主流的彩虹表都是100G以上，目前主要的算法有LM，NTLM，MD5，SHA1，MYSQLSHA1，HALFMCHALL，NTLMCHALL，ORACLE-SYSTEM，MD5-HALF。

- **<font color="red">加盐密码：</font>**

  为了减轻彩虹表的效果，开发人员开始使用加盐密码。不在只使用密码作为哈希函数的输入，而是为每个用户密码生成随机字节（称为盐）。盐和用户的密码将一起经过哈希函数运算，生成一个唯一的哈希。盐将以明文形式与用户的密码一起存储。然后，当做用户尝试进行身份验证时，盐和用户输入的密码一起经过哈希函数运算，再与存储的密码进行比较。唯一的盐意味着彩虹表不再有效，因为对于每个盐和密码的组合，哈希都是不同的。

- **<font color="red">自适应单向函数：</font>**

  随着硬件的不断发展，加盐哈希也不再安全。原因是，计算机可以每秒执行数十亿次哈希计算。这就意味着我们可以轻松地破解每个密码。

  现在，开发人员开始使用自适应单向函数来存储密码。使用自适应单向函数验证密码时，**故意占用资源（故意 使用大量的CPU、内存或其他资源）**。自适应单向函数允许配置一个“工作因子”，随着硬件的改进而增加。我们建议将“工作因子”调整到系统重校验密码需要约一秒钟时间。这种权衡是为了**让攻击者难以破解密码**。

  自适应单向函数包括**bcrypt、PBKDF2、scrypt和argon2**。



**PasswordEncoder：**

bcrypt：

`BCrypt`是一种哈希函数，专门设计用于存储密码。它的目标是提供一种安全的单向哈希算法，防止密码泄露后的彩虹表攻击和其他常见的密码攻击。

以下是BCrypt的主要特点和工作原理：

**特点：**
**不可逆性：** BCrypt是一种单向哈希函数，无法通过散列值还原出原始密码。这增加了密码的安全性。

**随机性：** 每次使用BCrypt哈希密码时，它都会生成一个不同的散列值，即使两个用户使用相同的密码，它们的哈希值也会不同。这是通过加盐实现的。

慢速哈希： BCrypt被故意设计为相对慢速的哈希函数，以增加攻击者破解密码的难度。这对防止暴力破解和彩虹表攻击非常有用。

**加密过程：**
**加盐：** BCrypt使用随机生成的盐(salt)来增加哈希的随机性。盐会与密码合并，并在哈希过程中使用，以防止使用相同密码的用户具有相同的哈希值。

**迭代次数：** BCrypt允许指定哈希的迭代次数。增加迭代次数会增加哈希计算的时间，提高密码的安全性。

**哈希计算：** 将密码和盐进行哈希计算，生成最终的哈希值。

------

PBKDF2：

`PBKDF2`（Password-Based Key Derivation Function 2）是一种基于密码的密钥派生函数，通常用于从密码生成加密密钥。它的目的是增加密码的安全性，特别是防御暴力破解和彩虹表攻击。

以下是PBKDF2的主要特点和工作原理：

**特点：**

1. **可配置的迭代次数：** PBKDF2允许设置迭代次数，通过增加迭代次数，可以增加派生密钥的计算复杂性，从而提高密码的安全性。
2. **加盐：** 与BCrypt类似，PBKDF2使用盐(salt)来增加密码派生的随机性。每个用户的盐都是唯一的，即使两个用户使用相同的密码，由于不同的盐，生成的派生密钥也是不同的。
3. **众所周知的哈希函数：** PBKDF2通常使用常见的哈希函数（如SHA-256、SHA-3等）作为其基础哈希函数。

**使用过程：**

1. **选择哈希函数：** 选择一个强大的哈希函数作为PBKDF2的基础哈希函数。
2. **提供盐：** 为每个密码派生操作生成一个随机的盐。
3. **选择迭代次数：** 设置适当的迭代次数，以平衡性能和安全性。
4. **执行派生：** 使用PBKDF2算法，将密码、盐、迭代次数和哈希函数作为输入，生成最终的派生密钥。

------

scrypt：

`scrypt` 是一种密码学上的哈希函数和密钥派生函数，与传统的哈希函数（如MD5、SHA-1）不同，它旨在抵抗针对硬件和软件实施的暴力破解攻击。`scrypt` 的设计目标是在计算资源相对有限的情况下，提供更高的安全性。

以下是`scrypt` 的主要特点和工作原理：

**特点：**

1. **内存消耗：** `scrypt` 的一个关键特点是它在计算密钥派生函数时涉及大量的内存，这使得对定制硬件和专用攻击的抵抗性更强。
2. **可配置的参数：** 与其他哈希函数相比，`scrypt` 具有更多的可配置参数，包括 CPU/memory cost、block size 和 parallelization factor。
3. **加盐：** `scrypt` 使用盐(salt)来增加哈希的随机性，防止使用相同密码的用户具有相同的哈希值。

**使用过程：**

1. **选择参数：** 选择适当的参数，包括 CPU/memory cost、block size 和 parallelization factor。这些参数直接影响 `scrypt` 的计算复杂性。
2. **提供盐：** 为每个密码派生操作生成一个随机的盐。
3. **执行派生：** 使用 `scrypt` 算法，将密码、盐和参数作为输入，生成最终的派生密钥。

------

argon2：

`Argon2` 是一种密码哈希函数，专门设计用于存储密码，并旨在提供更高级的密码保护，抵抗各种密码攻击。`Argon2` 是 PHC (Password Hashing Competition) 中的获胜者，该比赛旨在推动密码学社区发展更安全的密码哈希函数。

以下是 `Argon2` 的主要特点和工作原理：

**特点：**

1. **抗并行性：** `Argon2` 被设计为抵抗并行计算攻击，这使得在使用定制硬件或并行计算资源的情况下，破解密码的难度更大。
2. **内存消耗：** 与 `scrypt` 类似，`Argon2` 使用大量内存，这增加了对专用硬件攻击的抵抗性。
3. **可配置参数：** `Argon2` 具有多个可配置参数，包括时间成本、内存成本、并行线程数等。这些参数直接影响哈希的计算复杂性。
4. **加盐：** `Argon2` 使用盐(salt)来增加哈希的随机性，防止使用相同密码的用户具有相同的哈希值。

**使用过程：**

1. **选择参数：** 选择适当的参数，包括时间成本、内存成本、并行线程数等。这些参数应根据系统的硬件和安全要求进行调整。
2. **提供盐：** 为每个密码派生操作生成一个随机的盐。
3. **执行哈希：** 使用 `Argon2` 算法，将密码、盐和参数作为输入，生成最终的哈希值。

------



**密码加密测试：**

```java
 //工作因子，默认值是10，最小值是4，最大值是31，值越大运算速度越慢
PasswordEncoder encoder = new BCryptPasswordEncoder(4);
// 明文：password
// 密文：result，即使明文密码相同，每次生成的秘闻也不一致
String result = encoder.encode("password");
System.out.println(result);

//  密码校验
Assert.isTrue(encoder.matches("password",result),"密码不一致");
```



**DelegatingPasswordEncoder：**

- 表中存储的密码形式：**{bcrypt}$2a$10$dUm5S35Pkm.ktbQEpVqJtOZOt33GwvHUxxZEWzZa7NXf/hQtIO/5u**
- 通过如源码可以知道：可以通过**{bcrypt}**前缀动态获取和密码的形式类型一致的PasswordEncoder对象
- 目的：方便随时做密码策略的升级，兼容数据库中的老版本密码策略生成的密码。



## 3.7、自定义登录页面

**创建登录Controller：**

```java
@GetMapping("/login")
public String login(){
    return "login";
}
```



**创建login.html：**

用户名和密码必须为username与password

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="https://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>SpringSecurity登录界面</title>
</head>
<body>
<h1>登录</h1>
<div th:if="${param.error}">
    错误的用户和密码.
</div>
<!--method必须为post-->
<!--th:action="@{/login}",
使用动态参数，表单中会自动生成_csrf隐藏字段，用于防止csrf攻击
login：和登录页面保持一致即可，SpringSecurity自动进行登录认证
-->
<form th:action="@{/login}" method="post">
    <div>
        <input type="text" name="username" placeholder="用户名"/>
    </div>
    <div>
        <input type="password" name="password" placeholder="密码">
    </div>
    <input type="submit" value="登陆">
    
</form>
</body>
</html>
```

可以在WebSecurityConfig里修改username与password

```java
       .formLogin(form ->{
          form.loginPage("/login").permitAll(); // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
       });
```



**WebSecurityConfig：**

不使用默认的表单

```java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // 开启授权保护
       http.authorizeRequests(
             authorize -> authorize
                   // 对所有请求开启授权保护
                   .anyRequest()
                   // 已认证的请求会被自动授权
                   .authenticated()
             )
             // 生成html表单
//           .formLogin(withDefaults()); // 使用表单授权方式
       .formLogin(form ->{
          form.loginPage("/login");
       });
             //  没有html页面的表单，只有弹窗的表单
//           .httpBasic(withDefaults());    // 使用基本授权方式

       // 关闭csrf攻击防御
       http.csrf(csrf -> csrf.disable());
       return http.build();
    }
```

会出现这个问题

![image-20240205211244103](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205211244103.png)

主要原因是login页被授权保护了，形成了递归跳转

使得登录页面无需授权即可访问登录页

```java
.formLogin(form ->{
    form.loginPage("/login").permitAll(); // 无需授权即可访问访问登录页面
});
```

错误页面

```java
       .formLogin(form ->{
          form.loginPage("/login").permitAll(); // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
//        .failureUrl("/login?failure"); //校验失败时跳转的地址，默认值是"/login?error"
       });
```

开启csrf攻击防御

```java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // 开启授权保护
       http.authorizeRequests(
             authorize -> authorize
                   // 对所有请求开启授权保护
                   .anyRequest()
                   // 已认证的请求会被自动授权
                   .authenticated()
             )
             // 生成html表单
//           .formLogin(withDefaults()); // 使用表单授权方式
       .formLogin(form ->{
          form.loginPage("/login").permitAll(); // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
//        .failureUrl("/login?failure"); //校验失败时跳转的地址，默认值是"/login?error"
       });
             //  没有html页面的表单，只有弹窗的表单
//           .httpBasic(withDefaults());    // 使用基本授权方式

       // 关闭csrf攻击防御
//     http.csrf(csrf -> csrf.disable());
       return http.build();
    }
```

![image-20240205220953615](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205220953615.png)

当不使用动态链接的from表单，action不但没有生成动态链接，连csrf也没有被自动的生成

![image-20240205221127656](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205221127656.png)



# 4、前后端分离

## 4.1、用户认证流程

- 登录成功后调用：AuthenticationSuccessHandler
- 登录失败后调用：AuthenticationFailureHandler

![image-20240205222915963](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205222915963.png)

## 4.2、引入fastjson

```xml
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2</artifactId>
    <version>2.0.37</version>
</dependency>
```



## 4.3、认证成功的响应

```java
package com.security.springboot06security.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //  获取用户身份信息
        Object principal = authentication.getPrincipal();
        //  获取用户的凭证信息
        Object credentials = authentication.getCredentials();
        //  获取用户权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        HashMap result = new HashMap();
        result.put("code",0);   // 成功
        result.put("message","登录成功");   //
        result.put("data",principal);   //

        
        //  将结果对象转换成json字符串
        String json = JSON.toJSONString(result);

        //  返回json数据到前端
        //  响应头
        response.setContentType("application/json;charset=UTF-8");
        //  响应体
        response.getWriter().println(json);
    }
}
```

```java
.formLogin(form ->{
          form.loginPage("/login").permitAll() // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
//        .failureUrl("/login?failure"); //校验失败时跳转的地址，默认值是"/login?error"
                .successHandler(new MyAuthenticationSuccessHandler()); // 认证成功时的处理
       });
```

![image-20240205225704182](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205225704182.png)



## 4.4、认证失败的响应

```java
package com.security.springboot06security.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        //  获取失败的信息
        String localizedMessage = exception.getLocalizedMessage();

        HashMap result = new HashMap();
        result.put("code",-1);   // 失败
        result.put("message","登录失败");   //


        //  将结果对象转换成json字符串
        String json = JSON.toJSONString(result);

        //  返回json数据到前端
        //  响应头
        response.setContentType("application/json;charset=UTF-8");
        //  响应体
        response.getWriter().println(json);
    }
}
```

```java
       .formLogin(form ->{
          form.loginPage("/login").permitAll() // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
//        .failureUrl("/login?failure"); //校验失败时跳转的地址，默认值是"/login?error"
                .successHandler(new MyAuthenticationSuccessHandler())  // 认证成功时的处理
                .failureHandler(new MyAuthenticationFailureHandler()); // 认证失败时的处理
       });
```

![image-20240205230209103](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205230209103.png)



## 4.5、注销响应

```java
package com.security.springboot06security.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        HashMap result = new HashMap();
        result.put("code",0);   // 成功
        result.put("message","注销成功");   //

        //  将结果对象转换成json字符串
        String json = JSON.toJSONString(result);

        //  返回json数据到前端
        //  响应头
        response.setContentType("application/json;charset=UTF-8");
        //  响应体
        response.getWriter().println(json);
    }
}
```

```java
       .formLogin(form ->{
          form.loginPage("/login").permitAll() // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
//        .failureUrl("/login?failure"); //校验失败时跳转的地址，默认值是"/login?error"
                .successHandler(new MyAuthenticationSuccessHandler())  // 认证成功时的处理
                .failureHandler(new MyAuthenticationFailureHandler()); // 认证失败时的处理
       }).logout(logout -> {
          logout.logoutSuccessHandler(new MyLogoutSuccessHandler()); //注销成功时的处理
             });
```

![image-20240205230857620](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240205230857620.png)



## 4.6、请求未认证的接口

当访问一个需要认证之后才能访问的接口的时候，Spring Security会使用**AuthenticationEntryPoint**将用户请求跳转到登录页面，要求用户提供登录凭证。

这里我们也希望系统**返回JSON结果**，因此我们定义类**实现AuthenticationEntryPoint接口**

```java
package com.security.springboot06security.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String localizedMessage = "需要登录";//authException.getLocalizedMessage();

        HashMap result = new HashMap();
        result.put("code",-1);   // 告诉用户需要登录
        result.put("message",localizedMessage);   //


        //  将结果对象转换成json字符串
        String json = JSON.toJSONString(result);

        //  返回json数据到前端
        //  响应头
        response.setContentType("application/json;charset=UTF-8");
        //  响应体
        response.getWriter().println(json);
    }
}

```

```java
.formLogin(form ->{
          form.loginPage("/login").permitAll() // 无需授权即可访问访问登录页面
//              .usernameParameter("name") // 修改表单规定写死的username
//              .passwordParameter("pwd"); // 修改表单规定写死的password
//        .failureUrl("/login?failure"); //校验失败时跳转的地址，默认值是"/login?error"
                .successHandler(new MyAuthenticationSuccessHandler())  // 认证成功时的处理
                .failureHandler(new MyAuthenticationFailureHandler()); // 认证失败时的处理
       }).logout(logout -> {
          logout.logoutSuccessHandler(new MyLogoutSuccessHandler()); //注销成功时的处理
             }).exceptionHandling(exception ->{
                exception.authenticationEntryPoint(new MyAuthenticationEntryPoint()); //   请求未认证的处理
             });
```



## 4.7、跨域

跨域全称是跨域资源共享（Cross-Origin Resources Sharing，CORS），它是浏览器的保护机制，只允许网页请求统一域名下的服务，统一域名指=>协议、域名、端口号都要保持一致，如果有一项不同，那么就是跨域请求。在前后端分离的项目中，需要解决跨域的问题。

在SpringSecurity中解决跨域很简单，在配置文件中添加如下配置即可

```java
//	跨域
http.cors(withDefaults());
```



# 5、身份认证

## 5.1、用户认证信息

**基本概念**

![image-20240206014544763](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240206014544763.png)

在SpringSecurity框架中，SecurityContextHolder、SecurityContext、Authentication、Principal和Credential是一些与身份验证功能和授权相关的重要概念。它们之间的关系如下：

1. SecurityContextHolder：SecurityContextHolder是SpringSecurity存储已认证用户详细信息的地方。

2. SecurityContext：SecurityContext是从SecurityContextHolder获取的内容，包含当前已认证用户的Authentication信息。

3. Authentication：Authentication表示用户的身份认证信息。它包含了用户的Principal、Credential和Authority信息。

4. Principal：通常用于表示当前用户的身份信息。Spring Security 将 `Principal` 封装在 `Authentication` 对象中，该对象包含了有关用户身份的详细信息，如用户名、密码（通常是已加密的密码）、权限（角色）等。

   在 Spring Security 中，`Principal` 可以是一个 `UserDetails` 对象，该对象包含了有关用户的详细信息。`UserDetails` 接口定义了获取用户名、密码、权限等信息的方法。

5. 在 Spring Security 中，`Credential` 通常指的是用户的凭据，即用户提供的用于身份验证的信息，最常见的形式是密码。在身份验证流程中，用户通常会提供用户名和密码（凭据），以证明他们的身份。

   Spring Security 使用 `Authentication` 对象来表示身份验证请求。其中，凭据信息通常包含在 `UsernamePasswordAuthenticationToken` 对象中，该对象是 `Authentication` 的一个实现。



```java
SecurityContext context = SecurityContextHolder.getContext();
Authentication authentication = context.getAuthentication();
Object principal = authentication.getPrincipal();
Object credentials = authentication.getCredentials();
//  授权信息
Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
```

```java
package com.security.springboot06security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class indexController {

    @GetMapping("/")
    public Map index(){

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        //  授权信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String name = authentication.getName();

        HashMap result = new HashMap();
        result.put("username",name);
        result.put("authorities",authorities);

        return result;
    }
}
```





## 5.2、会话并发处理

`后登陆的账号会使先登录的账号失效`

**实现处理器接口**

实现接口SessionInformationExpiredStrategy

```java
package com.security.springboot06security.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;

public class MySessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {

        HashMap result = new HashMap();
        result.put("code",-1);   // 告诉用户需要登录
        result.put("message","该账号已从其他设备登录");   //


        //  将结果对象转换成json字符串
        String json = JSON.toJSONString(result);

        HttpServletResponse response = event.getResponse();
        //  返回json数据到前端
        //  响应头
        response.setContentType("application/json;charset=UTF-8");
        //  响应体
        response.getWriter().println(json);
    }
}
```

```java
http.sessionManagement(session ->{
    session.maximumSessions(1) // 设置最多多少个用户登录，后登陆的用户正常登录，先登录的用户超时
          .expiredSessionStrategy(new MySessionInformationExpiredStrategy());
});
```

![image-20240206020847488](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240206020847488.png)



# 6、授权

授权管理的实现在SpringSecurity中非常灵活，可以帮助应用程序实现以下两种常见的授权需求：

- 用户-权限-资源：例如张三的权限是添加用户、查看用户列表，李四的权限是查看用户列表
- 用户-角色-权限-资源：例如张三的角色是管理员、李四的角色是普通用户，管理员能做所有的操作，普通用户只能查看信息



## 6.1、基于Request的授权

**用户-权限-资源**

`需求：`

- 具有USER_LIST权限的用户可以访问/user/list
- 具有USER_ADD权限的用户可以访问/user/add

**配置权限**

SecurityFilterChain

```java
http.authorizeRequests(
       authorize -> authorize
             // 添加授权配置
             .requestMatchers("/user/list").hasAnyAuthority("USER_LIST")
             .requestMatchers("/user/add").hasAnyAuthority("USER_ADD")

             // 对所有请求开启授权保护
             .anyRequest()
             // 已认证的请求会被自动授权
             .authenticated()
       )
```

```java
/***
 * 从数据库中获取用户信息
 * @param username the username identifying the user whose data is required.
 * @return
 * @throws UsernameNotFoundException
 */
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("name", username);
    User user = userMapper.selectOne(queryWrapper);
    if (user == null) {
        throw new UsernameNotFoundException(username);
    } else {
        Boolean flag = false;
        if (user.getRole() == 0) {
            flag = false;
        } else if (user.getRole() == 1) {
            flag = true;
        }
        // 使用密码编码器加密密码
        user.setPwd(passwordEncoder.encode(user.getPwd()));

        //  直接赋值这两个权限
        Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
        authorityCollection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "USER_LIST";
            }
        });
        authorityCollection.add(()->"USER_ADD");

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPwd(),
                flag,
                true,
                true,
                true,
                authorityCollection
        );
    }
}
```

![image-20240206022643305](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240206022643305.png)

很明显，此用户已经授权了这两个权限

**请求未授权的接口**

```java
package com.security.springboot06security.config;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code",-1);   // 没有权限
        result.put("message","没有权限");   //


        //  将结果对象转换成json字符串
        String json = JSON.toJSONString(result);

        //  返回json数据到前端
        //  响应头
        response.setContentType("application/json;charset=UTF-8");
        //  响应体
        response.getWriter().println(json);
    }
}
```

```java
http.exceptionHandling(exception ->{
          exception.authenticationEntryPoint(new MyAuthenticationEntryPoint()); //   请求未认证的处理
          exception.accessDeniedHandler(new MyAccessDeniedHandler());            //
});
```



**用户-角色-资源**

`需求：角色为ADMIN的用户才可以访问/user/**路径下的资源`

配置角色

SecurityFilterChain

```java
@Override
    public boolean userExists(String username) {
        return false;
    }



    /***
     * 从数据库中获取用户信息
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            Boolean flag = false;
            if (user.getRole() == 0) {
                flag = false;
            } else if (user.getRole() == 1) {
                flag = true;
            }
//            // 使用密码编码器加密密码
//            user.setPwd(passwordEncoder.encode(user.getPwd()));
//
//            //  直接赋值这两个权限
//            Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
//            authorityCollection.add(new GrantedAuthority() {
//                @Override
//                public String getAuthority() {
//                    return "USER_LIST";
//                }
//            });
//            authorityCollection.add(()->"USER_ADD");
//
//            return new org.springframework.security.core.userdetails.User(
//                    user.getName(),
//                    user.getPwd(),
//                    flag,
//                    true,
//                    true,
//                    true,
//                    authorityCollection
//            );
            return org.springframework.security.core.userdetails.User.withUsername(
                    user.getName())
                    .password(user.getPwd())
                    .disabled(!flag)
                    .credentialsExpired(false)
                    .accountLocked(false)
                    .roles("ADMIN")
                    .build();
        }
    }
```



**用户-角色-权限-资源**

RBAC（Role-Based Access Control，基于角色的访问控制）是一种常用的数据库设计方案，它将用户的权限分配和管理与角色相关联。以下是一个基本的RBAC数据库设计方案的示例：

1. 用户表（User table）：包括用户的基本信息，例如用户名、密码和其他身份验证信息。

   | 列名     | 数据类型 | 描述         |
   | -------- | -------- | ------------ |
   | user_id  | int      | 用户ID       |
   | username | varchar  | 用户名       |
   | password | varchar  | 密码         |
   | email    | varchar  | 电子邮件地址 |
   | ...      | ...      | ...          |

2. 角色表（Role table）：存储所有可能得角色及其描述。

   | 列名        | 数据类型 | 描述     |
   | ----------- | -------- | -------- |
   | role_id     | int      | 角色ID   |
   | role_name   | varchar  | 角色名称 |
   | description | varchar  | 角色描述 |
   | ...         | ...      | ...      |

3. 权限表（Permission table）：定义系统中所有可能得权限。

   | 列名            | 数据类型 | 描述     |
   | --------------- | -------- | -------- |
   | permission_id   | int      | 权限ID   |
   | permission_name | varchar  | 权限名称 |
   | description     | varhar   | 权限描述 |
   | ...             | ...      | ...      |

4. 用户角色关联表（User-Role table）：将用户与角色关联起来。

   | 列名         | 数据类型 | 描述           |
   | ------------ | -------- | -------------- |
   | user_role_id | int      | 用户角色关联ID |
   | user_id      | int      | 用户ID         |
   | role_id      | int      | 角色ID         |
   | ...          | ...      | ...            |

5. 角色权限关联表（Role-Permission table）：将角色与权限关联起来。

   | 列名                | 数据类型 | 描述           |
   | ------------------- | -------- | -------------- |
   | role_permissions_id | int      | 角色权限关联ID |
   | role_id             | int      | 角色ID         |
   | permission_id       | int      | 权限ID         |
   | ...                 | ...      | ...            |

在这个设计方案中，用户可以被分配一个或多个角色，而每个角色又可以具有一个或多个权限。通过对用户角色关联和角色权限关联表进行操作，可以实现灵活的权限管理和访问控制。





## 6.2、基于方法的授权

**开启方法授权**

配置文件中添加如下注解

```java
@EnableMethodSecurity
```

```java
// 标识这个类为配置类
@Configuration
@EnableMethodSecurity   // 开启基于方法的授权
public class WebSecurityConfig {
}
```

给用户授予权限

```java
 return org.springframework.security.core.userdetails.User.withUsername(
            user.getName())
            .password(user.getPwd())
            .disabled(!flag)
            .credentialsExpired(false)
            .accountLocked(false)
            .roles("ADMIN")
            .build();
}
```

![image-20240207155243124](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207155243124.png)



**方法检查用户是否有权限**

```java
@GetMapping("/list")
@PreAuthorize("hasRole('ADMIN')")
//  执行方法之前检查是否有这个权限
public List<User> getList(){
    return userService.list();
}

@PostMapping("/add")
//  执行方法之前检查是否有这个权限
@PreAuthorize("hasRole('USER')")
public void add(@RequestBody User user){
    userService.saveUserDetails(user);
}
```

![image-20240207155859261](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207155859261.png)

ADMIN就没有了添加的权限

ADMIN就只能查询了

![image-20240207155955018](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207155955018.png)



给定一定的用户名，如果用户名不是指定的，就无法访问这个方法

```java
@GetMapping("/list")
@PreAuthorize("hasRole('ADMIN') and authentication.name == '22'")
//  执行方法之前检查是否有这个权限
public List<User> getList(){
    return userService.list();
}
```

![image-20240207160716005](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207160716005.png)

对应的使用别的账号登录，就无法获取到getList

使用了相应的账号名就可以访问了

![image-20240207160840260](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207160840260.png)



两种写法

```java
//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize(
            "hasAnyAuthority('USER_ADD')"
    )
```



**多种权限的授予**

```java
   return org.springframework.security.core.userdetails.User.withUsername(
                    user.getName())
                    .password(user.getPwd())
                    .disabled(!flag)
                    .credentialsExpired(false)
                    .accountLocked(false)
                    .roles("ADMIN","USER_ADD")
//                    .roles("USER_ADD") // 会覆盖前面的权限
                    .build();
        }
```

![image-20240207161233208](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207161233208.png)



# 7、OAuth2

## 7.1、OAuth2简介

`OAuth2是什么？`

“Auth”表示“授权”Authorization
“O”是Open的简称，表示“开放”
连在一起就表示“开放授权”。OAuth2是一种**开放授权**的协议。



川崎高彦：[OAuth2.0](https://darutk.medium.com/the-simplest-guide-to-oauth-2-0-8c71bd9a15bb)

阮一峰：[OAuth 2.0 的一个简单解释](https://www.ruanyifeng.com/blog/2019/04/oauth_design.html)



## 7.2、OAuth2的角色

OAuth2.0协议包含以下角色：

1. 资源所有者（Resource Owner）：即用户，资源的拥有人，想要通过客户应用访问资源服务器上的资源。
2. 客户应用（Client）：通常是一个Web或无线应用，他需要访问用户的受保护资源。
3. 资源服务器（Resource Server）：存储受保护资源的服务器或定义了可以访问到资源的API，接收并验证客户端的访问令牌，以决定是否授权访问资源。
4. 授权服务器（Authorization Server）：负责验证资源所有者的身份并向客户端颁发访问令牌。

![image-20240207173159108](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207173159108.png)



## 7.3、 OAuth2的使用场景

### 开放系统间授权

#### 社交登录

在传统的身份验证中，用户需要提供用户名和密码，还有很多网站登录时，允许使用第三方网站的身份，这成为“第三方登录”。所谓第三方登录，实质就是OAuth授权。用户想要登录A网站，A网站让用户提供第三方网站的数据，证明自己的身份。获取第三方网站的身份数据，就需要OAuth授权。



#### 开放API

例如云冲印服务的实现



### 现代微服务安全

#### 单块应用安全

![image-20240207174933170](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207174933170.png)



#### 微服务安全

![image-20240207175114458](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207175114458.png)





#### 企业内部应用认证授权

- SSO：Single Sign On 单点登录
- IAM：Identity and Access Management身份识别和访问管理





## 7.4、OAuth2的四中授权模式 

**阮一峰：**[OAuth 2.0 的四种方式 - 阮一峰的网络日志（ruanyifeng.com）](https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)



四种模式：

- 授权码（Authorization-code）
- 隐藏式（Implicit）
- 密码式（Password）
- 客户端凭证（Client Credentials）



### 第一种方式：授权码

`授权码（Authorization code），指的是第三方应用现申请一个授权码，然后再用该码获取令牌。`

这种方式是最常用，最复杂，也是最安全的，他适用于哪些有后端的Web应用。授权码通过前端传送，令牌则是存储在后端，而且所有与资源服务器的通信都在后端完成。这样的前后端分离，可以避免令牌泄露。

![image-20240207182636656](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207182636656.png)



### 第二种方式：隐藏式

`隐藏式（Implicit），有些Web应用是纯前端应用，没有后端。这时就用不上上面的方式了，必须将令牌存储在前端。`

RFC6749规定了这种方式，允许直接向前端办法令牌。这种方式没有授权码这个中间步骤，所以称为隐藏式。

![image-20240207183134281](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207183134281.png)



### 第三种方式：密码式

`密码式（Password）：如果你高度信任某个应用，RFC6749也允许用户把用户名和密码，直接告诉该应用。该应用就是用你的密码，申请令牌。`

这种方式需要用户给自己的用户名/密码，虽然风险很大，因此只适用于其他授权方式都无法采用的情况，而且必须是用户高度信任的应用。

![image-20240207183544185](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207183544185.png) 



### 第四种方式：凭证式

`凭证式（Client Credentials）：适用于没有前端的命令行应用，即在命令行下请求令牌。`

这种方式给出的令牌，是针对第三方应用的，而不是针对用户的，即有可能多个用户共享一个令牌。

![image-20240207183134281](K:\GitHub\notes\SpringSecurity\SpringSecurity.assets\image-20240207183134281.png)
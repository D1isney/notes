# Spring

# 1、Spring

## 1.1、简介

- Spring框架是由于软件开发的复杂性而创建的。Spring使用的是基本的JavaBean来完成以前只可能由EJB完成的事情。然而，Spring的用途不仅仅限于服务器端的开发。从简单性、可测试性和松耦合性角度而言，绝大部分Java应用都可以从Spring中受益。

- 2002年，首次推出了Spring框架的雏形，interface21框架

- Spring框架即以interface为基础，j经过重新设计，并不断丰富其内涵，于2004年3月24日，发布了1.0正式版。

- **RodJohnson**，Spring Framework创始人。

- Spring理念：使现有的技术更加容易使用，简化开发。整合了现有的技术框架。

- ```markdown
  官网：https://spring.io/
  GitHub：https://github.com/spring-projects/spring-framework
  中文文档：https://www.docs4dev.com/docs/zh/spring-framework/5.1.3.RELEASE/reference/
  ```

- SSH：Struct2 + Spring + Hibernate
- SSM：SpringMVC + Spring + Mybatis



## 1.2、优点

- Spring是一个开源的免费的框架（容器）
- Spring是一个轻量级的、非入侵式的框架
- 控制反转（IOC）、面向切面编程（AOP）
- 支持事务的处理

总结：Spring就是一个轻量级的控制反转（IOC）和面向切面编程（AOP）的框架！



## 1.3、组成

**七大模块**

![image-20231106131454478](K:\JAVA_SSM\Spring.assets\image-20231106131454478.png)



## 1.4、拓展

- Spring Boot：构建一切
  - 一个快速开发的脚手架
  - 基于SpringBoot可以快速开发单个微服务
  - 约定大于配置
- Spring Cloud：协调一切
  - Spring Cloud是基于Spring Boot实现的
- SpringCloud Data Flow：连接一切

弊端：发展了太久之后，违背了原来的理念，配置十分繁琐，人称“**配置地狱”**。



# 2、IOC理论推导

1. UserDao接口
2. UserDaoImpl 实现类
3. UserService 业务接口
4. UserServiceImpl 业务实现类

用set接口实现，动态化创建需要的类型业务

```java
//     利用set进行动态实现值的注入
public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
}
```

```java
    @Test
    public void demoTest(){
        //  用户实际调用
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(new UserDaoOracleImpl());
//        userService.setUserDao(new UserDaoMySqlImpl());
//        userService.setUserDao(new UserDaoMySqlImpl());
        userService.getUser();
    }
```

**IOC本质**

**控制反转IOC（Inversion of Control），是一种设计思想，DI（依赖注入）是实现IOC的一种方法**，也有人认为DI只是IOC的另一种说法。没有IOC的程序中，我们使用面向对象编程，对象的创建与对象间的依赖关系完全硬编码在程序中，对象的创建有程序自己控制，控制反转后将对象的创还能转移给第三方，个人认为所谓控制反转就是：获得依赖对象的方式反转了。



采用XML方式配置Bean的时候，Bean的定义信息是和实现分离的，而采用注解的方式可以把两者合为一体，Bean的定义信息直接以注解的形式定义在实现类中，从而达到了零配置的目的。



**控制反转是一种通过描述（XML或注解）并通过第三方去生产或获取特定对象的方式。在Spring中实现控制反转的是IOC容器，其实现方法是依赖注入（Dependency Injection，DI）**



# 3、HelloSpring

XML配置文件

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--    使用Spring来创建对象，在Spring中这些都成为Bean-->
    <!--
        类型 变量名 = new 类型();
        Hello hello = new Hello();
        bean = 对象   new Hello();

        id = 变量名
        class = new 的对象
        property 相当于给对象中的属性设置一个值
    -->
    <bean id="hello" class="com.pojo.Hello">
        <property name="name" value="Spring"/>
    </bean>

</beans>

```

POJO

```java
package com.pojo;

public class Hello {
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void show() {
        System.out.println("Hello" + Name);
    }
}

```

测试

```java
import com.pojo.Hello;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    @Test
    public void demoTest() {
        //  获取Spring的上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //  对象都在Spring中管理了，使用就直接去里面取出来皆可以了
        Hello hello = (Hello) context.getBean("hello");
        hello.setName("DisneyD33");
        hello.show();
    }
}
```

**反转：**程序本身不创建对象，而变成被动的接收对象

**依赖注入：**必须有Set方法

IOC是一种编程思想，由主动的编程变成被动的接收。

IOC：对象由Spring来创建，管理，装配



# 4、IOC创建对象的方式

## 4.1、使用无参构造创建对象，默认

```java
    public User() {
    }

```

```xml
<bean id="user" class="com.pojo.User">
    <property name="name" value="DisenyD33"/>
</bean>
```

```java
        //  拿到容器
        //  默认只能构建无参
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        User user = (User) context.getBean("user");
        user.show();
```

## 4.2、假设要使用有参构造创建对象

```java
    public User(String name){
        this.name = name;
    }
```

1. 下标赋值

   ```xml
       <!--    第一种。下表赋值-->
       <bean id="user" class="com.pojo.User">
           <constructor-arg index="0" value="DisneyD33"/>
       </bean>
   ```

2. 参数的类型匹配

   ```xml
       <!--    第一种。下表赋值-->
       <bean id="user" class="com.pojo.User">
           <constructor-arg index="0" value="DisneyD33"/>
       </bean>
   ```

3. **通过参数名**

   ```xml
       <!--    第三种：直接通过参数名来设置-->
       <bean id="user" class="com.pojo.User">
           <constructor-arg name="name" value="蛋蛋"/>
       </bean>
   ```

## 4.3、总结

```java
package com.pojo;

public class UserD {
    private String name;

    public UserD() {
        System.out.println("UserD无参构造函数");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("name：" + name);
    }
}

```

```xml
    <bean id="user" class="com.pojo.User">
        <constructor-arg name="name" value="蛋蛋"/>
    </bean>


    <bean id="userD" class="com.pojo.UserD">

    </bean>
```

```java
        //  拿到容器
        //  默认只能构建无参
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        User user = (User) context.getBean("user");
        user.show();
```

```markdown
UserD无参构造函数
name：蛋蛋
```

在配置文件加载的时候，容器中管理的对象就已经初始化了



# 5、Spring配置

## 5.1、别名

```xml
    <!--    别名，如果添加了别名，我们也可以不使用别名获取对象-->
    <alias name="user" alias="disney"/>
```

## 5.2、Bean的配置

```xml
    <!--
        id：bean的唯一标识符，也就是相当于对象名
        class：bean 对象所对应的类型 ： 包名 + 类型
        name：也是别名，而且name 可以同时取多个别名
    -->
    <bean id="userD" class="com.pojo.UserD" name="d1,d2">

    </bean>
```

## 5.3、import

一般用于团队开发使用，可以将多个配置文件，合并导入到一个

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">

    <import resource="beans.xml"/>
    <import resource="beans2.xml"/>

</beans>
```



# 6、依赖注入

## 6.1 、构造器注入



## **6.2、Set注入【重点】**

- 依赖注入：Set注入
  - 依赖：bean对象的创建依赖于容器
  - 注入：bean对象中的所有属性，由容器来注入

【环境搭建】

1. 复杂类型

   ```java
   package com.pojo;
   
   public class Address {
       private String address;
   
       public String getAddress() {
           return address;
       }
   
       public void setAddress(String address) {
           this.address = address;
       }
   }
   ```

2. 真实测试对象

   ```java
   package com.pojo;
   
   import java.util.*;
   
   public class Student {
   
       private String name;
       private Address address;
       private String[] books;
       private List<String> hobby;
       private Map<String,String> card;
       private Set<String> game;
       private String wife;
       private Properties info;
   
       @Override
       public String toString() {
           return "Student{" +
                   "name='" + name + '\'' +
                   ", address=" + address +
                   ", books=" + Arrays.toString(books) +
                   ", hobby=" + hobby +
                   ", card=" + card +
                   ", game=" + game +
                   ", wife='" + wife + '\'' +
                   ", info=" + info +
                   '}';
       }
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public Address getAddress() {
           return address;
       }
   
       public void setAddress(Address address) {
           this.address = address;
       }
   
       public String[] getBooks() {
           return books;
       }
   
       public void setBooks(String[] books) {
           this.books = books;
       }
   
       public List<String> getHobby() {
           return hobby;
       }
   
       public void setHobby(List<String> hobby) {
           this.hobby = hobby;
       }
   
       public Map<String, String> getCard() {
           return card;
       }
   
       public void setCard(Map<String, String> card) {
           this.card = card;
       }
   
       public Set<String> getGame() {
           return game;
       }
   
       public void setGame(Set<String> game) {
           this.game = game;
       }
   
       public String getWife() {
           return wife;
       }
   
       public void setWife(String wife) {
           this.wife = wife;
       }
   
       public Properties getInfo() {
           return info;
       }
   
       public void setInfo(Properties info) {
           this.info = info;
       }
   }
   ```

3. applicationContext.xml

   ```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="student" class="com.pojo.Student">
           <!--        第一种：普通值注入 value-->
           <property name="name" value="DisneyD33"/>
       </bean>
   
   </beans>
   ```

   

4. 测试类

   ```java
   import com.pojo.Student;
   import org.springframework.context.ApplicationContext;
   import org.springframework.context.support.ClassPathXmlApplicationContext;
   
   public class MyTest {
       public static void main(String[] args) {
           ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
           Student student = (Student) context.getBean("student");
           System.out.println(student);
       }
   }
   ```

​	

完善注入信息

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="student" class="com.pojo.Student">
        <!--        第一种：普通值注入 value-->
        <property name="name" value="DisneyD33"/>
        <!--        第二种：Bean注入，ref-->
        <property name="address" ref="address"/>

        <!--        数组注入-->
        <property name="books">
            <array>
                <value>JavaWeb</value>
                <value>Web</value>
                <value>Java</value>
            </array>
        </property>

        <!--        List注入-->
        <property name="hobby">
            <list>
                <value>Code</value>
                <value>TV</value>
                <value>Phone</value>
            </list>
        </property>

        <!--        Map-->
        <property name="card">
            <map>
                <entry key="ID" value="121700"/>
                <entry key="pass" value="disney"/>
            </map>
        </property>

        <!--        Set-->
        <property name="game">
            <set>
                <value>王者荣耀</value>
                <value>和平精英</value>
            </set>
        </property>

        <!--        null值注入-->
        <!--        <property name="wife" value=""/>-->
        <property name="wife">
            <null/>
        </property>

        <!--
        Properties
            key = value
            key = value
            key = value
        -->
        <property name="info">
            <props>
                <prop key="userName">userName</prop>
                <prop key="passWord">passWord</prop>
            </props>
        </property>

    </bean>

    <bean id="address" class="com.pojo.Address">
        <property name="address" value="广州"/>
    </bean>

</beans>
```





## 6.3、拓展方式注入

p命名空间，依赖于第三方的约束

```markdown
 	 xmlns:p="http://www.springframework.org/schema/p"
 	 xmlns:c="http://www.springframework.org/schema/c"
```

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:c="http://www.springframework.org/schema/c"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--        p明明空间注入，可以直接注入属性的值：property-->
    <bean id="user" class="com.pojo.User" p:name="蛋蛋" p:age="23"/>

    <!--        c命名空间通过构造器注入-->
    <bean id="user2" class="com.pojo.User" c:age="24" c:name="disney"/>

</beans>wo
```

测试：

```java
import com.pojo.Student;
import com.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student = (Student) context.getBean("student");
        System.out.println(student);
        /*
        * Student{
        *   name='DisneyD33',
        *   address=Address{address='广州'},
        *   books=[JavaWeb, Web, Java],
        *   hobby=[Code, TV, Phone],
        *   card={ID=121700, pass=disney},
        *   game=[王者荣耀, 和平精英],
        *   wife='null',
        *   info={passWord=passWord, userName=userName}}
        * */

        User user = context.getBean("user",User.class);
        System.out.println(user);

        User user2 = context.getBean("user2",User.class);
        System.out.println(user2);

    }
}
```

注意：

​	c命名与p命名，需要导入XML约束



## 6.4、Bean的作用域

```markdown
    1. Singleton（单例作用域）
    2. Prototype（原型作用域（多例作用域））
    3. Request（请求作用域）
    4. Session（会话作用域）
    5. Application（全局作用域）
    6. Websocket（HTTP WebSocket作用域）
```



1. 单例模式（Spring默认机制）

   ```markdown
   <bean id="user2" class="com.pojo.User" c:age="24" c:name="disney" scope="singleton"/>
   ```

2. 原型模式

   每次使用都会创建一个新的对象

   ```markdown
   <bean id="user2" class="com.pojo.User" c:age="24" c:name="disney" scope="prototype"/>
   ```

   ```java
           User user2 = context.getBean("user2",User.class);
           User user3 = context.getBean("user2",User.class);
   //        System.out.println(user2);
           System.out.println(user2==user3);
   ```

3. 其余的request、session、application等只能在Web开发中使用



# 7、Bean的自动装配

- 自动装配式Spring满足Bean依赖一种方式
- Spring会在上下文中自动寻找，并自动给bean装配属性



在Spring中有三种装配的方式

1. 在xml中显示的配置
2. 在java中显示的配置
3. 隐式的自动装配bean【重点】



## 7.1、测试

【搭建环境】

- xml

  ```xml
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">
      <bean id="people" class="com.pojo.People">
          <property name="cat" ref="cat"/>
          <property name="dog" ref="dog"/>
          <property name="name" value="蛋蛋"/>
      </bean>
      <bean id="cat" class="com.pojo.Cat"/>
      <bean id="dog" class="com.pojo.Dog"/>
  
  </beans>
  ```

- 实体类

  ```java
  package com.pojo;
  
  public class Cat {
      public void shot(){
          System.out.println("喵喵喵~");
      }
  }
  ```

  ```java
  package com.pojo;
  
  public class Dog {
      public void shot(){
          System.out.println("汪汪汪~");
      }
  }
  ```

  ```java
  package com.pojo;
  
  public class People {
      private String name;
      private Dog dog;
      private Cat cat;
  
      @Override
      public String toString() {
          return "People{" +
                  "name='" + name + '\'' +
                  ", dog=" + dog +
                  ", cat=" + cat +
                  '}';
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public Dog getDog() {
          return dog;
      }
  
      public void setDog(Dog dog) {
          this.dog = dog;
      }
  
      public Cat getCat() {
          return cat;
      }
  
      public void setCat(Cat cat) {
          this.cat = cat;
      }
  }
  ```

- 测试类

  ```java
  import com.pojo.People;
  import org.junit.Test;
  import org.springframework.context.ApplicationContext;
  import org.springframework.context.support.ClassPathXmlApplicationContext;
  
  public class MyTest {
      @Test
      public void test1(){
          ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
          People people = context.getBean("people", People.class);
          people.getDog().shot();
          people.getCat().shot();
      }
  }
  ```





## 7.2、ByName自动装配

```xml
    <!--
    byName：会自动在容器上下文查找，和自己对象set方法后面的值队形的bean ID
    -->
    <bean id="people" class="com.pojo.People" autowire="byName">
        <property name="name" value="蛋蛋"/>
    </bean>
    <bean id="cat" class="com.pojo.Cat"/>
    <bean id="dog" class="com.pojo.Dog"/>
    <!--    无法自动装配-->
    <!--    <bean id="dog11" class="com.pojo.Dog"/>-->
```



## 7.3、ByType自动装配

```xml
    <!--
    byName：会自动在容器上下文查找，和自己对象set方法后面的值队形的bean ID
    byType：会自动在容器上下文查找，和自己对象属性类型相同的Bean Id
    -->
    <bean id="people" class="com.pojo.People" autowire="byType">
        <property name="name" value="蛋蛋"/>
    </bean>
    <bean id="cat" class="com.pojo.Cat"/>
    <!--    <bean id="dog" class="com.pojo.Dog"/>-->

    <!--    能跑-->
    <!--        <bean id="dog11" class="com.pojo.Dog"/>-->
    <bean class="com.pojo.Dog"/>
```



## 7.4、小结

- byName的时候，需要保证所有Bean的id唯一，并且这Bean需要和自动注入的属性的set方法的值一致
- byType的时候，需要保证所有Bean的class唯一，并且这个Bean需要和自动注入的属性的类型一致



## 7.5、使用注解实现自动装配

`JDK1.5支持注解，Spring2.5就开始支持！`

要使用注解须知：

1. 导入约束	context约束

2. 配置注解的支持   context:annotation-config/ 

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd">
   
   <!--    配置注解支持-->
       <context:annotation-config/> 
   </beans>
   ```

   

3. **@Autowired**

   直接在类上直接使用，可以忽略Set方式使用

   也可以在Set方式上使用

   使用Autowired可以不用编写Set方法，前提是自动装配的属性在容器上（IOC）存在，且符合名字byName

   ```java
       private String name;
       @Autowired
       private Dog dog;
       @Autowired
       private Cat cat;
   ```

4. 科普

   ```markdown
   @Nullable 	字段标记了这个注解，说明这个字段可以为null;
   ```

   ```java
       public People(@Nullable String name) {
           this.name = name;
       }
   ```

   ```java
       //	注解里有个值，设置为false，就可装配的时候不塞入值 默认为true
       public @interface Autowired {
           boolean required() default true;
       }
   ```

   ```java
       //	如果显示定义了Autowired的required属性为false，说明这个对象可以为null，否则不能为空
   	@Autowired(required = false)
       private Dog dog;
   ```

5. @Qualifier(value = "xxx")

   如果自动装配**@Autowired**的环境比较复杂，自动装配无法通过注解**@Autowired**完成的时候，我们可以使用@Qualifier(value = "xxx")去配置**@Autowired**的使用，指定一个唯一的bean对象注入！

   ```java
       @Autowired
       @Qualifier(value = "dog22")
   ```

   ```xml
       <bean id="people" class="com.pojo.People"/>
       <bean id="cat" class="com.pojo.Cat"/>
       <bean id="dog22" class="com.pojo.Dog"/>
   ```

6. @Resource

   java原生注解

   ```java
       @Resource
       private Dog dog;
       @Resource
       private Cat cat;
   ```

   加name属性来指定装配

   ```java
       @Resource(name = "xxx")
       private Dog dog;
   ```



## 7.6、小结

@Resource和@Autowired的区别：

- 都是用来自动装配的，都可以放在属性字段上
- @Autowired 通过byType的方式来实现，而且必须要求这个对象存在！【常用】
- @Resource 默认通过byName的方式实现，如果找不到名字，则通过byType，如果两个都找不到的情况下，就报错！【常用】
- 执行顺序不同：
  - @Autowired 通过byType的方式实现
  -  @Resource默认通过byName的方式实现





# 8、使用注解开发

在Spring4之后，要是用注解开发，必须要保证AOP的包导入了

```xml
    <dependencies>
        <dependency>
            <groupId>jakarta.annotation</groupId>
            <artifactId>jakarta.annotation-api</artifactId>
            <version>1.3.5</version>
        </dependency>
    </dependencies>
```

使用注解需要导入context约束，增加注解的支持

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">


    <!--    配置注解支持-->
    <context:annotation-config/>
</beans>
```



1. **bean**

2. **属性如何注入**

   ```java
   package com.pojo;
   
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.stereotype.Component;
   
   //  等价于  <bean id="user" class="com.pojo.User"/>
   //  @Component 组件
   @Component
   public class User {
   
       // 相当于<bean id="user" class="com.pojo.User">
       //      <property name="name" value="蛋蛋"/>
       //      </bean>
       @Value("蛋蛋")
       public String name;
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   }
   ```

3. 衍生的注解

   @Component 有几个衍生注解，在Web开发中，会按照MVC三层架构分层

   - dao	      【@Repository】
   - service     【@Service】
   - controller  【@Controller】

   这四个注解功能都是一样的，都是代表将某个类注册到Spring中，装配Bean

4. **自动装配**

   ```markdown
   - @Autowired 通过byType的方式来实现，而且必须要求这个对象存在！【常用】
   - @Resource 默认通过byName的方式实现，如果找不到名字，则通过byType，如果两个都找不到的情况下，就报错！【常用】
   - @Nullable 	字段标记了这个注解，说明这个字段可以为null;
   ```

5. **作用域**

   ```java
   @Scope("singleton")
   @Scope("prototype")
   ```

   ```java
   public @interface Scope {
       @AliasFor("scopeName")
       String value() default "";
   
       @AliasFor("value")
       String scopeName() default "";
   
       ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
   }
   ```

6. **小结**

   XML与注解：

   - ​	xml更加万能，使用于任何场合！维护简单方便
   - ​    注解不是自己的类使用不了，维护相对复杂

   xml与注解最佳实践：
   
   - ​	 xml用来管理Bean；
   
   - ​     注解只负责完成属性的注入；
   
   - ​     我们在使用的过程中，只需要注意一个问题：必须让注解生效，就需要开启注解的支持
   
     ```xml
         <!--    自定要扫描的包，这个包下的注解就会生效-->
         <context:component-scan base-package="com"/>
         <!--    配置注解支持-->
         <context:annotation-config/>
     ```



# 9、使用Java的方式配置Spring

完全不使用Spring的xml配置了，全权交给Java做

JavaConfig 是Spring的一个子项目，在Spring4之后，成为了一个核心功能！

pojo：

```java
package com.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//  这个注解的意思，就是说明这个类被Spring接管了，注册到了容器中
@Component
public class User {
    //  属性注入值
    @Value("蛋蛋")
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

config：

```java
package com.config;

import com.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//  本质跟Component一样
//  Configuration代表这个一个配置类，和beans.xml一样
@Configuration
@ComponentScan("com.pojo")
//  通过Import来引入别的Config
@Import(DisneyConfig.class)
public class UserConfig {

    //  注册一个Bean，就相当于之前写的一个Bean标签
    //  方法名就相当于Bean标签的id属性
    //  方法返回值，就相当于Bean标签的class属性
    @Bean
    public User myUser(){
        //  就是返回要注入到的Bean的对象
        return new User();
    }

}
```

测试类：

```java
import com.config.UserConfig;
import com.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        //  如果完全使用配置类方式去做，只能通过AnnotationConfigApplicationContext 上下文来获取容器，通过配置类的class对象加载
        ApplicationContext context = new AnnotationConfigApplicationContext(UserConfig.class);
        User myUser = context.getBean("myUser", User.class);
        System.out.println(myUser);
    }
}
```



小结：

```markdown
 	如果完全使用配置类方式去做，只能通过AnnotationConfigApplicationContext 上下文来获取容器，通过配置类的class对象加载
 	通过Import来引入别的Config类
```





# **10、代理模式**

`为什么要学习代理模式？`

因为这就是SpringAOP的底层

代理模式的分类：

- 静态代理
- 动态代理



## 10.1、静态代理

角色分析：

- 抽象角色：一般会使用接口或抽象类来解决
- 真实角色：被代理的角色
- 代理角色：代理真实角色，代理真实角色后，一般会做些附属操作
- 客户：访问代理对象的人



代码步骤：

1. 接口

   ```java
   package com.demo01;
   
   // 租房
   public interface Rent {
       public void rent();
   }
   ```

2. 真实角色

   ```java
   package com.demo01;
   
   //  房东
   public class Host implements Rent {
   
       @Override
       public void rent() {
           System.out.println("房东要出租房子");
       }
   }
   ```

3. 代理角色

   ```java
   package com.demo01;
   
   public class Proxy implements Rent{
       private Host host;
   
       public Proxy() {
       }
   
       public Proxy(Host host) {
           this.host = host;
       }
   
       @Override
       public void rent() {
           seeHouse();
           host.rent();
           free();
       }
   
       //  看房
       public void seeHouse(){
           System.out.println("中介带看房");
       }
   
       public void free(){
           System.out.println("收中介费");
       }
   }
   ```

4. 用户端访问代理角色

   ```java
   package com.demo01;
   
   public class Client {
       public static void main(String[] args) {
           Host host = new Host();
   //        host.rent();
           // 代理
           Proxy proxy = new Proxy(host);
           proxy.rent();
           proxy.seeHouse();
       }
   }
   ```

   

代理模式的好处：

- 可以使真实角色的操作更加纯粹！不用去关注一些公共的业务
- 公共也就交给代理角色！实现了业务的分工！
- 公共业务发生拓展的时候，方便集中管理！

缺点：

- 一个真实角色就会产生一个代理角色；开发效率会变低



## 10.2、加深理解

1. 创建抽象接口UserService

   ```java
   package com.demo02;
   
   public interface UserService {
       public void add();
       public void delete();
       public void update();
       public void query();
   }
   ```

2. 创建实体类UserServiceImpl

   ```java
   package com.demo02;
   
   public class UserServiceImpl implements UserService{
       @Override
       public void add() {
           System.out.println("增加一个用户");
       }
   
       @Override
       public void delete() {
           System.out.println("删除一个用户");
       }
   
       @Override
       public void update() {
           System.out.println("修改一个用户");
       }
   
       @Override
       public void query() {
           System.out.println("查询一个用户");
       }
   
       /*
       * 在此添加代码方法，改动原有的代码，可能会把业务逻辑改乱
       * */
   }
   ```

3. 代理对象

   需求：需要添加日志，在代理对象中添加，就不用修改原有的实体类对象代码

   ```java
   package com.demo02;
   
   public class UserServiceProxy implements UserService {
       UserServiceImpl userService;
   
       public void setUserService(UserServiceImpl userService) {
           this.userService = userService;
       }
   
       @Override
       public void add() {
           log("add");
           userService.add();
       }
   
       @Override
       public void delete() {
           log("delete");
           userService.delete();
       }
   
       @Override
       public void update() {
           log("update");
           userService.update();
       }
   
       @Override
       public void query() {
           log("query");
           userService.delete();
       }
   
       public void log(String str) {
           System.out.println("使用了" + str + "方法");
       }
   }
   ```

4. 测试类

   ```java
   package com.demo02;
   
   public class Client {
       public static void main(String[] args) {
           UserServiceImpl userService = new UserServiceImpl();
           UserServiceProxy userServiceProxy = new UserServiceProxy();
           userServiceProxy.setUserService(userService);
           userService.add();
       }
   }
   ```



## 10.3、动态代理

- 动态代理和静态代理角色一样
- 动态代理的代理类是动态生成的，不是直接写好的
- 动态代理分为两大类：基于接口的动态代理，基于类的动态代理
  - 基于接口 --- JDK动态代理  【原生JDK】
  - 基于类：cglib
  - java字节码实现：JavaSsist

需要了解两个类：Proxy【代理】、InvocationHandler【调用处理程序】

动态代理的好处：

- 可以使真实角色的操作更加纯粹！不用去关注一些公共的业务
- 公共也就交给代理角色！实现了业务的分工！
- 公共业务发生拓展的时候，方便集中管理！
- 一个动态代理类代理的是一个接口，一般就是对应的一类业务
- 一个动态代理类可以代理多个类，只要是实现了同一个接口即可



**InvocationHandler：**

1. 接口

   ```java
   package com.demo04;
   
   public interface UserService {
       public void add();
       public void delete();
       public void update();
       public void query();
   }
   ```

2. 实体类

   ```java
   package com.demo04;
   
   public class UserServiceImpl implements UserService {
       @Override
       public void add() {
           System.out.println("增加一个用户");
       }
   
       @Override
       public void delete() {
           System.out.println("删除一个用户");
       }
   
       @Override
       public void update() {
           System.out.println("修改一个用户");
       }
   
       @Override
       public void query() {
           System.out.println("查询一个用户");
       }
   
       /*
       * 1.改动原有的代码，可能会把业务逻辑改乱
       * */
   }
   ```

3. InvocationHandler动态代理类

   ```java
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
   ```

4. 客户端

   ```java
   package com.demo04;
   
   public class Client {
       public static void main(String[] args) {
           //  真实角色
           UserServiceImpl userService = new UserServiceImpl();
   
           //  代理角色，不存在
           ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler();
           //  设置要代理的对象
           proxyInvocationHandler.setTarget(userService);
           //  动态生成代理类
           UserService proxy = (UserService) proxyInvocationHandler.getProxy();
           proxy.delete();
       }
   }
   ```



# 11、AOP

## 11.1、什么是AOP

`在软件业，AOP为Aspect Oriented Programming的缩写，意为：面向切面编程，通过预编译方式和运行期间动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。`

![image-20231108215501786](K:\JAVA_SSM\Spring.assets\image-20231108215501786.png)



## 11.2、AOP在Spring中的使用

```markdown
	提供生命式事务：允许用户自定义切面
```

- **横切关注点：**跨越应用程序多个模块的方法或功能。即是，与我们业务逻辑无关的，但是我们需要关注的部分，就是横切关注点。如日志，安全，缓存，事务等等。
- **切面（ASPECT）：**横切关注点被模块化的特殊对象。即，它是一个类。
- **通知（Advice）：**切面必须要完成的工作，即，它是类中的一个方法。
- **目标（Target）：**被通知对象。
- **代理（Proxy）：**向目标对象应用通知后创建的对象。
- **切入点（PointCut）：**切面通知执行的“地点”的定义。
- **连接点（JoinPoint）：**与切入点匹配的执行点。

![aop](K:\JAVA_SSM\Spring.assets\aop-1699452424085.png)

SpringAOP中，通过Advice（通知）定义横切逻辑，Spring中支持5种类型的Advice：

（1）前置通知（Before）： 在方法（切点）执行前添加功能（执行通知）

（2）后置通知（After returning）： 在方法（切点）执行后添加功能

（3）异常通知（After throwing）： 在方法抛出异常后添加功能

（4）最终通知（After）： 无论方法是否执行异常，都会执行该通知，相当于异常中的finally

（5）环绕通知（Around）： 包围一个连接点，在方法执行前和后都添加功能

即AOP在不改变原有的代码下，去增加新的功能；



## 11.3、使用Spring实现AOP

【重点】在使用AOP之前，需要导入一个依赖包

```xml
	<dependencies>
		<dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.4</version>
        </dependency>
    </dependencies>
```

### **方式一：使用Spring的API接口【主要SpringAPI接口实现】**

**XML：**

```xml
   <!--    配置AOP
        需要导入AOP的约束
        方式一：使用原生Spring API接口
    -->
    <aop:config>
        <!--        切入点
            expression：表达式
            execution()：要执行的位置
        -->
        <aop:pointcut id="pointcut" expression="execution(* com.service.UserServiceImpl.*(..))"/>

        <!--        执行话那要增加！-->
        <aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
        <aop:advisor advice-ref="beforeLog" pointcut-ref="pointcut"/>

    </aop:config>
```

**Log：**

```java
package com.log;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class AfterLog implements AfterReturningAdvice {

    /*
     *  returnValue：返回值
     *  */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("执行了：" + method.getName() + "的方法！" + "返回结果为：" + returnValue);
    }
}
```

```java
package com.log;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class BeforeLog implements MethodBeforeAdvice {

    /*
     * method：要执行的目标对象的方法
     * objects：参数
     * o：目标对象
     * */
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println(o.getClass().getName() + "的" + method.getName() + "被执行了！");
    }
}
```

实体类：

```java
package com.service;

public class UserServiceImpl implements UserService{
    @Override
    public void add() {
        System.out.println("增加一个用户");
    }

    @Override
    public void delete() {
        System.out.println("删除一个用户");
    }

    @Override
    public void update() {
        System.out.println("更新一个用户");
    }

    @Override
    public void query() {
        System.out.println("查询一个用户");
    }
}
```

接口：

```java
package com.service;

public interface UserService {
    public void add();
    public void delete();
    public void update();
    public void query();
}
```

测试类：

```java
import com.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        //  动态代理的是接口
        UserService userService = context.getBean("userService", UserService.class);
        userService.add();
    }
}
```



### **方式二：自定义类来实现AOP【主要是切面定义】**

**XML：**

```xml
    <!--     方式二：自定义类-->
    <bean id="diy" class="com.utils.diyPointCut"/>
    <aop:config>
        <!--    自定义切面，ref 要引用的类-->
        <aop:aspect ref="diy">
            <!--    切入点-->
            <!--    execution(<修饰符模式>?<返回类型模式><方法名模式>(<参数模式>)<异常模式>?)-->
            <aop:pointcut id="point" expression="execution(* com.service.UserServiceImpl.*(..))"/>
            <!--    通知-->
            <aop:before method="BeforeLog" pointcut-ref="point"/>
            <aop:after method="AfterLog" pointcut-ref="point"/>
        </aop:aspect>
    </aop:config>
```





### **方式三：使用注解实现**

**代理类：**

```java
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
```

**XML：**

```xml
    <!--    方式三-->
    <bean id="annotationPointCut" class="com.utils.AnnotationPointCut"/>
    <!--    开启注解支持-->
    <aop:aspectj-autoproxy/>
```





# 12、整合Mybatis

步骤：

1. 导入相关jar包

   - junit

   - mybatis

   - mysql数据库

   - spring相关的

   - aop

   - mybatis - spring

     ```xml
         <dependencies>
             <dependency>
                 <groupId>junit</groupId>
                 <artifactId>junit</artifactId>
                 <version>4.12</version>
             </dependency>
     
             <dependency>
                 <groupId>mysql</groupId>
                 <artifactId>mysql-connector-java</artifactId>
                 <version>8.0.11</version>
             </dependency>
     
             <dependency>
                 <groupId>org.mybatis</groupId>
                 <artifactId>mybatis</artifactId>
                 <version>3.5.6</version>
             </dependency>
     
             <dependency>
                 <groupId>org.springframework</groupId>
                 <artifactId>spring-webmvc</artifactId>
                 <version>5.3.13</version>
             </dependency>
     
             <dependency>
                 <groupId>org.springframework</groupId>
                 <artifactId>spring-jdbc</artifactId>
                 <version>5.3.17</version>
             </dependency>
     
             <dependency>
                 <groupId>org.aspectj</groupId>
                 <artifactId>aspectjweaver</artifactId>
                 <version>1.9.7</version>
             </dependency>
     
             <dependency>
                 <groupId>org.mybatis</groupId>
                 <artifactId>mybatis-spring</artifactId>
                 <version>2.0.5</version>
             </dependency>
     
             <dependency>
                 <groupId>org.projectlombok</groupId>
                 <artifactId>lombok</artifactId>
                 <version>1.18.28</version>
             </dependency>
         </dependencies>
     ```

2. 编写配置文件

3. 测试



## 12.1、回忆Mybatis

1. 编写实体类
2. 编写核心配置文件
3. 编写接口
4. 编写Mapper.xml
5. 测试



## 12.2、Mybatis - Spring

`https://mybatis.org/spring/zh/index.html`

1. 编写数据源
2. sqlSessionFactory
3. sqlSessionTemplate
4. 需要给接口加实现类【Impl】
5. 将实现类，注入到Spring中
6. 测试使用



**Spring-dao.xml：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--
        DataSource
        使用Spring的数据源替换Mybatis的配置 c3p0 dbcp druid
        这里使用Spring提供的JDBC
    -->

    <bean id="datasource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true&amp;allowMultiQueries=true&amp;useSSL=true&amp;serverTimezone=Asia/Shanghai&amp;useSSL=false"/>
        <property name="username" value="root"/>
        <property name="password" value="123456"/>
    </bean>


    <!--    SqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="datasource" />
        <!--绑定Mybatis配置文件-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <property name="mapperLocations" value="classpath:com/mapper/*.xml"/>
    </bean>

    <!--    SqlSessionTemplate：就是使用的sqlSession-->
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <!--只能使用构造器注入sqlSessionFactory，因为没有set方法-->
        <constructor-arg index="0" ref="sqlSessionFactory"/>
    </bean>

    <bean id="userMapper" class="com.mapper.UserMapperImpl">
        <property name="sqlSession" ref="sqlSession"/>
    </bean>
</beans>
```

**UserMapperImpl：**

```java
package com.mapper;

import com.pojo.User;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class UserMapperImpl implements UserMapper{
    /**
     * 所有操作，都是用sqlSession来操作，现在使用SqlSessionTemplate；
     * @return
     */
    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<User> selectUser() {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.selectUser();
        return users;
    }
}

```

**测试类：**

```java
@Test
public void test1(){
    ApplicationContext context = new ClassPathXmlApplicationContext("Spring-dao.xml");
    UserMapper userMapper = context.getBean("userMapper", UserMapper.class);
    for (User user : userMapper.selectUser()) {
        System.out.println(user);
    }
```





# 13、声明式事务

## 1、事务

- 要么都成功，要么都失败
- 事务在项目中十分重要，涉及到数据的一致性
- 确保完整性和一致性

事务ACID原则：

- 原子性
- 一致性
- 隔离性
  - 多个业务可能操作同一个资源，防止数据损坏
- 持久性
  - 事务一旦提交，无论系统发生问题，结果都不会再被影响，被持久化存储

```markdown
    一个使用MyBatis—Spring的其中一个主要原因是他允许MyBatis参与到Spring的事务管理中。而不是给MyBatis创建一个新的专用事物管理器，MyBatis-Spring借助了Spring中的DataSourceTransactionManager来实现事务管理。
```



## 2、Spring中的事务管理

- 声明式事务：AOP（切面）
- 编程式事务：需要在代码中，进行事务的管理

```xml
    <!--
        配置声明式事务
        要开启 Spring 的事务处理功能，在 Spring 的配置文件中创建一个 DataSourceTransactionManager 对象：
    -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="datasource"/>
    </bean>

    <!--    结合AOP实现事务的植入-->
    <!--    配置事务的类
        tx:advice   配置事务通知
    -->
    <tx:advice id="txADVICE" transaction-manager="transactionManager">
        <!--        给那些方法配置事务-->
        <!--        配置事务的传播特性
            propagation ：默认（REQUIRED）
        -->
        <tx:attributes>
            <tx:method name="add" propagation="REQUIRED"/>
            <tx:method name="delete" propagation="REQUIRED"/>
            <tx:method name="update" propagation="REQUIRED"/>
            <tx:method name="query" read-only="true"/>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>

    <!--配置事务切入-->
    <aop:config>
        <aop:pointcut id="txPoint" expression="execution(* com.mapper.*.*(..))"/>
        <aop:advisor advice-ref="txADVICE" pointcut-ref="txPoint"/>
    </aop:config>
```

![image-20231114170840340](K:\JAVA_SSM\Spring.assets\image-20231114170840340.png)



**思考：**

为什么需要事务？

- 如果不配置事务，可能给存在数据提交不一致的情况
- 如果不在Spring中去配置声明式事务，就需要在代码中手动配置事务
- 事务在项目的开发中，十分重要，涉及到数据的一致性和完整性的问题
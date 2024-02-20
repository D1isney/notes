# MyBatis

------

## 1、简介

### 1.1、什么是Mybatis

- MyBatis是一款优秀的**持久层框架**
- 它支持定制化SQL、存储过程以及高级映射。
- MyBatis避免了几乎所有的JDBC代码和手动设置参数以及获取结果集。
- MyBatis可以使用简单的XML或注解来配置和映射原生类型、接口和Java的Pojo（Plain Old Java Object，普通老式Java对象）为数据库中的记录。
- MyBatis本是apache的一个开源项目iBatis，2010年这个项目由apache software foundation迁移到了google code，并且改名为MyBatis。
- 2013年11月迁移到Github。



如何获得MyBatis？

- maven仓库
- Github：https://github.com/mybatis/mybatis-3
- 中文文档：https://mybatis.org/mybatis-3/zh/index.html



### 1.2、持久化

数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程
- 内存：**断电即失（RAM）**
- 数据库（jdbc）、io文件持久化。
- 生活：冷藏、罐头

**为什么需要持久化？**

- 有一些对象，不能让它丢掉。
- 内存太贵了



### 1.3、持久层

Dao层、Service层、Controller层....

- 完成持久化工作的代码块
- 层界限十分明显





### 1.4、为什么需要Mybatis？

- 帮助程序员将数据存入到数据库中
- 方便
- 传统的JDBC代码太复杂、简化、框架、自动化
- 不用Mybatis也可以、更容易上手
- 优点：
  - 简单易写
  - 灵活
  - 解除sql与程序代码的耦合
  - 提供映射标签
  - 提供对象关系映射标签
  - 提供xml标签





## 2、第一个Mybatis程序

思路：搭建环境 --> 导入Mybatis --> 编写代码 --> 测试

### 2.1、搭建环境

```java
CREATE TABLE `user` (
  `id` int NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `pwd` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user`(`id`,`name`,`pwd`)VALUES
    (1,'张三','123'),
	(2,'李四','123'),
	(3,'王五','123')
```

新建项目

1. 新建一个普通的maven项目

2. 删除src目录

3. 导入maven依赖

   ```xml
       <!--    导入依赖-->
       <dependencies>
   <!--        mysql驱动-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>8.0.11</version>
           </dependency>
   <!--        mybatis-->
           <dependency>
               <groupId>org.mybatis</groupId>
               <artifactId>mybatis</artifactId>
               <version>3.5.2</version>
           </dependency>
   <!--        junit-->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.12</version>
               <scope>test</scope>
           </dependency>
       </dependencies>
   ```



### 2.2、创建一个模块

- 编写mabatis的核心配置文件

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-config.dtd">
  <!--核心配置文件-->
  <configuration>
  
      <environments default="development">
          <environment id="development">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
                  <property name="driver" value="com.mysql.jdbc.Driver"/>
                  <property name="url" value="jdbc://mysql://localhost:3306/mybatis?useSSL=true&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                  <property name="username" value="root"/>
                  <property name="password" value="123456"/>
              </dataSource>
          </environment>
          <environment id="test">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
                  <property name="driver" value="${driver}"/>
                  <property name="url" value="${url}"/>
                  <property name="username" value="${username}"/>
                  <property name="password" value="${password}"/>
              </dataSource>
          </environment>
      </environments>
  </configuration>
  ```

- 编写mybatis的工具类

```java
package com.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//sqlSessionFactory --> sqlSession
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 既然有了SqlSessionFactory，顾名思义，我们就可以从中获得SqlSession的实例了。
    // SqlSession完全包含了面向数据库执行SQL命令所需要的所要方法。
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

//
}
```





### 2.3、编写代码

- 实体类

```java
package com.pojo;

//实体类
public class User {
    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

```

- Dao接口

```java
package com.dao;

import com.pojo.User;

import java.util.List;

public interface UserDao {
    List<User> getUserList();
}

```

- 接口实现类

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace=绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.dao.UserDao">
    <!--    select查询语句-->
    <select id="getUserList" resultType="com.pojo.User">
        select * from mybatis.user
    </select>

</mapper>
```



### 2.4、测试

注意点

org.apache.ibatis.binding.BindingException: Type interface com.dao.UserDao is not known to the MapperRegistry.

**MapperRegistry是什么？**

核心配置文件中注册mappers

- junit测试

```xml
<!--    在build中配置resources，防止资源到处失败的问题-->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

```java
package com.dao;

import com.pojo.User;
import com.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class UserDaoTest {

    @Test
    public void test() {
        //  第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //  执行SQ
        //  方式一
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();

        for (User user: userList
             ) {
            System.out.println(user);
        }

        //  关闭sqlSession
        sqlSession.close();

    }

}

```

问题：

1. 配置文件没有注册
2. 绑定接口错误
3. 方法名不对
4. 返回类型不对
5. Maven导出资源问题





## 3、CRUD

### 3.1、namespace

namespace中的问题要和Dao/Mapper接口名一致



### 3.2、interface

```java
package com.dao;

import com.pojo.User;

import java.util.List;

public interface UserMapper {
    //    查询所有用户
    List<User> getUserList();

    //    根据ID查询用户
    User getUserById(int id);

    //    insert一个用户
    int addUser(User user);

    //  修改
    int updateUser(User user);

    //    删除
    int deleteUser(int id);
}

```







### 3.3、select

选择、查询语句；

- id：就是对应的namespace中的方法名；
- resultType：Sql语句执行的返回值
- parameterType：参数类型

```xml
    <!--    select查询语句-->
    <select id="getUserList" resultType="com.pojo.User">
        select * from mybatis.user
    </select>
```

```java
    @Test
    public void test() {
        //  第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try {
            //  执行SQLq
            //  方式一
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User user : userList
            ) {
                System.out.println(user);
            }
            System.out.println();

            //  方式二
            List<User> objectList = sqlSession.selectList("com.dao.UserMapper.getUserList");
            for (User user : objectList) {
                System.out.println(user);
            }

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            //  关闭sqlSession
            sqlSession.close();
        }
    }
```



### 3.4、insert

增删改都需要提交事务

```java
    @Test
    public void addUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.addUser(new User(4, "蛋蛋", "121700"));
        if (res > 0) {
            System.out.println("插入成功！");
        }
        //  提交事务
        sqlSession.commit();
        sqlSession.close();
    }
```

```xml
    <!--    对象中的属性可以直接取出来-->
    <insert id="addUser" parameterType="com.pojo.User">
        insert into mybatis.user (id,name,pwd) values(#{id},#{name},#{pwd});
    </insert>
```



### 3.5、update

增删改都需要提交事务

```xml
    <!--    修改-->
    <update id="updateUser" parameterType="com.pojo.User">
        update mybatis.user set name=#{name},pwd=#{pwd} where id = #{id};
    </update>
```

```java
    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser(new User(4,"Disney蛋蛋","001217"));
        //	提交事务
        sqlSession.commit();
        sqlSession.close();
    }
```





### 3.6、delete

增删改都需要提交事务

```java
    @Test
    public void deleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser(4);
        //	提交事务
        sqlSession.commit();
        sqlSession.close();;
    }
```

```xml
	<!--    删除-->
    <delete id="deleteUser" parameterType="int">
        delete from mybatis.user where id = #{id}
    </delete>
```



注意点：**增删改需要提交事务！**





### 3.7、分析错误

1. xml文件标签不匹配
2. resource绑定mapper，需要使用路径！
3. 程序配置文件必须符合规范！
4. 空指针异常 NullPointerException，没有注册到SqlSession
5. 输出的xml存在中文乱码
6. Maven资源没有导出问题





### 3.8、Map

通过使用map，就不需要每次传参都传User

假设：实体类或者数据库中的表，字段或参数过多，我们应当使用map

**通过map插入：**

```java
    //  map插入
    int addUser2(Map<String, Object> map);
```

```xml
    <!--    对象中的属性可以直接取出来-->
    <insert id="addUser2" parameterType="map">
        insert into mybatis.user (id,name,pwd) values(#{userId},#{userName},#{passWord});
    </insert>
```

```java
    @Test
    public void addUser2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<>();
        map.put("userId",5);
        map.put("userName","123");
        map.put("passWord","123");
        mapper.addUser2(map);
        sqlSession.commit();
        sqlSession.close();
    }
```

------

**通过map查询：**

```xml
    <!--    使用map去查找-->
    <select id="getUserById2" parameterType="map" resultType="com.pojo.User">
        select * from mybatis.user where id = #{userId} and name = #{userName}
    </select>
```

```java
    //    使用map查找
    User getUserById2(Map<String, Object> map);
```

```java
    //    通过map去查询
    @Test
    public void getUserById2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String,Object> map = new HashMap<>();
        map.put("userName","张三");
        map.put("userId",1);
        User userById2 = mapper.getUserById2(map);
        System.out.println(userById2);
        sqlSession.commit();
        sqlSession.close();
    }
```

------

Map传递参数，直接在sql中取出key即可		parameterType=”map“

对象传递参数，直接在sql中取对象的属性即可		parameterType=”Object“

只有一个基本类型参数的情况下，可以直接在sql中取到

多个参数用map，**或者注解**





### 3.9、模糊查询

1. java代码执行的时候，传递通配符% %

   ```java
           List<User> users = mapper.gerUserLike("%蛋蛋%");
   ```

2. sql语句中拼写

   ```xml
           select * from mybatis.user where name like "%"#{value}"%"
   ```





## 4、配置解析

### 4.1、核心配置文件

- mybatis-config.xml
- MyBatis 的配置文件包含了会深深影响 MyBatis 行为的设置和属性信息。
- configuration（配置）
  - properties（属性）
  - settings（设置）
  - typeAliases（类型别名）
  - typeHandlers（类型处理器）
  - objectFactory（对象工厂）
  - plugins（插件）
  - environments（环境配置）
    - environment（环境变量）
      - transactionManager（事务管理器）
      - dataSource（数据源）
  - databaseIdProvider（数据库厂商标识）r)
  - mappers（映射器）



### 4.2、配置环境

MyBatis 可以配置成适应多种环境。

**不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**

环境多选一，修改默认环境，选择环境id切换到另外一个环境

```xml
<environments default="test">
    <environment id="development"></environment>
    <environment id="test"></environment>
</environments>
```

- **事务管理器（transactionManager）**

  JDBC - 这个配置就是直接使用了JDBC的提交和回滚设置，它依赖于数据源得到的连接来管理事务作用域。 

  ```xml
  <transactionManager type="JDBC">
    <property name="skipSetAutoCommitOnClose" value="true"/>
  </transactionManager>
  ```

  MANAGED - 这个配置几乎没做什么。它从不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如 JEE 应用服务器的上下文）。 默认情况下它会关闭连接。然而一些容器并不希望连接被关闭，因此需要将 closeConnection 属性设置为 false 来阻止默认的关闭行为。例如:

  ```xml
  <transactionManager type="MANAGED">
    <property name="closeConnection" value="false"/>
  </transactionManager>
  ```

  **如果你正在使用 Spring + MyBatis，则没有必要配置事务管理器，因为 Spring 模块会使用自带的管理器来覆盖前面的配置。**

  **Mybatis默认的事务管理器就是JDBC**

  

- **数据源（dataSource）**

  连接数据库 	dbcp	c3p0	druid（德鲁伊）

  **默认的连接池就是POOLED**



### **4.3、属性（properties）**

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。

编写一个db.properties

```properties
driver = com.mysql.cj.jdbc.Driver
url = jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&allowMultiQueries=true&useSSL=true&serverTimezone=Asia/Shanghai&useSSL=false
username = root
password = 123456
```

**在XML中，所有的标签都可以规定其顺序**

在核心配置文件中映入：

```xml
    <!--    引入外部配置文件-->
    <properties resource="db.properties">
        <property name="username" value="root"/>
        <!-- 优先走里面的属性 可以使用这里的属性-->
        <property name="pwd" value="123456"/>
    </properties>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${pwd}"/>
            </dataSource>
        </environment>
	</environments>
```

- 可以直接引入外部文件
- 可以在其中增加一些属性配置
- 如果有两个文件有同一个字段，优先使用外部配置文件



### 4.4、类型别名（typeAliases）

- 类型别名可为 Java 类型设置一个缩写名字。 

- 它仅用于 XML 配置，意在降低冗余的全限定类名书写。

  ```xml
      <!--    类型别名-->
      <!--    给实体类起别名-->
      <!--    xml配置的类-->
      <typeAliases>
          <!--        别名-->
  <!--        <typeAlias type="com.pojo.User" alias="User"></typeAlias>-->
          
          <!--        扫描一个实体的包 默认别名就为User = user-->
          <package name="com.pojo"/>
      </typeAliases>
  ```

在实体类比较少的时候实用第一种。

如果实体类十分多，建议使用第二种。

第一种可以DIY别名。

第二种只可以使用注解起别名。

```java
@Alias("author")
public class Author {
    ...
}
```





### 4.5、设置（Setting）

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。

| 设置名             | 描述                                                         | 有效值                                                       | 默认值 |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | :----- |
| cacheEnabled       | 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。     | true \| false                                                | true   |
| lazyLoadingEnabled | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | true \| false                                                | false  |
| logImpl            | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。        | SLF4J \| LOG4J（3.5.9 起废弃） \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置 |



### 4.6、其他配置

- typeHandlers（类型处理器）
- objectFactory（对象工厂）
- plugins（插件）
  - mybatis-generator-core
  - mybatis-plus
  - 通用mapper





### 4.7、映射器（Mappers）

MapperRegistry：注册绑定Mapper文件

方式一：【推荐使用】

```xml
    <!--    每一个mapper.xml都需要在mybatis核心配置文件中注册-->
    <mappers>
        <mapper resource="com/dao/UserMapper.xml"/>
    </mappers>
```

方式二：

```xml
    <mappers>        
		<mapper class="com.dao.UserMapper"/>
	</mappers>
```

使用class文件绑定注册：

1. 接口和Mapper配置文件必须同名
2. 接口和Mapper配置文件必须在同一个包下

方式三：

```xml
    <mappers>
        <package name="com.dao"/>
    </mappers>
```

使用扫描包进行注入绑定：

1. 接口和Mapper配置文件必须同名
2. 接口和Mapper配置文件必须在同一个包下





### 4.8、生命周期和作用域

不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

**SqlSessionFactoryBuilder：**

- 一旦创建了SqlSessionFactory，就不再需要它了。
- 局部变量

**SqlSessionFactory：**

- 说白了就是可以想象为：数据库连接池
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。
- 因此 SqlSessionFactory 的最佳作用域是应用作用域。
- 最简单的就是使用单例模式或者静态单例模式。

**SqlSession：**

- 连接到连接池的请求
- 关闭SqlSession
- 每个线程都应该有它自己的 SqlSession 实例。
- SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。
- 用完之后需要赶紧关闭，否则资源被占用！



## 5、ResultMap

解决属性名和字段名不一致的问题

数据库中的字段

```mysql
CREATE TABLE `user` (
  `id` int NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `pwd` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

实体类中的字段

```java
    private int id;           
    private String name;      
    private String password;  
```

test：

```java
    @Test
    public void getUserList(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById);
        sqlSession.close();
    }
```

User{id=1, name='张三', password='null'}

```mysql
select * from mybatis.user where id = #{id}
select id,name,pwd from mybatis.user where id = #{id}
```

**解决：**

1. 起别名

   ```mysql
   select id,name,pwd as password from mybatis.user where id = #{id}
   ```

2. resultMap

   结果集映射

   ```txt
   id		name		pwd
   id		name		password
   ```

   ```xml
       <!--    结果集映射-->
       <resultMap id="UserMap" type="com.pojo.User">
           <!--        column数据库中的字段，property实体类中的属性-->
           <result column="id" property="id"></result>
           <result column="name" property="name"></result>
           <result column="pwd" property="password"></result>
       </resultMap>
       <select id="getUserById" resultMap="UserMap">
           <!--        select * from mybatis.user where id = #{id}-->
           select id,name,pwd from mybatis.user where id = #{id}
       </select>
   ```

   - `resultMap` 元素是 MyBatis 中最重要最强大的元素。
   - ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。

   

   ## 

## 6、日志

### 6.1、日志工厂

- SLF4J
- LOG4J       【掌握】
- JDK_LOGGING
- COMMONS_LOGGING
- STDOUT_LOGGING      【掌握】
- NO_LOGGING

在Mybatis中具体使用那个日志，在设置中设定！



**STDOUT_LOGGING标准日志输出：**

在Mybatis核心文件中，配置日志

```xml
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
```

控制台输出：

```
pening JDBC Connection
Created connection 258112787.
Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@f627d13]
==>  Preparing: select id,name,pwd from mybatis.user where id = ? 
==> Parameters: 1(Integer)
<==    Columns: id, name, pwd
<==        Row: 1, 张三, 123456
<==      Total: 1
User{id=1, name='张三', password='123456'}
Resetting autocommit to true on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@f627d13]
Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@f627d13]
Returned connection 258112787 to pool.
```



### **6.2、LOG4J**

**什么是LOG4J？**

- Log4j是Apache的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是控制台、文件、GUI组件，甚至是套接口服务器、NT的事件记录器、UNIX Syslog守护进程等；

- 我们也可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。
- 最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。



1、先导入log4j的包

```xml
    <dependencies>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>
    </dependencies>
```

2、log4j.properties

```properties
#将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n

#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/Mybatis.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

3、配置log4j为日志的实现

```xml
    <settings>
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

4、log4j的使用

直接测试巡行查询

```
[org.apache.ibatis.transaction.jdbc.JdbcTransaction]-Opening JDBC Connection
[org.apache.ibatis.datasource.pooled.PooledDataSource]-Created connection 2058135834.
[org.apache.ibatis.transaction.jdbc.JdbcTransaction]-Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@7aaca91a]
[com.dao.UserMapper.getUserById]-==>  Preparing: select id,name,pwd from mybatis.user where id = ? 
[com.dao.UserMapper.getUserById]-==> Parameters: 1(Integer)
[com.dao.UserMapper.getUserById]-<==      Total: 1
User{id=1, name='张三', password='123456'}
```



**简单实用：**

1. 在要使用Log4j的类中，导入包 import org.apache.log4j.Logger;

2. 日志对象，参数为当前类的class

   ```java
     //    提升作用域
       static Logger logger = Logger.getLogger(UserDaoTest.class);
   ```

3. 日志级别

   ```java
           logger.info("info:进入了testLog4j");
           logger.debug("debug:进入了testLog4j");
           logger.error("error:进入了testLog4j");
   ```





## 7、分页

**思考：为什么要分页？**

- 减少数据的处理量

### **7.1、使用Limit分页**

```sql
select * from user limit startIndex,pageSize;
select * from user limit 0,2;
```

每页显示**2**个，从第**0**个开始查



使用Mybatis实现分页，核心SQL

1. 接口

   ```java
       //      分页实现查询
       List<User> getUserByLimit(Map<String, Integer> map);
   ```

2. Mapper.XML

   ```xml
   <select id="getUserByLimit" resultMap="UserMap" parameterType="map" resultType="com.pojo.User">
       select * from user limit #{startIndex},#{pageSize}
   </select>
   ```

3. 测试

```java
    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("startIndex",0);
        map.put("pageSize",2);
        List<User> userByLimit = mapper.getUserByLimit(map);

        for (User user : userByLimit) {
            System.out.println(user);
        }
        
        sqlSession.close();
    }
```



### 7.2、RowBounds分页

不再使用SQl实现分页

1、测试

```java
    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

//        RowBounds 对象
        RowBounds bounds = new RowBounds(1,2);

//        通过Java代码层面实现分页
        List<Object> objects = sqlSession.selectList("com.dao.UserMapper.getUserByRowBounds",null,bounds);
        for (Object object : objects) {
            System.out.println(object);
        }

        sqlSession.close();
    }
```

2、mapper.xml

```xml
    <!--    分页-->
    <select id="getUserByRowBounds" resultMap="UserMap">
        select * from user
    </select>
```

3、接口

```java
    //    分页2
    List<User> getUserByRowBounds();
```



### 7.3、插件分页

Mybatis分页插件PageHelper

```
https://pagehelper.github.io/
```





## 8、使用注解开发

### 8.1、面向接口编程

- 在一个面向对象的系统中，系统的各种功能是由许许多多的不同对象协作完成的。在这种情况下，各个对象内部是如何实现自己的,对系统设计人员来讲就不那么重要了；而各个对象之间的协作关系则成为系统设计的关键。小到不同类之间的通信，大到各模块之间的交互，在系统设计之初都是要着重考虑的，这也是系统设计的主要工作内容。面向接口编程就是指按照这种思想来编程。
- 根本原因：解耦，可拓展，提高服用，分层开发中，上层不用管具体的实现，大家都遵守共同的标准，是的开发变得容易，规范性更好。

**关于接口的理解**

- 接口从更深层次的理解，应是定义（规范，约束）与实现（名实分离的原则）的分离。
- 接口的本身反映了系统设计人员对系统的抽象理解。
- 接口应有两类：
  - 第一类是对一个个体的抽象，它可对应的一个抽象体（abstract class）；
  - 第二类是对一个个体某一方面的抽象，即形成一个抽象面（interface）；
- 一个个体有可能有多个抽象面。抽象体与抽象面是有区别的。



### 8.2、面向注解开发

1. 注解在接口上实现

   ```java
   @Select("select * from user")
   List<User> getUser();
   ```

2. 需要核心配置文件中绑定接口

```xml
<mappers>
    <mapper class="com.dao.UserMapper"/>
</mappers>
```
 3. 测试

    ```java
        @Test
        public void getUser() {
            SqlSession sqlSession = MybatisUtils.getSqlSession();
            //  底层主要应用反射
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> user = mapper.getUser();
            for (User user1 : user) {
                System.out.println(user1);
            }
    
            sqlSession.close();
    
        }
    ```

本质：反射机制实现

底层：动态代理



### 8.3、CRUD

可以在工具类创建的时候实现自动提交事务

```java
    public static SqlSession getSqlSession() {
        //  自动提交事务
        return sqlSessionFactory.openSession(true);
    }
```

1、接口

```java
    @Select("select * from user")
    List<User> getUser();

    //  方法存在多个参数，所有参数前必须加上@Param
    @Select("select * from user where id = #{id}")
    User getUserById05(@Param("id") int id);

    @Insert("insert into user(id,name,pwd) value (#{id},#{name},#{password})")
    int addUser(User user);

    @Update("update user set name=#{name},pwd=#{password} where id = #{id}")
    int updateUser(User user);

    @Delete("delete from user where id=#{id}")
    int deleteUser(@Param("id") int id);
```

2、测试类

```java
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //  底层主要应用反射
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        List<User> user = mapper.getUser();
//        for (User user1 : user) {
//            System.out.println(user1);
//        }

        //  通过id查找
//        User userById = mapper.getUserById05(1);
//        System.out.println(userById);

        //  添加
//        int i = mapper.addUser(new User(7, "", ""));

        //  修改
//        mapper.updateUser(new User(7,"dandan","31"));

        //  删除
        mapper.deleteUser(7);
        sqlSession.close();
```

注意：【接口注册绑定到核心配置文件中！】



### 8.4关于@Param（）注解

- 基本类型的参数或者String类型，需要加上
- 引用类型不需要加
- 如果只有一个基本类型的话，可以忽略，但是建议加上
- SQL引用的就是@Param（）中设定的属性名

**#{}与&{}区别**

- #{}表示一个占位符号
  - 通过#{}可以实现preparedStatement向占位符中设置值，自动进行Java类型和jdbc类型转换
  - #{}可以有效的防止SQL注入
  - #{}可以接收建磊类型值或者pojo属性值
  - 如果parameterType传给单个简单类型值，#{}括号中可以是value或其他名称
- ${}表示拼接SQL串
  - 通过${}可以将parameterType传入的内容拼接在SQL中而不进行jdbc类型转换
  - ${}可以接收简单类型值或pojo属性值，如果parameterType传输单个简单类型值，${}中只能是value





## 9、Lombok

- Java库
- 插件
- 工具

使用步骤：

1. IDEA中安装Lombok插件

2. 项目中导入lombok的jar包

   ```xml
       <dependencies>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>1.18.10</version>
           </dependency>
       </dependencies>
   ```

3. ```
   实体类上加注解
   @Getter and @Setter
   @FieldNameConstants
   @ToString
   @EqualsAndHashCode
   @AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
   @Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
   @Data
   @Builder
   @SuperBuilder
   @Singular
   @Delegate
   @Value
   @Accessors
   @Wither
   @With
   @SneakyThrows
   @val
   @var
   experimental @var
   @UtilityClass
   ```



@Data：无参构造，get，set，tostring，hashcode，equals

@NoArgsConstructor：无参

@AllArgsConstructor：有参





## 10、多对一处理

多对一：多个学生对应一个老师	**【关联】**

一对多：一个老师教导多个学生	**【集合】**

SQL：

```sql
CREATE TABLE `teacher` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师'); 

CREATE TABLE `student` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  `tid` INT(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');
```



### 10.1、测试环境搭建

1. 导入lombok
2. 新建实体类Teacher，Student
3. 建立Mapper接口
4. 建立Mapper.xml
5. 在核心配置文件中绑定注册我们的Mapper接口或者文件
6. 测试查询是否成功



### 10.2、按照查询嵌套查询

```xml
<!--
        思路：
        select s.id,s.name,t.name from student s , teacher t where s.tid = t.id;

        1、查新所有学生的信息
        2、根据查询出来的学生的tid，寻找对应的老师 子查询
    -->
    <select id="getStudent" resultMap="StudentTeacher">
        select * from student;
    </select>
    <resultMap id="StudentTeacher" type="com.pojo.Student">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
        <!--
            复杂的属性，需要单独处理
            对象：association
            集合：collection
        -->
        <association property="teacher" column="tid" javaType="com.pojo.Teacher" select="getTeacher"/>
    </resultMap>
    <select id="getTeacher" resultType="com.pojo.Teacher">
        select * from teacher where id = #{id}
    </select>
```



### 10.3、按照结果嵌套

```xml
    <!--    按照结果嵌套处理-->
    <select id = "getStudent2" resultMap="StudentTeacher2">
        select s.id as sid,s.name as sname,t.name as tname
        from student s , teacher t
        where s.tid = t.id;
    </select>
    <resultMap id="StudentTeacher2" type="com.pojo.Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <association property="teacher" javaType="com.pojo.Teacher">
            <result property="name" column="tname"/>
        </association>
    </resultMap>
```



**多对一查询方式：**

- 子查询
- 联表查询





## 11、一对多处理

例如：一个老师拥有多个学生

对于老师而言，就是一对多的关系

### 11.1、环境搭建

实体类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private int id;
    private String name;
    private int tid;
}
```

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private int id;
    private String name;

    //  一个老师拥有多个老师
    private List<Student> students;
}
```

2、测试

```java
    @Test
    public void getTeacher(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        List<Teacher> teacher = mapper.getTeacher();
        for (Teacher teacher1 : teacher) {
            System.out.println(teacher1);
        }
        sqlSession.close();
    }
```



### 11.2、按照结果嵌套

```sql
select s.id sid , s.name sname , t.name tname , t.id tid
from student s ,teacher t
where s.tid = t.id;
```

**接口：**

```java
    //  获取制定老师下的所有学生及老师的信息
    Teacher getTeacher(@Param("tid") int id);
```

**XMl：**

```xml
    <!--    按结果嵌套查询-->
    <select id="getTeacher" resultMap="TeacherStudent">
        select s.id sid , s.name sname , t.name tname , t.id tid
        from student s ,teacher t
        where s.tid = t.id and t.id = #{tid}
    </select>
    <resultMap id="TeacherStudent" type="com.pojo.Teacher">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
        <!--
            复杂的属性，我们需要单独处理 对象：association 集合：collection
        javaType： 执行属性的类型！
        集合中的泛型信息，我们使用ofType获取
        -->
        <collection property="students" ofType="com.pojo.Student">
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
            <result property="tid" column="tid"/>
        </collection>
    </resultMap>
```

**测试：**

```java
    @Test
    public void getTeacher(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher(1);
        System.out.println(teacher);
        sqlSession.close();
    }
```



### 11.3、按照查询嵌套查询

**接口：**

```java
    Teacher getTeacher2(@Param("tid") int id);
```

**XML：**

```xml
    <select id="getTeacher2" resultMap="TeacherStudent2">
        select * from teacher where id = #{tid}
    </select>
    <resultMap id="TeacherStudent2" type="com.pojo.Teacher">
        <collection property="students" column="id" javaType="ArrayList" ofType="com.pojo.Student" select="getStudentByTeacherId"/>
    </resultMap>
    <select id="getStudentByTeacherId" resultType="com.pojo.Student">
        select * from student where tid = #{tid}
    </select>
```

**测试：**

```java
    @Test
    public void getTeacher2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher2(1);
        System.out.println(teacher);
        sqlSession.close();
    }
```



### 11.4、小结

1. 关联 - association 【多对一】
2. 集合 - collection 【一对多】
3. javaType   &   ofType
   1. javaType - 用来制定实体类中属性的类型
   2. ofType - 用来制定映射到List或者集合中的pojo类型，泛型中的约束类型



- 保证SQL的可读性，尽量保证通俗易懂
- 注意一对多和多对一中，属性名和字段的问题
- 如果问题不好排查错误，可以使用日志，建议使用Log4j

**面试高频：**

- Mysql引擎
- InnoDB底层原理
- 索引
- 索引优化



## 12、动态SQL

**什么是动态SQL？**

动态SQL就是指根据不同的条件生成不同的SQL语句

如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach



### 12.1、搭建环境

**SQL：**

```sql
CREATE TABLE `blog`(
                       `id` VARCHAR(50) NOT NULL COMMENT '博客id',
                       `title` VARCHAR(100) NOT NULL COMMENT '博客标题',
                       `author` VARCHAR(30) NOT NULL COMMENT '博客作者',
                       `create_time` DATETIME NOT NULL COMMENT '创建时间',
                       `views` INT(30) NOT NULL COMMENT '浏览量'
)ENGINE=INNODB DEFAULT CHARSET=utf8
```



创建一个基础工程

1. 导包

2. 编写配置文件

3. 编写实体类

   ```java
   @Data
   public class Blog {
       private int id;
       private String title;
       private String author;
       private Date createTime;
       private int views;
   }
   ```

4. 编写实体类对应的Mapper接口和Mapper.xml文件



### 12.2、if

```xml
  <select id="queryBlog" resultType="com.pojo.Blog" parameterType="map">
        select * from blog1 where 1 = 1
        <if test="title != null">
            and title = #{title}
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </select>
```

```java
    @Test
    public void queryBlog(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map = new HashMap();
//        map.put("title","Java如此简单");
        map.put("author","蛋蛋");
        List<Blog> blogs = mapper.queryBlog(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }
        sqlSession.close();
    }
```



### 12.3、choose（when，otherwise）

```xml
    <select id="queryBlogChoose" resultType="com.pojo.Blog" parameterType="map">
        select * from mybatis.blog1
        <!--        自动删除and并且为SQL加上and或or-->
        <where>
            <choose>
                <when test="title != null">
                    title = #{title}
                </when>
                <when test="author != null">
                    and author = #{author}
                </when>
                <otherwise>
                    and views = #{views}
                </otherwise>
            </choose>

        </where>
    </select>
```



### 12.4、trim（where，set）

```xml
        select * from mybatis.blog
        <!--        自动删除and并且为SQL加上and或or-->
        <where>
            <if test="title != null">
                and title = #{title}
            </if>
            <if test="author != null">
                and author = #{author}
            </if>
        </where>
```

```xml
    <update id="updateBlog" parameterType="map">
        update mybatis.blog1
        <!--        多余的逗号（，）会自动删除-->
        <set>
            <if test="title != null">
                title = #{title}
            </if>
            <if test="author != null">
                author = #{author}
            </if>
        </set>
        where id = #{id}
    </update>
```



### 12.5、foreach

```sql
select * from user where 1 = 1 and (id = 1 or id = 2 or id = 3)

<foreach item="id" index="index" collection="ids"
	open="("  separator="or" close=")">
	#{id}
</foreach>
# open ：开始
# separator ：分割的符号
# close ：结束


(id=1 or id=2 or id=3)

```

```sql
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  <where>
    <foreach item="item" index="index" collection="list"
        open="ID in (" separator="," close=")" nullable="true">
          #{item}
    </foreach>
  </where>
</select>
```

**XMl代码：**

```xml
   <!--    select * from mybatis.blog1 where 1 = 1 and (id = 1 or id = 2 or id = 3)-->
    <select id="queryBolgForeach" parameterType="map" resultType="com.pojo.Blog">
        select * from mybatis.blog1
        <where>
            <!--    传递一个万能的map，map中可以存在一个集合-->
            <foreach collection="ids" item="id" open="and (" separator="or" close=")">
                id = #{id}
            </foreach>
        </where>
    </select>
```

**Test代码：**

```java
    @Test
    public void queryBlogForeach(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Map map = new HashMap();
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        map.put("ids",ids);
        List<Blog> blogs = mapper.queryBolgForeach(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }
        sqlSession.close();
    }
```

**Java接口：**

```java
    // 查询第1-2-3号记录的信息
    List<Blog> queryBolgForeach(Map map);
```







### 12.6、SQL片段

将一些公共的部分抽取出来，一起用

提高代码的复用

1. 使用SQL标签抽取公共的部分

```xml
    <sql id="if-title-author">
        <if test="title != null">
            and title = #{title}
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </sql>
```

2. 在需要使用的地方使用include标签引用即可

```xml
    <select id="queryBlogIF" resultType="com.pojo.Blog" parameterType="map">
        select * from blog1
        <where>
            <!--     导入-->
            <include refid="if-title-author"></include>
        </where>
    </select>
```

注意事项：

- 最好基于单表来定义SQL片段
- 不要存在where标签







## 13、缓存

### 13.1简介

```markdown
# 查询：连接数据库，好资源！
	一次查询的结果，给它暂存在一个可以直接取到的地方！ ————》 内存：缓存
	再次查询相同的数据的时候，直接走缓存，就不用走数据库了
```



1. 什么是缓存【Cache】？

   - 存在内存中的临时数据。

   - 将用户经常查询的数据放在缓存（内存）中，用户去查询数据不用从磁盘上（关系型数据库数据文件）查询，从缓存中查询，从而提高查询效率，解决了高并发系统的性能问题。

2. 为什么使用缓存？

   - 减少和数据库的交互次数，减少系统开销，提高系统效率。

3. 什么样的数据能使用缓存？

   - 经常查询并且不经常改变的数据。【可以使用缓存】



### 13.2、Mybatis缓存

- Mybats包含一个非常强大的查询缓存特性，它可以非常方便地指定和配置缓存。缓存可以极大的提升查询效率。
- Mybatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**
  - 默认情况下，只有一级缓存开启。（SqlSession级别的缓存，也称为本地缓存）
  - 二级缓存需要手动开启和配置，它是基于namespace级别的缓存
  - 为了提高扩展性，Mybatis定义了缓存接口Cache。我们可以通过实现Cache接口来自定义二级缓存。



### 13.3、一级缓存

- 一级缓存也叫本地缓存
  - 与数据库同一次会话期查询到的数据会放在本地缓存中。
  - 以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查数据库



**测试步骤：**

1. 开启日志

2. 测试在一个Session中查询两次相同记录

3. 查看日志

   ```markdown
   Opening JDBC Connection
   Created connection 1330400026.
   ==>  Preparing: select * from user where id = ? 
   ==> Parameters: 1(Integer)
   <==    Columns: id, name, pwd
   <==        Row: 1, 张三, 123456
   <==      Total: 1
   User(id=1, name=张三, pwd=123456)
   =======
   User(id=1, name=张三, pwd=123456)
   true
   Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@4f4c4b1a]
   ```

4. 缓存失效的情况

   1. 查两条不同的sql

   ```markdown
   Opening JDBC Connection
   Created connection 1319483139.
   ==>  Preparing: select * from user where id = ? 
   ==> Parameters: 1(Integer)
   <==    Columns: id, name, pwd
   <==        Row: 1, 张三, 123456
   <==      Total: 1
   ==>  Preparing: select * from user where id = ? 
   ==> Parameters: 2(Integer)
   <==    Columns: id, name, pwd
   <==        Row: 2, 李四, 123
   <==      Total: 1
   User(id=1, name=张三, pwd=123456)
   =======
   User(id=2, name=李四, pwd=123)
   false
   Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@4ea5b703]
   ```

   2. 两次查询中间插入一条更新数据

      ```markdown
      Opening JDBC Connection
      Created connection 1330400026.
      ==>  Preparing: select * from user where id = ? 
      ==> Parameters: 1(Integer)
      <==    Columns: id, name, pwd
      <==        Row: 1, 张三, 123456
      <==      Total: 1
      User(id=1, name=张三, pwd=123456)
      ==>  Preparing: update user set name=?,pwd=? where id = ? 
      ==> Parameters: disney(String), blue(String), 2(Integer)
      <==    Updates: 1
      =======
      ==>  Preparing: select * from user where id = ? 
      ==> Parameters: 1(Integer)
      <==    Columns: id, name, pwd
      <==        Row: 1, 张三, 123456
      <==      Total: 1
      User(id=1, name=张三, pwd=123456)
      false
      Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@4f4c4b1a]
      ```

   3. 增删改操作，可能会改变原来的数据，所以必定会刷新缓存！

5. 查询不用的Mapper.xml

6. 手动清理缓存

   ```java
   sqlSession.clearCache();	//	手动清理缓存
   ```

**小结：**一级缓存默认是开启的，只在一个SqlSession中有效，也就是拿到连接到关闭连接这个区间。

一级缓存就是一个Map。



### 13.4、二级缓存

- 二级缓存也叫全局缓存，一级缓存作用域太低了，所以诞生了二级缓存

- 基于namespace级别的缓存，一个名称空间，对应一个二级缓存；

- 工作机制

  - 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
  - 如果当前会话关闭了，这个会话对应的一级缓存就没了；但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中；
  - 新的会话查询信息，就可以从二级缓存中获取内容。

- 默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：

  ```xml
  <cache/>
  ```



**Cache中的属性：**

```xml
<cache
  eviction="FIFO"
  flushInterval="60000"
  size="512"
  readOnly="true"/>
```

这个更高级的配置创建了一个 FIFO 缓存，每隔 60 秒刷新，最多可以存储结果对象或列表的 512 个引用，而且返回的对象被认为是只读的，因此对它们进行修改可能会在不同线程中的调用者产生冲突。

- `LRU` – 最近最少使用：移除最长时间不被使用的对象。
- `FIFO` – 先进先出：按对象进入缓存的顺序来移除它们。
- `SOFT` – 软引用：基于垃圾回收器状态和软引用规则移除对象。
- `WEAK` – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。



**步骤：**

1. 开启全局缓存

   1. ```xml
          <settings>
              <!--        开启全局缓存-->
              <setting name="cacheEnabled" value="true"/>
          </settings>
      ```

   2. Mapper.xml中添加cache

      ```xml
          <cache/>
      		
      		or
      	    <!--    在当前Mapper.xml中使用二级缓存-->
          <cache
                  eviction="FIFO"
                  flushInterval="60000"
                  size="512"
                  readOnly="true"/>
      ```

   3. 测试

      两个SqlSession对象 查同一条数据

      ```mariadb
      Opening JDBC Connection
      Created connection 1696263571.
      ==>  Preparing: select * from user where id = ? 
      ==> Parameters: 1(Integer)
      <==    Columns: id, name, pwd
      <==        Row: 1, 张三, 123456
      <==      Total: 1
      User(id=1, name=张三, pwd=123456)
      User(id=1, name=张三, pwd=123456)
      Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@651aed93]
      
      ```

      1. 问题：需要将实体类序列化
      
         ```java
         Caused by: java.io.NotSerializableException : com.pojo.User;
         public class User implements Serializable 
         ```
      
      2. 小结：
      
         - 开启了二级缓存，在同一个Mapper下就有效
         - 所有的数据都会先放在一级缓存中
         - 只有当会话提交，或者关闭的时候，才会提交到二级缓存
      
      3. 缓存顺序：
      
         1. 先看二级缓存中有没有
         2. 再看一级缓存中有没有
         3. 查询数据库



### 13.5、自定义缓存 ehcache

```markdown
EhCache 是一个纯Java的进程内缓存框架，具有快速、精干等特点，是Hibernate中默认的CacheProvider。
```

在程序中使用ehcache，先导包

```xml
        <dependency>
            <groupId>org.mybatis.caches</groupId>
            <artifactId>mybatis-ehcache</artifactId>
            <version>1.1.0</version>
        </dependency>
```

在Mapper.xml中导入

```xml
	  <cache type="com.domain.something.MyCustomCache"/>
```

ehcache.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
         updateCheck="false">

    <diskStore path="./tmpdir/Tmp_EhCache"/>

    <defaultCache
            eternal="false"
            maxElementsInMemory="10000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="259200"
            memoryStoreEvictionPolicy="LRU"/>

    <cache
            name="cloud_user"
            eternal="false"
            maxElementsInMemory="5000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="1800"
            memoryStoreEvictionPolicy="LRU"/>
</ehcache>
```


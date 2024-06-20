# SpringCloud

# 1、SpringCloud是什么能干嘛？

## 1.1、什么是SpringCloud

SpringCloud不同于其他Spring的优秀项目，它不再是想SpringSecurity、SpringMVC....的一个基础框架，而是更高层次的、架构视角的综合性大型项目，它的目标是构建一套标准化的微服务解决方案。让架构师、开发者在使用微服务理念构建应用系统的时候，面对各个环节的问题都可以找到相应的组件来处理。

## 1.2、SpringCloud缺点

SpringCloud因其涵盖的内容非常广泛，因此入门的难度也就打打提高了。同时，中文文档与资料的匮乏，以及官方文档的内容对于使用描述并不够细致等问题，也直接提升了使用者的学习门槛。



# 2、Cloud

## 2.1、服务注册与发现

1. Eureka
2. Consul
3. Etcd（Go语言）
4. Nacos（Alibaba）

## 2.2、服务调用和负载均衡

1. Ribbon（x）
2. OpenFeign
3. LoadBalancer

## 2.3、分布式事务

1. Seata（Alibaba）
2. LCN
3. Hmily

## 2.4、服务熔断和降级

1. Hystrix（x）
2. Circuit Breaker
3. Sentinel（Alibaba）

## 2.5、服务链路追踪

1. Sleuth（x） + Zipkin（图形化）
2. Micrometer Tracing

## 2.6、服务网关

1. Zuul（x）
2. Gate Way

## 2.7、分布式配置管理

1. Config + Bus
2. Consul
3. Nacos（Alibaba）

# 3、IDEA新建Project和Maven工程

父工程Pom文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.cloud</groupId>
    <artifactId>Cloud</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <hutool.version>5.8.22</hutool.version>
        <lombok.version>1.18.30</lombok.version>
        <druid.version>1.1.20</druid.version>
        <mybatis.springboot.version>3.0.2</mybatis.springboot.version>
        <mysql.version>8.0.11</mysql.version>
        <swagger3.version>2.2.0</swagger3.version>
        <mapper.version>4.2.3</mapper.version>
        <fastjson2.version>2.0.40</fastjson2.version>
        <persistence-api.version>1.0.2</persistence-api.version>
        <spring.boot.test.version>3.1.5</spring.boot.test.version>
        <spring.boot.version>3.2.0</spring.boot.version>
        <spring.cloud.version>2023.0.0</spring.cloud.version>
        <spring.cloud.alibaba.version>2022.0.0.0</spring.cloud.alibaba.version>
    </properties>
    <dependencyManagement>
        <dependencies>
            <!-- springboot 3.2.0 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- spring cloud 2023.0.0 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- spring cloud alibaba 2022.0.0.0-RC2 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring.cloud.alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!--     DAO        -->
            <!-- springboot 集成 mybatis -->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.springboot.version}</version>
            </dependency>
            <!-- mysql 数据库驱动-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!-- springboot 集成 druid 连接池 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <!-- 通用mapper4之tk.mybatis -->
            <dependency>
                <groupId>tk.mybatis</groupId>
                <artifactId>mapper</artifactId>
                <version>${mapper.version}</version>
            </dependency>
            <!-- persistence-->
            <dependency>
                <groupId>javax.persistence</groupId>
                <artifactId>persistence-api</artifactId>
                <version>${persistence-api.version}</version>
            </dependency>

            <!-- fastjson2-->
            <dependency>
                <groupId>com.alibaba.fastjson2</groupId>
                <artifactId>fastjson2</artifactId>
                <version>${fastjson2.version}</version>
            </dependency>
            <!-- swagger3 地址:http://localhost:8080/swagger-ui/index.html -->
            <dependency>
                <groupId>org.springdoc</groupId>
                <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                <version>${swagger3.version}</version>
            </dependency>
            <!-- hutool -->
            <dependency>
                <groupId>cn.hutool</groupId>
                <artifactId>hutool-all</artifactId>
                <version>${hutool.version}</version>
            </dependency>
            <!-- lombok -->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
                <optional>true</optional>
            </dependency>
            <!-- spring-boot-starter-test-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <version>${spring.boot.test.version}</version>
                <scope>test</scope>
            </dependency>

        </dependencies>
    </dependencyManagement>
</project>
```

## 3.1、Mapper4一键生成Dao层代码

构建一个mave项目

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>mybatis_generator</artifactId>
    <packaging>jar</packaging>

    <name>mybatis_generator</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- 通用的mybatis在tk单独使用, 所以生成工具有自己的单独的版本号 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.13</version>
        </dependency>
        <!-- generator -->
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.4.2</version>
        </dependency>
        <!-- 通用 mapper -->
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
        </dependency>
        <!-- mysql8驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- persistence -->
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>persistence-api</artifactId>
        </dependency>
        <dependency>
            <!-- hutool -->
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <!-- lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- spring-boot-starter-test-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.iunit,vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>${basedir}/src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>${basedir}/src/main/resources</directory>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.4.2</version>
                <configuration>
                    <configurationFile>${basedir}/src/main/resources/generatorConfig.xml</configurationFile>
                    <overwrite>true</overwrite>
                    <verbose>true</verbose>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.33</version>
                    </dependency>
                    <dependency>
                        <groupId>tk.mybatis</groupId>
                        <artifactId>mapper</artifactId>
                        <version>4.2.3</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>

</project>
```

resource构建两个文件

config.properties

```properties
package.name=com.cloud

jdbc.driverClass=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
jdbc.user=root
jdbc.password=123456
```

generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

    <properties resource="config.properties"/>

    <context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">

        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="tk.mybatis.mapper.common.Mapper"/>
            <property name="caseSensitive" value="true"/>
        </plugin>

        <jdbcConnection driverClass="${jdbc.driverClass}"
                        connectionURL="${jdbc.url}"
                        userId="${jdbc.user}"
                        password="${jdbc.password}">
        </jdbcConnection>

        <javaModelGenerator targetPackage="${package.name}.entities" targetProject="src/main/java"/>
        <sqlMapGenerator targetPackage="${package.name}.mapper" targetProject="src/main/java"/>
        <javaClientGenerator targetPackage="${package.name}.mapper" targetProject="src/main/java" type="XMLMAPPER"/>

        <table tableName="t_pay" domainObjectName="Pay">
            <generatedKey column="id" sqlStatement="JDBC"/>
        </table>

    </context>
</generatorConfiguration>
```



## 3.2、项目实战之微服务工程化编写步骤

建pom

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloud-provider-payment8001</artifactId>
    <packaging>jar</packaging>

    <name>cloud-provider-payment8001</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
        </dependency>
        <!--        Swagger3 嗲用用方式http://主机:5555/swagger-ui/index.html-->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>persistence-api</artifactId>
        </dependency>
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.28</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

 建yml

```yml
server:
  port: 8001

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
    username: root
    password: 123456
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  profiles:
    active: dev

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.cloud.entities
  configuration:
    map-underscore-to-camel-case: true

```

主启动类

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//  扫描所有mapper
@MapperScan("com.cloud.mapper")
//import tk.mybatis.spring.annotation.MapperScan;
public class Main8001
{
    public static void main( String[] args )
    {
        SpringApplication.run(Main8001.class,args);
    }
}
```

entities

```java
package com.cloud.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 表名：t_pay
 * 表注释：支付交易表
*/
@Table(name = "t_pay")
public class Pay {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 支付流水号
     */
    @Column(name = "pay_no")
    private String payNo;

    /**
     * 订单流水号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 用户账号ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 删除标志, 默认0不删除,1删除
     */
    private Byte deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取支付流水号
     *
     * @return payNo - 支付流水号
     */
    public String getPayNo() {
        return payNo;
    }

    /**
     * 设置支付流水号
     *
     * @param payNo 支付流水号
     */
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    /**
     * 获取订单流水号
     *
     * @return orderNo - 订单流水号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单流水号
     *
     * @param orderNo 订单流水号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取用户账号ID
     *
     * @return userId - 用户账号ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户账号ID
     *
     * @param userId 用户账号ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取交易金额
     *
     * @return amount - 交易金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置交易金额
     *
     * @param amount 交易金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取删除标志, 默认0不删除,1删除
     *
     * @return deleted - 删除标志, 默认0不删除,1删除
     */
    public Byte getDeleted() {
        return deleted;
    }

    /**
     * 设置删除标志, 默认0不删除,1删除
     *
     * @param deleted 删除标志, 默认0不删除,1删除
     */
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return updateTime - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
```

```java
package com.cloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDTO implements Serializable {
    private Integer id;
    //  支付流水号
    private String payNo;
    //  订单流水号
    private String orderNo;
    //  交易金额
    private BigDecimal amount;
}
```

mapper

```java
package com.cloud.mapper;

import com.cloud.entities.Pay;
import tk.mybatis.mapper.common.Mapper;

public interface PayMapper extends Mapper<Pay> {
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.cloud.mapper.PayMapper">
  <resultMap id="BaseResultMap" type="com.cloud.entities.Pay">
    <!--
      WARNING - @mbg.generated
    -->
    <id column="id" jdbcType="INTEGER" property="id" />
    <result column="pay_no" jdbcType="VARCHAR" property="payNo" />
    <result column="order_no" jdbcType="VARCHAR" property="orderNo" />
    <result column="user_id" jdbcType="INTEGER" property="userId" />
    <result column="amount" jdbcType="DECIMAL" property="amount" />
    <result column="deleted" jdbcType="TINYINT" property="deleted" />
    <result column="create_time" jdbcType="TIMESTAMP" property="createTime" />
    <result column="update_time" jdbcType="TIMESTAMP" property="updateTime" />
  </resultMap>
</mapper>
```

service

```java
package com.cloud.service;

import com.cloud.entities.Pay;

import java.util.List;

public interface PayService {
    int add(Pay pay);
    int delete(Integer id);
    int update(Pay pay);
    Pay getById(Integer id);
    List<Pay> getAll();
}
```

```java
package com.cloud.service.impl;

import com.cloud.entities.Pay;
import com.cloud.mapper.PayMapper;
import com.cloud.service.PayService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayServiceImpl implements PayService {

    @Resource
    private PayMapper payMapper;

    @Override
    public int add(Pay pay) {
        return payMapper.insertSelective(pay);
    }

    @Override
    public int delete(Integer id) {
        return payMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Pay pay) {
        return payMapper.updateByPrimaryKeySelective(pay);
    }

    @Override
    public Pay getById(Integer id) {
        return payMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Pay> getAll() {
        return payMapper.selectAll();
    }
}
```

controller

```java
package com.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cloud.entities.Pay;
import com.cloud.entities.PayDTO;
import com.cloud.service.PayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("/pay/add")
    public String addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return "成功插入记录，返回值：" + i;
    }

    @DeleteMapping(value = "/pay/delete/{id}")
    public Integer deletePay(@PathVariable("id") Integer id) {
        return payService.delete(id);
    }


    @PutMapping(value = "/pay/update")
    public String updatePay(@RequestBody PayDTO patDTO) {
        Pay pay = new Pay();

        BeanUtil.copyProperties(patDTO, pay);
        int i = payService.update(pay);
        return "成功修改记录，返回值：" + i;
    }

    @GetMapping("/pay/get/{id}")
    public Pay getById(@PathVariable("id") Integer id) {
        return payService.getById(id);
    }

    @GetMapping("/pau/getAll")
    public List<Pay> getAllPay(){
        return payService.getAll();
    }
}
```

Swagger3常用注解

| 注解         | 标注位置          | 作用                   |
| ------------ | ----------------- | ---------------------- |
| @Tag         | Controller类      | 标识Controller作用     |
| @Parameter   | 参数              | 标识参数作用           |
| @Parameters  | 参数              | 标识多重说明           |
| @Schema      | model层的JavaBean | 描述模型作用及每个属性 |
| @Operation   | 方法              | 描述方法作用           |
| @ApiResponse | 方法              | 描述相应状态码等       |

Swagger3Config

```java
package com.cloud.confis;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger3Config {

    @Bean
    public GroupedOpenApi payApi() {
        return GroupedOpenApi.builder().group("支付微服务模块").pathsToMatch("/pay/**").build();
    }

    @Bean
    public GroupedOpenApi otherApi() {
        return GroupedOpenApi.builder().group("其他微服务模块").pathsToMatch("/other/**").build();
    }

    @Bean
    public OpenAPI docsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("spring-cloud")
                        .description("通用设计")
                        .version("v1.0")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("")
                        .url("")
                );
    }
}
```

链接：http://localhost:8001/swagger-ui/index.html

PayController

```java
package com.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cloud.entities.Pay;
import com.cloud.entities.PayDTO;
import com.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name="支付微服务模块",description = "支付CRUD")
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("/pay/add")
    @Operation(summary = "新增",description = "新增支付流水方法，json串做参数")
    public String addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return "成功插入记录，返回值：" + i;
    }

    @DeleteMapping(value = "/pay/delete/{id}")
    @Operation(summary = "删除",description = "删除支付方法")
    public Integer deletePay(@PathVariable("id") Integer id) {
        return payService.delete(id);
    }


    @PutMapping(value = "/pay/update")
    @Operation(summary = "修改",description = "修改支付流水方法")
    public String updatePay(@RequestBody PayDTO patDTO) {
        Pay pay = new Pay();

        BeanUtil.copyProperties(patDTO, pay);
        int i = payService.update(pay);
        return "成功修改记录，返回值：" + i;
    }

    @GetMapping("/pay/get/{id}")
    @Operation(summary = "按照ID查流水",description = "查询支付流水方法")
    public Pay getById(@PathVariable("id") Integer id) {
        return payService.getById(id);
    }

    @GetMapping("/pau/getAll")
    public List<Pay> getAllPay(){
        return payService.getAll();
    }

}
```

Pay

```java
package com.cloud.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 表名：t_pay
 * 表注释：支付交易表
*/
@Table(name = "t_pay")
@Schema(title = "支付交易表实体类")
public class Pay {
    @Id
    @GeneratedValue(generator = "JDBC")
    @Schema(title = "主键")
    private Integer id;

    /**
     * 支付流水号
     */
    @Schema(title = "支付流水号")
    @Column(name = "pay_no")
    private String payNo;

    /**
     * 订单流水号
     */
    @Column(name = "order_no")
    @Schema(title = "订单流水号")
    private String orderNo;

    /**
     * 用户账号ID
     */
    @Column(name = "user_id")
    @Schema(title = "用户账号ID")
    private Integer userId;

    /**
     * 交易金额
     */
    @Schema(title = "交易金额")
    private BigDecimal amount;

    /**
     * 删除标志, 默认0不删除,1删除
     */

    private Byte deleted;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取支付流水号
     *
     * @return payNo - 支付流水号
     */
    public String getPayNo() {
        return payNo;
    }

    /**
     * 设置支付流水号
     *
     * @param payNo 支付流水号
     */
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    /**
     * 获取订单流水号
     *
     * @return orderNo - 订单流水号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单流水号
     *
     * @param orderNo 订单流水号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取用户账号ID
     *
     * @return userId - 用户账号ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户账号ID
     *
     * @param userId 用户账号ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取交易金额
     *
     * @return amount - 交易金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置交易金额
     *
     * @param amount 交易金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取删除标志, 默认0不删除,1删除
     *
     * @return deleted - 删除标志, 默认0不删除,1删除
     */
    public Byte getDeleted() {
        return deleted;
    }

    /**
     * 设置删除标志, 默认0不删除,1删除
     *
     * @param deleted 删除标志, 默认0不删除,1删除
     */
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return updateTime - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
```

## 3.3、上述模块还有哪些问题

### 3.3.1、时间格式问题

时间日志格式的统一和定制情况

1. 可以在相应的类属性上使用@JsonFormat注解：

   ```java
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date bilitime;
   ```

2. 如果是Spring Boot项目，也可以在application.yml文件中指定：

   ```yml
   spring:
   	jackson:
   		date-format: yyyy-MM-dd HH:mm:ss
   		time-zone: GMT+8
   ```

   

### 3.3.2、Java如何设计API接口实现统一格式返回？

影响前端/小程序/app等交互体验和开发

void、数值、String、对象entity、Map

1. 定义返回值的标准格式

   1. code状态值：由后端统一定义各种返回结果的状态码
   2. message描述：本次接口调用的结果描述
   3. data数据：本次返回的数据

2. 扩展

   1. 接口调用时间类：timestamp 接口调用时间

3. 步骤

   1. 新建枚举类 ReturnCodeEnum

      - Http请求返回的状态码

        | 分类 | 区间      | 分类描述                                       |
        | ---- | --------- | ---------------------------------------------- |
        | 1**  | 100 ~ 199 | 信息，服务器收到请求，需要请求者继续执行操作   |
        | 2**  | 200 ~ 299 | 成功，操作被成功接收并处理                     |
        | 3**  | 300 ~ 399 | 重定向，需要进一步的操作以完成请求             |
        | 4**  | 400 ~ 499 | 客户端错误，请求包含语法错误或无法完成请求     |
        | 5**  | 500 ~ 599 | 服务器错误，服务器在处理请求的过程中发生了错误 |

      - ReturnCodeEnum

        ```java
        package com.cloud.resp;
        
        import lombok.Getter;
        
        import java.util.Arrays;
        
        //  如何定义一个通用的枚举类
        //  举值 - 构造 - 遍历
        @Getter
        public enum ReturnCodeEnum {
            //  操作失败
            RC999("999","操作xxx失败"),
            //  操作成功
            RC200("200","success"),
            //  服务降级
            RC201("201","服务开启降级保护，请稍后再试！"),
            //  热点参数限流
            RC202("202","热点参数限流，请稍后再试！"),
            //  系统规则不满足
            RC203("203","系统规则不满足要求，请稍后再试！"),
            //  授权规则不通过
            RC204("204","授权规则不通过，请稍后再试！"),
            //  access_denied
            RC403("403","无访问权限，请管理员授予权限"),
            //  access_denied
            RC401("401","匿名用户访问无权限资源时的异常"),
            RC404("404","404页面找不到的异常"),
            //  服务异常
            RC500("500","系统异常，请稍后重试"),
            RC375("375","数字运算异常，请稍后重试"),
        
            INVALID_TOKEN("2001","访问令牌不合法"),
            ACCESS_DENIED("2003","没有权限访问该资源"),
            CLIENT_AUTHENTICATION_FAILED("1001","客户端认证失败"),
            USERNAME_OR_PASSWORD_ERROR("1004","业务逻辑异常"),
            BUSINESS_ERROR("1004","业务逻辑异常"),
            UNSUPPORTED_GRANT_TYPE("1003","不支持的认证模式");
        
            //  自定义状态码
            private final String code;
            //  自定义描述
            private final String message;
        
            ReturnCodeEnum(String code, String message) {
                this.code = code;
                this.message = message;
            }
        
            public static ReturnCodeEnum getReturnCodeEnum(String code){
                for (ReturnCodeEnum element : ReturnCodeEnum.values()){
                    if (element.getCode().equalsIgnoreCase(code)){
                        return element;
                    }
                }
                return null;
            }
        
            //  Stream式计算
            public static ReturnCodeEnum getReturnCodeEnum2(String code){
                return Arrays
                        .stream(ReturnCodeEnum.values())
                        .filter(x -> x.getCode()
                                .equalsIgnoreCase(code))
                        .findFirst().orElse(null);
            }
        
            public static void main(String[] args) {
                ReturnCodeEnum codeEnum = getReturnCodeEnum("200");
                assert codeEnum != null;
                System.out.println(codeEnum);
                System.out.println(codeEnum.getCode());
                System.out.println(codeEnum.getMessage());
        
        
                ReturnCodeEnum codeEnum1 = getReturnCodeEnum2("404");
                assert codeEnum1 != null;
                System.out.println(codeEnum1);
                System.out.println(codeEnum1.getCode());
                System.out.println(codeEnum1.getMessage());
            }
        }
        ```

   2. 新建统一返回对象ResultData

      ```java
      package com.cloud.resp;
      
      import lombok.Data;
      import lombok.experimental.Accessors;
      
      @Data
      @Accessors(chain = true)
      public class ResultData<T>{
          private String code;
          private String message;
          private T Data;
          private long timestamp;
      
          public ResultData(){
              this.timestamp = System.currentTimeMillis();
          }
      
          public static <T> ResultData<T> success(T data){
              ResultData<T> resultData = new ResultData<>();
              resultData.setCode(ReturnCodeEnum.RC200.getCode());
              resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
              resultData.setData(data);
              return resultData;
          }
      
          public static <T> ResultData<T> fail(String code,String message){
              ResultData<T> resultData = new ResultData<>();
              resultData.setCode(code);
              resultData.setMessage(message);
              return resultData;
          }
      }
      ```

   3. 修改PayController

      ```java
      package com.cloud.controller;
      
      import cn.hutool.core.bean.BeanUtil;
      import com.cloud.entities.Pay;
      import com.cloud.entities.PayDTO;
      import com.cloud.resp.ResultData;
      import com.cloud.service.PayService;
      import io.swagger.v3.oas.annotations.Operation;
      import io.swagger.v3.oas.annotations.tags.Tag;
      import jakarta.annotation.Resource;
      import lombok.extern.slf4j.Slf4j;
      import org.springframework.web.bind.annotation.*;
      
      import java.util.List;
      
      @RestController
      @Slf4j
      @Tag(name="支付微服务模块",description = "支付CRUD")
      public class PayController {
      
          @Resource
          private PayService payService;
      
          @PostMapping("/pay/add")
          @Operation(summary = "新增",description = "新增支付流水方法，json串做参数")
          public ResultData<String> addPay(@RequestBody Pay pay) {
              System.out.println(pay.toString());
              int i = payService.add(pay);
              return ResultData.success("成功插入记录，返回值：" + i);
          }
      
          @DeleteMapping(value = "/pay/delete/{id}")
          @Operation(summary = "删除",description = "删除支付方法")
          public ResultData<Integer> deletePay(@PathVariable("id") Integer id) {
              return ResultData.success(payService.delete(id));
          }
      
      
          @PutMapping(value = "/pay/update")
          @Operation(summary = "修改",description = "修改支付流水方法")
          public ResultData<String> updatePay(@RequestBody PayDTO patDTO) {
              Pay pay = new Pay();
      
              BeanUtil.copyProperties(patDTO, pay);
              int i = payService.update(pay);
              return ResultData.success("成功修改记录，返回值：" + i);
          }
      
          @GetMapping("/pay/get/{id}")
          @Operation(summary = "按照ID查流水",description = "查询支付流水方法")
          public ResultData<Pay> getById(@PathVariable("id") Integer id) {
              return ResultData.success(payService.getById(id));
          }
      
          @GetMapping("/pau/getAll")
          public List<Pay> getAllPay(){
              return payService.getAll();
          }
      }
      ```

   4. 结论

      通过ResultData.success()对返回结果进行包装后返回给前端 --- > 优化驱动力

   5. 查询的一个Bug

      ```java
      @GetMapping("/pay/get/{id}")
      @Operation(summary = "按照ID查流水",description = "查询支付流水方法")
      public ResultData<Pay> getById(@PathVariable("id") Integer id) {
          if (id == -4) throw new RuntimeException("传进来不能是负数");
          return ResultData.success(payService.getById(id));
      	//	前端会爆500错误
      }
      ```



### 3.3.3、全局异常接入返回的标准格式

有统一返回值 + 全局统一异常

1. 为什么需要全局异常处理器

   不用手写try-catch

2. 新建全局异常类GlobalExceptionHandler

   ```
   package com.cloud.exp;
   
   import com.cloud.resp.ResultData;
   import com.cloud.resp.ReturnCodeEnum;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.http.HttpStatus;
   import org.springframework.web.bind.annotation.ExceptionHandler;
   import org.springframework.web.bind.annotation.ResponseStatus;
   import org.springframework.web.bind.annotation.RestControllerAdvice;
   
   @Slf4j
   /*
    * 自定义客户端返回格式
    * 捕获客户端返回异常
    */
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       //  500服务器内部错误
       @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
       @ExceptionHandler(RuntimeException.class)
       public ResultData<String> exception(Exception e) {
           System.out.println("### come in GlobalExceptionHandler");
           log.error("全局异常信息：{}", e.getMessage(), e);
           return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
       }
   }
   ```

3. 修改Controller

   ```java
   //  手写异常捕捉
   @GetMapping(value = "/pay/error")
   public ResultData<Integer> getPayError(){
       Integer integer = 200;
       try {
           System.out.println("come in payError test");
           int age = 10/0;
       }catch (Exception e){
           e.printStackTrace();
           return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
       }
       return ResultData.success(integer);
   }
   ```



# 4、引入微服务理念

> Q：订单微服务80如何才能调用到支付微服务8001？

## 4.1、cloud-consumer-order80

微服务调用者订单模块

### 4.1.1、建立cloude-consumer-order80

### 4.1.2、改POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloude-consumer-order80</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!--web+actuator-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!--Lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--hutool-all-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <!--fastjson2-->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
        </dependency>
        <!--swagger3-->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 4.1.3、写YML

```yml
server:
  port: 80
```

### 4.1.4、主启动

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main80 {
    public static void main(String[] args) {
        SpringApplication.run(Main80.class, args);
    }
}
```

### 4.1.5、业务类

1. entities

   ```java
   package com.cloud.entities;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.io.Serializable;
   import java.math.BigDecimal;
   
   @AllArgsConstructor
   @NoArgsConstructor
   @Data
   public class PayDTO implements Serializable {
       private Integer id;
       //  支付流水号
       private String payNo;
       //  订单流水号
       private String orderNo;
       //  交易金额
       private BigDecimal amount;
   }
   ```

2. RestTemplate

   > Q：是什么
   >
   > RestTemplate提供了多种便捷访问远程Http服务的方法，是一种简单便捷的访问restful服务模版类，是Spring提供的用于访问Rest服务的客户端模版工具集
   >
   > 官网：https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html

   常用API使用说明：

   - 使用说明：
     - 使用RestTemplate访问restful接口非常的简单粗暴无脑。
     - （URL，requesMap，ResponseBean.class）这三个参数分别代表REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
   - getForObject方法和getForEntity方法
     - getForObject：返回对象为响应体中数据转化成的对象，基本上可以理解为JSON
     - getForEntity：返回对象为ResponseEntity对象，包含了响应中的一些重要信息，比如响应头、响应状态码、响应体等
   - postForObject方法和postForEntity方法
   - GET请求方法
   - POST请求方法

   

3. config配置类

   ```java
   package com.cloud.config;
   
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.web.client.RestTemplate;
   
   @Configuration
   public class RestTemplateConfig {
       @Bean
       public RestTemplate restTemplate(){
           return new RestTemplate();
       }
   }
   ```

4. controller

   ```java
   package com.cloud.controller;
   
   import com.cloud.entities.PayDTO;
   import com.cloud.resp.ResultData;
   import jakarta.annotation.Resource;
   import org.springframework.web.bind.annotation.*;
   import org.springframework.web.client.RestTemplate;
   
   @RestController
   public class OrderController {
       public static final String PaymentSrv_URL = "http://localhost:8001";
   
       @Resource
       private RestTemplate restTemplate;
   
       //增加
       @GetMapping(value = "/consumer/pay/add")
       public ResultData<?> addOrder(PayDTO payDTO) {
           return restTemplate.postForObject(PaymentSrv_URL + "/pay/add", payDTO, ResultData.class);
       }
   
       //删除
       @DeleteMapping(value = "/consumer/pay/delete/{id}")
       public void deleteOrder(@PathVariable("id") Integer id) {
           restTemplate.delete(PaymentSrv_URL + "/pay/delete/" + id, ResultData.class, id);
       }
   
       //修改
       @PutMapping(value ="/consumer/pay/update")
       public void updateOrder(@RequestBody PayDTO payDTO){
           restTemplate.put(PaymentSrv_URL+"/pay/update", ResultData.class,payDTO);
       }
   
       //查询
       @GetMapping(value = "/consumer/pay/get/{id}")
       public ResultData<?> getPayInfo(@PathVariable("id") Integer id) {
           return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/" + id, ResultData.class, id);
       }
   }
   
   ```



## 4.2、工程重构重复代码提取

多个微服务有重复的entities、api等

cloud-api-commons

### 4.2.1、新建Module cloud-api-commons

作用：对外暴露通用的组件/api/接口/工具类等



### 4.2.2、改Pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloud-api-commons</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
    </dependencies>
</project>
```



### 4.2.3、entities

```java
package com.cloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDTO implements Serializable {
    private Integer id;
    //  支付流水号
    private String payNo;
    //  订单流水号
    private String orderNo;
    //  交易金额
    private BigDecimal amount;
}
```



### 4.2.4、全局异常类

```java
package com.cloud.exp;

import com.cloud.resp.ResultData;
import com.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
/*
 * 自定义客户端返回格式
 * 捕获客户端返回异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //  500服务器内部错误
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResultData<String> exception(Exception e) {
        System.out.println("### come in GlobalExceptionHandler");
        log.error("全局异常信息：{}", e.getMessage(), e);
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
    }
}
```



### 4.2.5、maven命令clean install

在cloud-api-commons服务上，把cloud-api-commons打成jar包



### 4.2.6、订单80和支付8001分别改造

删除各自抽出来的公共的东西

pom文件引入本地的jar包

```xml
<dependency>
    <groupId>com.cloud</groupId>
    <artifactId>cloud-api-commons</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```



# 5、硬编码写死问题

```java
public static final String PaymentSrv_URL = "http://localhost:8001";
```

微服务所在的IP地址和端口号**硬编码**到订单微服务中，会存在非常多的问题

1. 如果订单微服务和支付微服务的IP地址或者端口号发生了变化，则支付微服务将变得不可用，需要同步修改订单微服务中调用支付微服务的IP地址和端口号。
2. 如果系统重提供了多个订单微服务和支付微服务，则不发实现微服务的负载均衡功能。
3. 如果系统需要支持更高的并发，需要部署更多的订单微服务和支付微服务，硬编码订单微服务则后续维护会变得异常复杂。

所以，在微服务开发的过程中，需要引入服务治理功能，实现微服务之间的动态注册与发现。

# 6、Consul服务注册与发现

## 6.1、为什么要引入服务注册中心？

管理所有微服务、解决微服务之间管理错综复杂、难以维护的问题



## 6.2、为什么不再使用传统老牌的Eureka

1. Eureka停更进维修
2. Eureka对出血者不友好 --- 首次看到自我保护机制 
3. 注册中心独立且微服务功能解耦 --- 目前主流服务中心，希望单独隔离出来而不是作为一个独立微服务嵌入到系统中
4. 阿里巴巴Nacos的崛起



## 6.3、Consul简介

### 6.3.1、是什么？

官网：https://www.consul.io/

HashiCorp Consul is a service networking solution that enables teams to manage secure network connectivity between services and across on-prem and multi-cloud environments and runtimes. Consul offers service discovery, service mesh, traffic management, and automated updates to network infrastructure devices. You can use these features individually or together in a single Consul deployment.

HashiCorp Consul是一套开源的分布式服务发现和配置管理系统，由HashiCorp公司用Go语言开发。

提供了微服务系统重的服务治理、配置中心、控制总线等功能。这些功能中的每一个都可以根据需要单独使用，也可以一起使用以构建全方位的服务网格，总之Consul提供了一种完成的服务网格解决方案。它具有很多优点，包括：基于raft协议，比较简介；支持健康检查，同时支持HTTP和DNS协议支持跨数据中心的WAN集群；提供图形界面；跨平台；支持Linux、Mac、Windows；

Spring Cloud Consul：https://spring.io/projects/spring-cloud-consul



### 6.3.2、能干嘛？

1. 服务发现：提供HTTP和DNS两种方法
2. 健康检测：支持多种方式，HTTP、TCP、Docket、Shell脚本定制
3. KV存储：Key、Value的存储方式
4. 多数据中心：Consul支持多数据中心
5. 可视化Web界面



### 6.3.3、什么下载？

官网：https://developer.hashicorp.com/consul/install



### 6.3.4、怎么使用？

Spring Cloud Consul：https://docs.spring.io/spring-cloud-consul/reference/quickstart.html

Consul：https://developer.hashicorp.com/consul#get-started



## 6.4、安装并运行consul

运行 consul.exe文件

安装目录下 consul -version

使用开发模式启动

> consul agent -dev
>
> 通过一下地址可以访问Consul的首页：http://localhost:8500





## 6.5、服务注册与发现

### 6.5.1、服务提供者8001

支付服务provider8001注册进consul

配置来源：https://docs.spring.io/spring-cloud-consul/reference/quickstart.html

pom

```xml
<!--Spring Cloud Consul discovery-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

yml

```yml
spring:
    cloud:
      consul:
        host: localhost
        port: 8500
        discovery:
          service-name: ${spring.application.name}
          heartbeat:
            enabled: true
        prefer-ip-address: true
```

主启动类

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//  扫描所有mapper
@MapperScan("com.cloud.mapper")
//import tk.mybatis.spring.annotation.MapperScan;
@EnableDiscoveryClient
public class Main8001
{
    public static void main( String[] args )
    {
        SpringApplication.run(Main8001.class,args);
    }
}
```



### 6.5.2、服务消费者80

引入consul

pom

```xml
<!--Spring Cloud Consul discovery-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

yml

```yml
spring:
    cloud:
      consul:
        host: localhost
        port: 8500
        discovery:
          service-name: ${spring.application.name}
          heartbeat:
            enabled: true
        prefer-ip-address: true
```

主启动类

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Main80 {
    public static void main(String[] args) {
        SpringApplication.run(Main80.class, args);
    }
}
```

解决硬编码的问题

```java
//    public static final String PaymentSrv_URL = "http://localhost:8001";
//    注册中心叫什么，这里就叫什么
    public static final String PaymentSrv_URL = "http://cloud-payment-service8001";
```

会出现一个问题 - 找不到cloud-payment-service8001

要设置负载均衡

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```



### 6.5.3、三个注册中心异同点

CAP：

1. C：Consistency（强一致性）
2. A：Availability（可用性）
3. P：Partition tolerance（分区容错性）

最多只能同时较好的满足两个

CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求，因此，根据CAP原理将NoSQL数据库分成了满足CA原则、满足CP原则和满足AP原则三大类：

CA - 单点集群，满足一致性，可用性的系统，通常在扩展性上不太强大

CP - 满足一致性，分区容忍性的系统，通常性能不是特别高

AP - 满足可用性，分区容忍性的系统，通常可能对一致性要求低一些

| 组件名    | 语言 | CAP  | 服务健康检查 | 对外暴露接口 | SpringCloud集成 |
| --------- | ---- | ---- | ------------ | ------------ | --------------- |
| Eureka    | Java | AP   | 可配支持     | HTTP         | 已集成          |
| Consul    | Go   | CP   | 支持         | HTTP/DNS     | 已集成          |
| Zookeeper | Java | CP   | 支持         | 客户端       | 已集成          |



### 6.5.4、AP架构

当网络分区出现后，为了保证可用性，系统B可以返回旧值，保证系统的可用性。

当数据出现不一致时，虽然A，B上的注册信息不完全相同，但每个Eureka节点依然能够正常对外提供服务，这会出现查询服务信息时如果请求A查不到，但是请求B就能查到。如此保证了可用性但牺牲了一致性结论：违背了一致性C的要求，只满足可用性和分区容错，即AP



### 6.5.5、CP架构

当网络分区出现后，为了保证一致性，就必须拒绝请求，否则无法保证一致性，Consul遵循CAP原理中的CP原则，保证了强一致性和分区容错性，且使用的是Raft算法，比zookeeper使用的Paxos算法更加简单。虽然保证了强一致性，但是可用性就相应下降了，例如服务注册的时间会稍长一些，因为Consul的Raft协议要求必须过半数的结点都写入成功才认为注册成功；在leader挂掉了之后，重新选举出leader之前会导致Consul服务不可用。结论：违背了可用性A的要求，只满足一致性和分区容错，即CP



## 6.6、服务配置与刷新

### 6.6.1、分布式系统面临的 -> 配置问题

微服务意味着要将单体应用中的业务拆分成一个个自服务，每个服务的粒度相对较小，因此系统中会出现大量的服务。由于每个服务都需要必要的配置信息才能运行，所以一套集中式的、动态的配置管理设施是必不可少的。比如某些配置文件中的内容大部分都是相同的，只有个别的配置项不同。就拿数据库配置来说吧，如果每个微服务使用的技术栈都是相同的，则每个微服务中关于数据库的配置几乎都是相同的，有时候主机迁移了，也希望是一次修改，处处生效。



### 6.6.2、官网说明

官网：https://docs.spring.io/spring-cloud-consul/reference/config.html



### 6.6.3、服务配置案例

1. 需求：

   通用全局配置信息，直接注册进Consul服务器，从Consul获取

   既然从Consul获取自然要遵守Consul的配置规则要求

2. 修改cloud-provider-payment8001

   pom

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
   	<artifactId>spring-cloud-starter-consul-config</artifactId>
   </dependency>
   <dependency>
   	<groupId>org.springframework.cloud</groupId>
   	<artifactId>spring-cloud-starter-bootstrap</artifactId>
   </dependency>
   ```

3. bootstrap.yml 是什么？

   application.yml是用户级的资源配置项

   bootstrap.yml是系统级，优先级更高

   Spring Cloud会创建一个“Bootstrap Context”，作为Spring应用“Application Context”的父上下文。初始化的时候，“Bootstrap”负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的“Environment”。

   “Bootstrap”属性有优先级，默认情况下，它们不会被本地配置覆盖。“Bootstrap Context”和“Application Context”有着不用的约定，所以新增了一个“Bootstrap.yml”，保证“Bootstrap Context”和“Application Context”配置的分离。

   **application.yml文件为bootstrap.yml，这是很关键的或者两者共存**

   因为bootstrap.yml是比application.yml先加载的。bootstrap.yml优先级高于application.yml

4. bootstrap.yml

   ```yml
   spring:
     application:
       name: cloud-payment-service8001
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           service-name: ${spring.application.name}
         config:
           profile-separator: '-' #分隔符，默认是， 现在改成-
           format: YAML
   ```

5. application.yml

   ```yml
   server:
     port: 8001
   
   spring:
     datasource:
       type: com.alibaba.druid.pool.DruidDataSource
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
       username: root
       password: 123456
     jackson:
       date-format: yyyy-MM-dd HH:mm:ss
       time-zone: GMT+8
     profiles:
       active: dev
   
   mybatis:
     mapper-locations: classpath:mapper/*.xml
     type-aliases-package: com.cloud.entities
     configuration:
       map-underscore-to-camel-case: true
   ```

6. consul服务器key / value 配置填写

   ![image-20240606230359870](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240606230359870.png)

   设置data

   ![image-20240606231906403](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240606231906403.png)

   ```json
   disney: 
    info: welecom to disney default config,version=1
   ```

7. controller

   ```java
   @Value("${server.port}")
   private String port;
   
   @GetMapping(value = "/pay/get/info")
   public String getInfoByConsul(@Value("${disney.info}") String info) {
       return "Info：" + info + "\t" + port;
   }
   ```

8. 拿到的数据

   > Info：welecom to disney dev config,version=1	8001



### 6.6.4、动态刷新案例

在Consul的dev配置分支修改了内容，马上访问，结果无效

实现：

```java
@RefreshScope   //  动态刷新
```

bootstrap.yml修改下，一般不改这个

```yml
spring:
  application:
    name: cloud-payment-service8001
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
      config:
        profile-separator: '-' #分隔符，默认是， 现在改成-
        format: YAML
        watch:
          wait-time: 1
```





### 6.6.5、思考

引出问题：

Consul配置持久化问题？



# 7、LoadBalancer负载均衡服务调用

## 7.1、Ribbon目前也进入维护模式

### 7.1.1、是什么？

Spring Cloud Ribbon 是基于Netflix Ribbon实现的一套客户端负载均衡的工具。

简单的说，Ribbo是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法和服务调用。Ribbon客户端组件提供一些列完善的配置项，如连接超时、重试等。简单来说，就是在配置文件汇总列出Load Balancer（简称LB）后面所有的机器，Ribbon会自动的帮助你基于某规则（如简单轮询，随机连接等）去连接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法。



## 7.2、Spring - Cloud - LoadBalancer概述

官网：https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html

### 7.2.1、是什么

LB负载均衡（Load Balancer），简单来说就是将用户的请求平摊的分配到多个服务上，从而达到系统的HA（高可用），常见的负载均衡有软件Nginx，LVS，硬件F5等



### 7.2.2、Spring - Cloud - LoadBalancer组件是什么

Spring Cloud LoadBalancer是由Spring Cloud官方提供的一个开源的、简单易用的客户端负载均衡，它包含在Spring Cloud-commons中用它来替换了以前的Ribbon组件。相比较于Ribbon，Spring Cloud LoadBalancer不仅能够支持RestTemplate。还支持，WebClient（WebClient是Spring Web Flux中提供的功能，可以实现响应式异步请求）



### 7.2.3、面试题

> Q：客户端负载 VS 服务器负载区别
>
> LoadBalancer本地负载均衡客户端 VS Nginx服务端负载均衡区别

Nginx是服务器负载均衡，客户端所有请求都会交给Nginx，然后由Nginx实现转发请求，即负载均衡是由服务端实现的。

LoadBalancer本地负载均衡，在调试微服务接口时候，会注册中心上获取注册信息服务列表之后缓存到JVM本地，从而在本地实现RPC远程服务调用技术。



## 7.3、Spring - Cloud - LoadBalancer负载均衡解析

官网：https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html

> 架构说明：80通过轮询负载访问8001/8002/8003

LoadBalancer在工作时分成两步：

1. 先选择ConsulServer从服务端查询并拉取服务列表，知道了它有多个服务，这些服务实现是完全一样的，默认轮询调用谁都可以正常执行。类似生活中的求医挂号，某个科室今日出诊的全部医生，让客户端自己选一个。
2. 按照指定的负载均衡策略从server去到的服务注册列表中由客户端自己选一个地址，所以LoadBalancer是一个客户端的负载均衡器。



### 7.3.1、服务调用负载均衡实战 - 上

构建8002、8003服务



80服务中，RestTemplate

```java
package com.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```



启动8001、8002、8003

![image-20240610205941890](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240610205941890.png)

访问：http://localhost:8001/pay/get/info

问题：{"code":"500","message":"Could not resolve placeholder 'disney.info' in value \"${disney.info}\"","timestamp":1718024617607,"data":null}

Consul配置没有被持久化



### 7.3.2、Consul配置持久化

1. E:\Cloud\consul目录下新建

   1. 空文件夹myconsul
   2. 新建文件consul_start.bat后缀为.bat

2. consul_start.bat内容信息

   ```shell
   @echo.服务启动....
   @echo off
   @sc create Consul binpath= "E:\Cloud\consul\consul.exe agent -server -ui -bind=127.0.0.1 -client=0.0.0.0 -bootstrap-expect 1 -data-dir E:\Cloud\consul\myconsul "
   @net start Consul
   @sc config Consul start= AUTO
   @echo.Consul start is OK......success
   @pause
   ```

3. 右键管理员权限打开

4. 启动结果

   ![image-20240610212115383](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240610212115383.png)

5. win后台

   ![image-20240610212306148](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240610212306148.png)

6. 后续consul的配置数据会保存近mydata文件夹，重启就有了



### 7.3.3、服务调用负载均衡实战 - 下

将80模块修改pom并注册进consul，新增LoadBalancer组件

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

```java
@GetMapping(value = "/consumer/pay/get/info")
private String getInfoByConsul() {
    return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
}
```

访问：http://localhost/consumer/pay/get/info

> Info：welcome to disney prod config,version=1 8001
>
> Info：welcome to disney prod config,version=1 8002
>
> Info：welcome to disney prod config,version=1 8003



### 7.3.4、负载均衡案例小总结

1. 编码使用DiscoveryClient动态获取所有上线的服务列表

   官网：https://docs.spring.io/spring-cloud-consul/reference/discovery.html

2. 代码解释，修改了80服务的Controller

   ```java
       @Resource
       private DiscoveryClient discoveryClient;
   
       @GetMapping("/consumer/discovery")
       public String discovery() {
           List<String> services = discoveryClient.getServices();
           for (String element : services) {
               System.out.println(element);
           }
           System.out.println("================");
           List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
           for (ServiceInstance element : instances) {
               System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
           }
           return instances.get(0).getServiceId() + ":" + instances.get(0).getPort();
       }
   ```

   访问：http://localhost/consumer/discovery

   ![image-20240610221457222](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240610221457222.png)

   

3. 结合前面实操，负载选择原理小总结

   负载均衡算法：rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标，每次服务启动后rest接口技术从1开始。

   ```java
   List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
   ```

   如：

   ```
   List[0] instances = 127.0.0.1:8001
   List[1] instances = 127.0.0.1:8002
   List[2] instances = 127.0.0.1:8003
   ```

   8001 + 8002 + 8003组合成为集群，它们共计3台机器，集群总数为3，按照轮询算法原理：

   当请求数为1时：1 % 3 = 1 对应下标位置为1，则获得服务器地址为127.0.0.1:8002

   当请求数为2时：2 % 3 = 2 对应下标位置为2，则获得服务器地址为127.0.0.1:8003

   当请求数为3时：3 % 3 = 0 对应下标位置为0，则获得服务器地址为127.0.0.1:8001



## 7.4、负载均衡算法原理

### 7.4.1、默认算法是什么？有几种？

官网：[Switching between the load-balancing algorithms](https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html)

默认两种：

1. 轮询
2. 随机



### 7.4.2、算法切换

```java
package com.cloud.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@LoadBalancerClient(
        //  下面的value值大小一定要和consul里面的名字一样
        value = "cloud-payment-service",configuration = RestTemplateConfig.class)
public class RestTemplateConfig {

    @Bean
    @LoadBalanced   //  使用@LoadBalanced注解赋予RestTemplate负载均衡的能力
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),name);
    }
}
```

# 8、OpenFeign服务接口调用

## 8.1、是什么？

官网：https://spring.io/projects/spring-cloud-openfeign

Github：https://github.com/spring-cloud/spring-cloud-openfeign

Feign是一个声明性web服务客户端。它使编写web服务客户端变得更容易。使用Feign创建一个接口并对其进行注释。它具有可插入的注释支持，包括Feign注释和JAX-RS注释。Feign还支持可插拔编码器和解码器。Spring Cloud添加了对Spring MVC注释的支持，以及对使用Spring Web中默认使用的HttpMessageConverter的支持。Spring Cloud集成了Eureka、Spring Cloud CircuitBreaker以及Spring Cloud LoadBalancer，以便在使用Feign时提供负载平衡的http客户端。

OpenFeign是一个声明式的Web服务客户端，只需创建一个Rest接口并在该接口上添加注解@FeignClient即可。

OpenFeign基本上就是当前微服务之间调用的事实标准。



## 8.2、能干嘛？

1. 可插拔的注解支持，包括Feign注解和JAX-RS注解
2. 支持可插拔的HTTP编码器和解码器
3. 支持Sentinel和它的Fallback
4. 支持Spring Cloud LoadBalancer的负载均衡
5. 支持HTTP请求和响应的压缩

前面在使用Spring Cloud LoadBalancer + RestTemplate时，利用RestTemplate对http请求的封装处理形成了一套模版化的调用方法。但是在实际开发中：

由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个服务自行封装，一些客户端类来包装这些依赖服务的调用。所以，OpenFeign在此基础上做了进一步封装，由它来帮助我们定义和实现依赖服务接口的定义。

在OpenFeign的实现下，我们只需要创建一个接口并使用注解的方式来配置它（在一个微服务接口上面标注一个@FeignClient注解即可），即可完成对服务提供方的接口绑定，同一对外暴露可以被调用的接口方法，大大简化和降低了调用客户端的开发量，也即由服务提供者给出调用接口清淡，消费者直接通过OpenFeign调用即可。

OpenFeign 同时还集成了Spring Cloud LoadBalancer，可以在使用OpenFeign时提供Http客户端的负载均衡，也可以集成阿里巴巴Sentinel来提供熔断、降级等功能。而与Sprin Cloud LoadBalancer不同的是，通过OpenFeign只需要定义服务绑定接口且以声明式的方法，优雅而简单的实现了服务调用。



## 8.3、OpenFeign通用步骤

新建Module：cloud-consumer-feign-order80

pom：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloud-consumer-feign-order80</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!--web+actuator-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!--Lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--hutool-all-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <!--fastjson2-->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
        </dependency>
        <!--swagger3-->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>

        <dependency>
            <groupId>com.cloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <!--Spring Cloud Consul discovery-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

yml：

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-openfeign-order
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true #优先使用服务ip进行注册
        service-name: ${spring.application.name}
```

启动类：

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //   该注解用于向使用consul为注册中心时注册服务
@EnableFeignClients //  启用feign客户端，定义服务+绑定接口，以声明式的方法优雅而简单的实现服务调用
public class MainOpenFeign80 {
    public static void main(String[] args) {
        SpringApplication.run(MainOpenFeign80.class,args);
    }
}
```

业务类：

修改cloud-api-commons通用模块

1. 引入openfeign依赖

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. 新建服务接口PayFeignApi，配置@FeignClient注解

   ```java
   package com.cloud.apis;
   
   import com.cloud.entities.PayDTO;
   import com.cloud.resp.ResultData;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestBody;
   
   @FeignClient(value = "cloud-payment-service")
   public interface PayFeignApi {
       @PostMapping(value = "/pay/add")
       public ResultData<?> addPay(@RequestBody PayDTO payDTO);
   
       @GetMapping(value = "/pay/get/{id}")
       public ResultData<?> getPayInfo(@PathVariable("id") Integer id);
   
       @GetMapping(value = "/pay/get/info")
       public String myLB();
   }
   ```

3. 参考微服务8001的Controller层，新建OrderController

   ```java
   package com.cloud.controller;
   
   import com.cloud.apis.PayFeignApi;
   import com.cloud.entities.PayDTO;
   import com.cloud.resp.ResultData;
   import jakarta.annotation.Resource;
   import org.springframework.web.bind.annotation.*;
   
   @RestController
   public class OrderController {
       @Resource
       private PayFeignApi payFeignApi;
   
       @PostMapping(value = "/feign/pau/add")
       public ResultData<?> addOrder(@RequestBody PayDTO payDTO) {
           System.out.println("第一步：模拟本地addOrder新增订单成功，第二部：在开启addPay支付微服务远程调用");
           return payFeignApi.addPay(payDTO);
       }
   
       @GetMapping(value = "/feign/pay/get/{id}")
       public ResultData<?> getPay(@PathVariable("id") Integer id) {
           System.out.println("----支付服务远程调用，按照ID查询订单支付流水信息");
           return payFeignApi.getPayInfo(id);
       }
   
       @GetMapping(value = "/feign/pay/myLB")
       public String myLB(){
           return payFeignApi.myLB();
       }
   }
   ```

   小总结：

   apis中的PayFeignApi要对应着相应consul的@FeignClient(value = "cloud-payment-service")服务

   消费者中的

   ```java
   @PostMapping(value = "/pay/add")
   public ResultData<?> addPay(@RequestBody PayDTO payDTO);
   ```

   要对应服务提供者的路径

   ```java
   @PostMapping("/pay/add")
   @Operation(summary = "新增", description = "新增支付流水方法，json串做参数")
   public ResultData<String> addPay(@RequestBody Pay pay) {
       System.out.println(pay.toString());
       int i = payService.add(pay);
       return ResultData.success("成功插入记录，返回值：" + i);
   }
   ```



## 8.4、OpenFeign高级特性

### 8.4.1、OpenFeign超时控制

 服务提供方cloud-provider-payment8001故意写暂停62秒程序

```java
@GetMapping("/pay/get/{id}")
@Operation(summary = "按照ID查流水", description = "查询支付流水方法")
public ResultData<Pay> getById(@PathVariable("id") Integer id) {
    if (id == -4) throw new RuntimeException("传进来不能是负数");

    //   服务提供业务处理，为了测试feign的超时时间控制
    try {
        TimeUnit.SECONDS.sleep(61);
    }catch (InterruptedException e){
        e.printStackTrace();
    }

    return ResultData.success(payService.getById(id));
}
```

服务调用方cloud-consumer-feign-order80 写好捕捉超时异常

```java
@GetMapping(value = "/feign/pay/get/{id}")
public ResultData<?> getPay(@PathVariable("id") Integer id) {
    System.out.println("----支付服务远程调用，按照ID查询订单支付流水信息");
    ResultData<?> data = null;
    try {
        System.out.println("----调用开始："+ DateUtil.now());
        data = payFeignApi.getPayInfo(id);
    }catch (Exception e){
        e.printStackTrace();
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
    }
    return data;
}
```

访问：http://localhost/feign/pay/get/1

![image-20240611174604049](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240611174604049.png)

![image-20240611174805249](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240611174805249.png)

所以说OpenFeign默认等待时间就是60秒，但是服务端处理超过规定时间会导致Feign客户端返回报错。

为了避免这样的情况，有时候我们需要设置Feign客户端的超时控制，默认60秒太长或者业务时间太短都不好



yml配置：

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-openfeign-order
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true #优先使用服务ip进行注册
        service-name: ${spring.application.name}
    openfeign:
      client:
        config:
#          指定某个服务
#          cloud-payment-service:
          default:
            # OpenFeign连接超时时间
            connect-timeout: 3000
            # 请求处理超时时间
            read-timeout: 3000
```



### 8.4.2、OpenFeign重试机制

默认重试是关闭的，给了默认值

开启重试机制：（Retryer）

```java
package com.cloud.config;

import feign.Retryer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Retryer myRetryer() {
        //  Feign默认配置是没有重试策略的
        //  return Retryer.NEVER_RETRY;
        //  最大请求数为4（1+3），初始间隔为100ms，重试时间最大间隔时间为1s
        return new Retryer.Default(100, 1, 4);
    }
}
```



### 8.4.3、OpenFeign默认HttpClient修改

> 是什么？
>
> OpenFeign中Http Client ，如果不做特殊配置，OpenFeign默认使用JDK自带的HttpURLConnection发送HTTP请求，由于默认HttpURLConnection没有连接池、性能和效率比较低，如果采用默认，性能不是最好的，所以加到最大。
>
> ![image-20240611193234659](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240611193234659.png)

使用Apache Http Client替代OpenFeign默认的HttpURLConnection

pom

```xml
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
    <version>5.3</version>
</dependency>
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-hc5</artifactId>
    <version>13.1</version>
</dependency>
```

关闭重试

yml配置

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-openfeign-order
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true #优先使用服务ip进行注册
        service-name: ${spring.application.name}
    openfeign:
      httpclient:
        hc5:
          # Apache HttpClient5 配置开启
          enabled: true
      client:
        config:
#          指定某个服务
#          cloud-payment-service:
          default:
#             OpenFeign连接超时时间
            connect-timeout: 3000
            # 请求处理超时时间
            read-timeout: 3000
```

替换之后

![image-20240611201556061](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240611201556061.png)



### 8.4.4、OpenFeign请求、响应压缩

> 是什么？
>
> **对请求和响应进行GZIP压缩**
>
> Spring Cloud OpenFeign支持对请求和响应进行GZIP压缩，以减少通信过程中的性能损耗。
>
> 通过下面的两个参数设置，就能开启请求与响应的压缩功能：
>
> ```properties
> spring.cloud.openfeign.compression.request.enabled=true
> spring.cloud.openfeign.compression.response.enabled=true
> ```
>
> **细粒度化设置**
>
> 对请求压缩做一些更细致的设置，比如下面的配置内容指定压缩的请求数据类型并设置了请求压缩的大小下限，只对超过这个大小的请求才会进行压缩：
>
> ```properties
> spring.cloud.openfeign.compression.request.enabled=true
> # 触发压缩的类型
> spring.cloud.openfeign.compression.request.mime-types=text/xml,application/xml,application/json
> # 最小触发压缩的大小
> spring.cloud.openfeign.compression.request.min-request-size=2048
> ```

开启压缩功能（yml）

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-openfeign-order
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true #优先使用服务ip进行注册
        service-name: ${spring.application.name}
    openfeign:
      httpclient:
        hc5:
          # Apache HttpClient5 配置开启
          enabled: true
      compression:
        request:
          enabled: true
          min-request-size: 2048 # 最小触发压缩的大小
          mime-types: text/xml,application/xml,applications/json # 触发压缩数据类型
        response:
          enabled: true
      client:
        config:
#          指定某个服务
#          cloud-payment-service:
          default:
#             OpenFeign连接超时时间
            connect-timeout: 3000
            # 请求处理超时时间
            read-timeout: 3000
```



### 8.4.5、OpenFeign日志打印功能

> 是什么？
>
> Feign提供了日志打印功能，我们可以通过配置来调整日志级别，congress了解Feign中Http请求的细节，说白了就是对Feign接口的调用情况进行监控和输出。

> 日志级别
>
> NONE：默认的，不显示任何日志；
>
> BASIC：仅记录请求方法、URL、响应状态码及执行时间；
>
> HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息；
>
> FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据；

> 配置日志Bean
>
> ```java
> package com.cloud.config;
> 
> import feign.Logger;
> import feign.Retryer;
> import org.springframework.context.annotation.Bean;
> import org.springframework.context.annotation.Configuration;
> 
> @Configuration
> public class FeignConfig {
> 
>     @Bean
>     public Retryer myRetryer() {
>         //  Feign默认配置是没有重试策略的
>         //  return Retryer.NEVER_RETRY;
> 
>         //  最大请求数为4（1+3），初始间隔为100ms，重试时间最大间隔时间为1s
>         return new Retryer.Default(100, 1, 4);
>     }
> 
>     @Bean
>     Logger.Level feignLoggerLevel() {
>         return Logger.Level.FULL;
>     }
> }
> ```

> YML文件里需要开启日志的Feign客户端
>
> ```YML
> # 以什么级别要监控那个api
> logging:
>   level:
>     com:
>       cloud:
>         apis:
>           PayFeignApi: debug
> ```

访问：http://localhost/feign/pay/get/1

压缩

![image-20240611232258663](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240611232258663.png)

不压缩

![image-20240611232444393](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240611232444393.png)





# 9、CircuitBreaker断路器

## 9.1、Hystrix目前也进入维护模式

### 9.1.1、是什么？

> Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，不会导致整个服务失败，避免级联故障，以提高分布式系统的单行。



## 9.2、概述

> 分布式系统面临的问题
>
> 复杂分布式体系结构中的应用程序有数十个依赖关系，每个依赖关系在某些时候将不可避免的失败。

> 服务雪崩
>
> 多个微服务之间调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其他的微服务，这就是所谓的“**扇出**”。如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，所谓的“雪崩效应”。
>
> 对于高流量的应用来说，单一的后端依赖可能会导致所有服务器上的所有资源都在几秒钟内饱和。比失败更糟糕的是，这些应用程序还可能导致服务之间的延迟增加，备份队列，线程和其他系统资源紧张，导致整个系统发生更多的级联故障。这些都表示需要对故障和延迟进行隔离和管理，以便单个依赖关系的失败，不能取消整个应用程序或系统。
>
> 所以，通常当发现一个模块下的某个实例失败后，这时候这个模块依然还会接收流量，然后这个有问题的模块还调用了其他的模块，这样就会发生级联故障，或者叫雪崩。



如何解决上述问题，避免整个系统大面积故障

1. 服务熔断
2. 服务降级
3. 服务限流
4. 服务限时
5. 服务预热
6. 接近实时的监控
7. 兜底的处理动作



## 9.3、Circuit Breaker是什么？

官网：https://spring.io/projects/spring-cloud-circuitbreaker

> 实现原理：
>
> Circuit Breaker的目的是保护分布式系统免受故障和异常，提高系统的可用性和健壮性。
>
> 当一个组件或服务出现故障时，Circuit Breaker会迅速切换到OPEN状态（保险丝跳闸断电），阻止请求发送到该组件或服务从而避免更多的请求发送到该组件或服务。这可以减少对该组件或服务的负载，防止该组件或服务进一步崩溃，并使整个系统能够继续正常运行。同时，Circuit Breaker还可以提高系统的可用性和健壮性，因为它可以在分布式系统的各个组件之间自动切换，从而避免单点故障的问题。

Circuit Breaker只是一套规范和接口，落地实现者是Resilience4J



## 9.4、Resilience4J

### 9.4.1、是什么

Github：https://github.com/resilience4j/resilience4j

Resilience是一个专为函数式编程设计的轻量级容错库。Resilience4J提供那个高阶函数（装饰器），以通过断路器、速率限制器、重试或隔板增强任何功能接口、lambda表达式或方法引用。可以在任何函数式接口、lambda表达式或方法引用上堆叠多个装饰器。优点是可以选择需要的装饰器，而没有其他选择。

Resilience4J 2 需要Java 17



### 9.4.2、能干嘛

Resilience4J提供了几个核心模块：

- resilience4j - CircuitBreaker：断路
- resilience4j - ratelimiter：速率限制
- resilience4j - bulkhead：舱壁
- resilience4j - retry：自动重试（同步和异步）
- resilience4j - timelimiter：超时处理
- resilience4j - cache：结果缓存

还有用于指标、Feign、Kotlin、Spring、Ratpack、Vertx、RxJava2等的附加模块。





### 9.4.3、怎么使用

官网：https://resilience4j.readme.io/docs/circuitbreaker

中文手册：https://github.com/lmhmhl/Resilience4j-Guides-Chinese/blob/main/index.md





## 9.5、案例实战

### 9.5.1、熔断（CircuitBreaker）（服务熔断 + 服务降级）

- 断路器有三个普通状态：关闭（CLOSED）、开启（OPEN）、半开（HALF_OPEN），还有两个特殊状态：禁用（DISABLED）、强制开启（FORCED_OPEN）。
- 当熔断器关闭时，所有的请求都会通过熔断器。
  - 如果失败率超过设定的阈值，熔断器就会从关闭状态转换到打开状态，这时所有的请求都会被拒绝。
  - 当经过一段时间后，熔断器会从打开状态转换到半开状态，这时仅有一定数量的请求会被放入，并重新计算失败率。
  - 如果失败率超过阈值，则变为打开状态，如果失败率低于阈值，则变为关闭状态。
- 断路器使用华东庄口来存储和统计调用的结果。可以选择基于调用数量的滑动窗口或者基于时间的滑动窗口。
  - 基于访问数量的滑动窗口统计了最近N次调用的返回结果。基于时间的滑动窗口统计了最近N秒的调用返回结果。
- 除此以外，熔断器还会有两种特殊状态：DISABLED（始终允许访问）和FORCED_OPEN（始终拒绝访问）。
  - 这两个状态不会生成熔断器事件（除状态转换外），并不会记录事件的成功或失败。
  - 退出这两个状态的唯一方法是出发状态转换或者重置熔断器。



8001服务新建PayCircuitController

```java
package com.cloud.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class PayCircuitController {

    @GetMapping(value = "/pay/circuit/{id}")
    public String myCircuit(@PathVariable("id") Integer id){
        if (id == -4) throw new RuntimeException("----circuit id 不能负数");
        if (id == 9999){
            try {
                TimeUnit.SECONDS.sleep(5);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return "Hello Circuit! inputID：" + id + "\t" + IdUtil.simpleUUID();
    }
}
```



api-commons - PayFeignApi

```java
@GetMapping(value = "/pay/circuit/{id}")
public String myCircuit(@PathVariable("id") Integer id);
```



修改cloud-consumer-feign-order80

pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
<!--由于断路保护等需要AOP实现，所以必须导入AOP包-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

yml

基于COUNT_BASED（计数的滑动窗口）

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-openfeign-order
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true #优先使用服务ip进行注册
        service-name: ${spring.application.name}
    openfeign:
      httpclient:
        hc5:
          # Apache HttpClient5 配置开启
          enabled: true
      compression:
        request:
          enabled: true
          min-request-size: 2048 # 最小触发压缩的大小
          mime-types: text/xml,application/xml,applications/json # 触发压缩数据类型
        response:
          enabled: true

      circuitbreaker:
        enabled: true
        group:
          enabled: true

      client:
        config:
          #          指定某个服务
          #          cloud-payment-service: 
          default:
            #             OpenFeign连接超时时间
            connect-timeout: 20000
            # 请求处理超时时间
            read-timeout: 20000
#logging:
#  level:
#    com:
#      cloud:
#        apis:
#          PayFeignApi: debug
resilience4j:
  circuitbreaker:
    configs:
      default:
        failure-rate-threshold: 50 # 设置50%的调用失败时打开断路器，超过请求百分比Circuit Breaker变为OPEN状态
        sliding-window-type: COUNT_BASED # 滑动窗口的类型
        sliding-window-size: 6 # 滑动窗口的大小配置COUNT_BASED表示6个请求，配置TIME_BASED表示6s
        minimum-number-of-calls: 6 # 断路器计算失败率或慢调用率之前所需的最小样本（每个滑动窗口周期）
        automatic-transition-from-open-to-half-open-enabled: true # 是否启用自动从开始状态过渡到半开状态，默认值为true。如果启用，Circuit Breaker将自动从开启状态过渡到掰开状态，并允许一些请求通过以测试服务是否恢复正常
        wait-duration-in-open-state: 5s # 从OPEN到HALF_OPEN状态需要等待的时间
        permitted-number-of-calls-in-half-open-state: 2 # 半开状态允许的最大请求数，默认值为10。在半开状态下，CircuitBreaker将允许最多permittedNumberOfCallsInHalfOpenState个请求通过，如果其中有任何一个请求失败，Circuit Breaker将重新进入开启状态。
        record-exceptions:
          - java.lang.Exception
    instances:
      cloud-payment-service:
        base-config: default
```

OrderCircuitController

```java
package com.cloud.controller;

import com.cloud.apis.PayFeignApi;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderCircuitController {

    @Resource
    private PayFeignApi payFeignApi;

    @CircuitBreaker(name = "cloud-payment-service",fallbackMethod = "myCircuitFallback")
    @GetMapping(value = "/feign/pay/circuit/{id}")
    public String myCircuitBreaker(@PathVariable("id") Integer id){
        return payFeignApi.myCircuit(id);
    }

    public String myCircuitFallback(Throwable t){
        return "myCircuitFallback，系统繁忙，请稍后重试";
    }
}
```

访问：http://localhost:80/feign/pay/circuit/1

Hello Circuit! inputID：1	589caa871b00418bb84cb5eef5b2d3d3

访问：http://localhost:80/feign/pay/circuit/-4

myCircuitFallback，系统繁忙，请稍后重试

访问：http://localhost:80/feign/pay/circuit/9999

myCircuitFallback，系统繁忙，请稍后重试

服务降级

多点几次http://localhost:80/feign/pay/circuit/-4

接下来访问：http://localhost:80/feign/pay/circuit/1

myCircuitFallback，系统繁忙，请稍后重试

------

yml

基于TIME_BASED（时间的滑动窗口）

```yml
resilience4j:
  timelimiter:
    configs:
      default:
        timeout-duration: 10s # 默认限制远程1s，超过1s就超时异常，配置了降级，就走降级逻辑
  circuitbreaker:
    configs:
      default:
        failure-rate-threshold: 50
        slow-call-duration-threshold: 2s # 慢调用时间阈值，高于这个阈值的视为慢调用并增加慢调用比例
        slow-call-rate-threshold: 30 # 慢调用百分比峰值，超过百分之30就会开启断路器，慢调用时间为2s
        sliding-window-type: TIME_BASED # 滑动窗口的类型
        sliding-window-size: 2
        minimum-number-of-calls: 2
        permitted-number-of-calls-in-half-open-state: 2
        wait-duration-in-open-state: 5s
        record-exceptions:
          - java.lang.Exception
    instances:
      cloud-payment-service:
        base-config: default
```

------

小总结：

> 断路器开启或者关闭的条件

- 当满足一定的峰值和失败率达到一定条件后，断路器将会进入OPEN状态（保险丝跳闸），服务熔断
- 当OPEN的时候，所有请求都不会调用主业务逻辑方法，而是直接走fallbackmethod兜底方法，服务降级
- 一段时间之后，这个时候断路器会从OPEN进入到HALF_OPEN半开状态，会放几个请求过去探探链路是否通？如果成功，断路器会CLOSE（类似保险丝闭合，恢复可用）；如果失败，继续开启；一直重复上述



### 9.5.2、隔离（BulkHead）

官网：https://resilience4j.readme.io/docs/bulkhead

中文文档：https://github.com/lmhmhl/Resilience4j-Guides-Chinese/blob/main/core-modules/bulkhead.md

> 是什么？
>
> bulkhead（船的）舱壁 / （飞机的）隔板
>
> 限并发
>
> 隔板来自造船行业，船舱内部一般会分成很多小隔舱，一旦一个隔舱漏水因为隔板的存在而不至于影响其它隔舱和整体船。

> 能干什么？
>
> 依赖隔离&负载保护：用来限制对于下游服务的最大并发数量的限制

> Resilience4j提供了如下两种隔离的实现方式，可以限制并发执行的数量
>
> 1. SemaphoreBulkhead（信号量舱壁）
> 2. FiexedThreadPoolBulkhead（固定线程池舱壁）



#### 9.5.1.1、SemaphoreBulkhead

> 概述
>
> 信号量舱壁（SemaphoreBulkhead）原理
>
> 当信号量有空闲时，进入系统的请求会直接获取信号量并开始业务处理。
>
> 当信号来那个全被占用时，接下来的请求将会进入阻塞状态，SemaphoreBulkhead提供了一个阻塞计时器，如果阻塞状态的请求在阻塞计时内无法获取到信号量则系统会拒绝这些请求。
>
> 若请求在阻塞计时内获取到了信号量，那将直接获取信号量并执行相应的业务处理。

修改8001服务中PayCircuitController

```java
@GetMapping(value = "/pay/bulkhead/{id}")
public String myBulkhead(@PathVariable("id") Integer id) {
    if (id == -4) throw new RuntimeException("----bulkhead id 不能负数");
    if (id == 9999) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    return "Hello Bulkhead! inputID：" + id + "\t" + IdUtil.simpleUUID();
}
```

api中PayFeignApi接口新增舱壁api方法

```java
@GetMapping(value = "/pay/bulkhead/{id}")
public String myBulkhead(@PathVariable("id") Integer id);
```

修改cloud-consumer-feign-order80 => pom、yml、orderCircuitController

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-bulkhead</artifactId>
</dependency>
```

```yml
resilience4j:
  bulkhead:
    configs:
      default:
        max-concurrent-calls: 2 # 隔离允许并发线程执行的最大数量
        max-wait-duration: 1s # 当达到并发调用数量时，新的线程的阻塞时间，只愿意等待1秒，过时不候进舱壁兜底fallback
    instances:
      cloud-payment-service:
        base-config: default
  timelimiter:
    configs:
      default:
        timeout-duration: 20s
```

两个窗口访问：http://localhost/feign/pay/bulkhead/9999

一个窗口访问：http://localhost/feign/pay/bulkhead/1

没有延时的窗口会繁忙



#### 9.5.1.2、FiexedThreadPoolBulkhead

> 概述
>
> 固定线程池舱壁（FixedThreadPoolBulkhead）
>
> FixedThreadPoolBulkhead的功能与SemaphoreBulkhead一样也是用于限制并发执行的次数的，但是二者的实现原理存在差别而且表现效果也存在细微的差别。FixedThreadPoolBulkhead使用一个固定线程池和一个等待队列来实现舱壁。
>
> 当线程次中存在空闲时，则此时进入系统的请求将直接进入线程池开启新线程或使用空闲线程来处理请求。
>
> 当线程池中无空闲时，接下来的请求将进入等待队列，若等待队列仍然无剩余空间时接下来的请求将直接被拒绝，在队列中的请求等待线程池出现空闲时，将进入线程池进行业务处理。
>
> **另外：ThreadPoolBulkhead只对CompletableFuture方法有效，所以我们必创返回CompletableFuture类型的方法。**

修改cloud-consumer-feign-order80 => pom、yml、orderCircuitController

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-bulkhead</artifactId>
</dependency>
```

```yml
# 固定线程池舱壁（FixedThreadPoolBulkhead）
resilience4j:
  thread-pool-bulkhead:
    configs:
      default:
        core-thread-pool-size: 1
        max-thread-pool-size: 1 # 最高容纳数量 max-thread-pool-size + queue-capacity = 2
        queue-capacity: 1
    instances:
      cloud-payment-service:
        base-config: default
  timelimiter:
    configs:
      default:
        timeout-duration: 10s
```

```java
    //  threadPool
    @GetMapping(value = "/feign/pay/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkheadPoolFallback", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> myBulkheadPool(@PathVariable("id") Integer id) {
        System.out.println(Thread.currentThread().getName() + "\t" + "---开始进入");
        //
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "---准备离开");

        return CompletableFuture.supplyAsync(() -> payFeignApi.myBulkhead(id) + "\t" + "myBulkheadPool");
    }
    public CompletableFuture<String> myBulkheadPoolFallback(Integer id, Throwable t) {
        return CompletableFuture.supplyAsync(() -> "myBulkheadPoolFallback,系统繁忙，请稍后重试");
    }
```

分别访问：

http://localhost/feign/pay/bulkhead/1

http://localhost/feign/pay/bulkhead/2

http://localhost/feign/pay/bulkhead/3

第三个就会线程繁忙



### 9.5.3、限流（RateLimiter）

官网：https://resilience4j.readme.io/docs/ratelimiter

Github：https://github.com/lmhmhl/Resilience4j-Guides-Chinese/blob/main/core-modules/ratelimiter.md

> 是什么？
>
> 限流，就是限制最大访问流量。系统能提供的最大并发是有限的，同时来的请求又太多，就需要限流。

> 面试题：说说常见限流算法
>
> 1. 漏斗算法（Leaky Bucket）
>
>    ![image-20240613230900228](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240613230900228.png)
>
>    ![image-20240613231003172](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240613231003172.png)
>
> 2. 令牌桶算法（Token Bucket） - > Spring Cloud默认使用该算法
>
> 3. 滚动时间窗（Tumbling Time Window）
>
>    ![image-20240613231429726](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240613231429726.png)
>
>    缺点：间隔临界的一段时间内的请求就会超过系统限制，可能导致系统被压垮
>
> 4. 滑动时间窗（Sliding Time Window）



 cloud-provider-payment8001支付微服务 ->  修改PayCircuitController新增myRatelimit方法

```java
//  限流
@GetMapping(value = "/pay/rateLimit/{id}")
public String myRateLimit(@PathVariable("id") Integer id) {
    return "Hello myRateLimit! inputID：" + id + "\t" + IdUtil.simpleUUID();
}
```

api模块中 -> PayFeignApi接口新增限流api方法

```java
@GetMapping(value = "/pay/rateLimit/{id}")
public String myRateLimit(@PathVariable("id") Integer id);
```

修改cloud-consumer-feign-order80 -> pom、yml、order的controller

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-ratelimiter</artifactId>
</dependency>
```

```yml
# 限流器
resilience4j:
  timelimiter:
    configs:
      default:
        timeout-duration: 10s
  ratelimiter:
    configs:
      default:
        limit-for-period: 2 # 在一次刷新周期内，允许执行的最大请求
        limit-refresh-period: 1s # 限流器每隔LimitRefreshPeriod舒心一次，将允许处理的最大请求数量重置为LimitForPeriod
        timeout-duration: 1 # 线程等待权限的默认等待时间
    instances:
      cloud-payment-service:
        base-config: default
```

```java
@GetMapping(value = "/feign/pay/ratelimit/{id}")
@RateLimiter(name="cloud-payment-service",fallbackMethod = "myRateLimitFallback")
public String myBulkheadRateLimit(@PathVariable("id") Integer id) {
    return payFeignApi.myRateLimit(id);
}

public String myRateLimitFallback(){
    return "你被限流了，禁止访问";
}
```



# 10、Sleuth（Mirometer）+ZipKin分布式链路追踪

## 10.1、Sleuth目前也进入维护模式

> Spring Cloud Sleuth’s last minor version is 3.1. You can check the [3.1.x](https://github.com/spring-cloud/spring-cloud-sleuth/tree/3.1.x) branch for the latest commits. The core of this project got moved to [Micrometer Tracing](https://micrometer.io/docs/tracing) project and the instrumentations will be moved to [Micrometer](https://micrometer.io/) and all respective projects (no longer all instrumentations will be done in a single repository). 



## 10.2、分布式链路追踪概述

> Q：为什么会出现这个技术？需要解决哪些问题？
>
> 在微服务框架中，一个由客户发起的请求在后端系统中会经过多个不同的服务节点调用来协同产生最后的请求结果，每一个前端请求都会形成一条复杂的分布式服务调用链路，链路中的任何一环出现高延时或错误都会引起整个请求最后的失败。
>
> 在大规模分布式微服务集群下，如何实时观测系统的整体调用链路情况。
>
> 在大规模分布式微服务集群下，如何快速发现并定位到问题。
>
> 在大规模分布式微服务集群下，如何尽可能精确的判断故障对系统的影响范围与影响程度。
>
> 在大规模分布式微服务集群下，如何尽可能精确的梳理出服务之间的依赖关系，并判断出服务之间的依赖关系是否合理。
>
> 在大规模分布式微服务集群下，如何尽可能精确的分析整个系统调用链路的性能与瓶颈点。
>
> 在大规模分布式微服务集群下。如何尽可能精确的分析系统的存储瓶颈与容量规划。
>
> 综上所述：
>
> 分布式链路追中技术要解决的问题，分布式链路追踪（Distributed Tracing），就是将一次分布式请求还原成调用链路，进行日志记录，性能监控并将一次分布式请求的调用情况集中展示。比如各个服务节点上的耗、请求具体到达哪台机器上、每个服务节点的请求状态等等。



## 10.3、Spring Cloud Sleuth: Micrometer

官网：https://micrometer.io/docs/tracing

Github：https://github.com/spring-cloud/spring-cloud-sleuth

**Spring Cloud Sleuth 无法与 Spring Boot 3.x 及更高版本兼容。Sleuth 支持的 Spring Boot 的最后一个主要版本是 2.x。**

Spring Cloud Sleuth（Micrometer）提供了一套完整的分布式链路追踪（Distributed Tracing）解决方案且兼容支持了ZipKin展现

小总结：将一次分布式请求还原成调用链路，进行日志记录和性能监控，并将一次分布式请求的调用情况集中web展示



行业内比较成熟的其他分布式链路追踪技术解决方案

| 技术       | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| Cat        | 由大众点评开源，基于Java开发的实时监控平台，包括实时引用监控，业务监控。集成方案是通过代码埋点的方式来时间监控，比如：拦截器、过滤器等。对代码的侵入性很大，集成成本加高，风险较大。 |
| Zipkin     | 由Twitter公司开源，开放源代码分布式的跟踪系统，用于收集服务的定时数据，以解决微服务架构中的延迟问题，包括：数据的手机、存储，查询和展现。结合Spring-Cloud-Sleuth使用较为简单，集成方便，但是功能较简单。 |
| Pinpoint   | Pinpoint是一款开源的基于字节码注入的调用链分析，以及应用监控分析工具。特点是支持多种插件，UI功能强大，介入端无代码侵入。 |
| SkyWalking | SkyWalking是国人开源的基于字节码注入的调用链分析，以及引用监控分析工具。特点是支持多种插件，UI功能较强，接入端无代码侵入。 |



## 10.4、分布式链路追踪原理

一条链路追踪会在每个服务调用的时候加上TraceID和SpanID

链路通过TraceId唯一标识，Span标识发起的请求信息，各Span通过Parent ID关联起来（Span：表示调用链路来源，通俗的理解Span就是一次请求信息）



## 10.5、Zipkin

官网：https://zipkin.io/



### 10.5.1、是什么？

ZipKin是一种分布式链路跟踪系统图形化的工具，ZipKin是Twitter开源的分布式跟踪系统，能够手机微服务运行过程中的实时调用链路信息，并能够将这些调用链路信息展示到Web图形化界面后三个供开发人员分析，开发人员能够给从ZipKin中分析出调用链路中的性能瓶颈，识别出存在问题的应用程序，进而定位问题和解决问题。



### 10.5.2、Zipkin为什么出现？

单有Sleuth（Micrometer）行不行？

当配置了Sleuth链路追踪的时候，追踪的信息第一个是Trace ID，第二个是Span ID。只有日志没有图，观看不方便，不美观。



### 10.5.3、下载

官网：https://zipkin.io/pages/quickstart.html

下载：https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec

下载：https://repo1.maven.org/maven2/io/zipkin/zipkin-server/

> java -jar xxx.jar
>

> Regardless of how you start Zipkin, browse to http://your_host:9411 to find traces!
>
> 访问：localhost:9411/zipkin/



## 10.6、Micrometer+ZipKin搭建链路监控案例

Micrometer：数据采样

Zipkin：图形展示

------

父工程POM：

```xml
<micrometer-tarcing-version>1.2.0</micrometer-tarcing-version>
<micrometer-observation-version>1.12.0</micrometer-observation-version>
<feign-micrometer.version>12.5</feign-micrometer.version>
<zipkin-reporter-brave.version>2.17.0</zipkin-reporter-brave.version>
```

```xml
<!--micrometer-tracing-bom导入链路追踪版本中心 1-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bom</artifactId>
    <version>${micrometer-tarcing-version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<!--micrometer-tracing指标追踪 2-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing</artifactId>
    <version>${micrometer-tarcing-version}</version>
</dependency>

<!--micrometer-tracing-bridge-brave适配zipkin的桥接包 3-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
    <version>${micrometer-tarcing-version}</version>
</dependency>

<!--micrometer-observation 4-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-observation</artifactId>
    <version>${micrometer-observation-version}</version>
</dependency>

<!-- feign-micrometer 5-->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-micrometer</artifactId>
    <version>${feign-micrometer.version}</version>
</dependency>

<!-- zipkin-reporter-brave 6-->
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
    <version>${zipkin-reporter-brave.version}</version>
</dependency>
```

引入的Jar分别是什么意思？

由于Micrometer Tracing是一个门面工具自身并没有实现完整的链路追踪系统，具有的链路追踪另外需要引入的是第三方链路追踪系统的依赖：

| 序号 | 包名                            | 说明                                                         |
| :--- | :------------------------------ | :----------------------------------------------------------- |
| 1    | micrometer-tracing-bom          | 导入链路追踪版本中心，体系化说明                             |
| 2    | micrometer-tracing              | 指标追踪                                                     |
| 3    | micrometer-tracing-bridge-brave | 一个Micrometer模块，用于分布式跟踪工具Brave集成，以收集应用程序的分布式跟踪数据。Brave是一个开源的分布式跟踪工具，它可以帮助用户在分布式系统中跟踪请求的流转，它使用一种称为”跟踪上下文“的机制，将请求的跟踪信息存储在请求的头部，然后将请求传递给下一个服务。在整个请求链中，Brave会将每个服务处理请求的时间和其他信息存储到跟踪数据中，以便用户可以了解整个请求路径和性能 |
| 4    | micrometer-observation          | 一个基于度量库Micrometer的观测模块，用于手机应用程序的度量数据 |
| 5    | feign-micrometer                | 一个Feign HTTP客户端的Micrometer模块，用于手机客户端请求的度量数据 |
| 6    | zipkin-reporter-brave           | 一个用于将Brave跟踪数据报告到Zipkin跟踪系统的库              |

补充包：spring-boot-starter-actuator Spring Boot框架的一个模块用于监视和管理应用程序

------

8001服务提供者 -> pom、yml、PayMicrometerController

```xml
<!-- Zipkin-->
<!--micrometer-tracing指标追踪 2-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing</artifactId>
</dependency>

<!--micrometer-tracing-bridge-brave适配zipkin的桥接包 3-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>

<!--micrometer-observation 4-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-observation</artifactId>
</dependency>

<!-- feign-micrometer 5-->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-micrometer</artifactId>
</dependency>

<!-- zipkin-reporter-brave 6-->
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

```yml
# Zipkin
management:
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
  tracing:
    sampling:
      probability: 1.0 # 采样率默认为0.1（0.1就是10次只能有一次被记录下来），值越大收集越及时
```

```java
@RestController
public class PayMicrometerController {

    @GetMapping(value = "/pay/micrometer/{id}")
    public String myMicrometer(@PathVariable("id") Integer id) {
        return "Hello, myMicrometer inputId：" + id + "\t  服务返回：" + IdUtil.simpleUUID();
    }
}
```

------

PayFeignApi

```java
@GetMapping(value = "/pay/micrometer/{id}")
public String myMicrometer(@PathVariable("id") Integer id);
```

------

80服务调用者 -> pom、yml、OrderMicrometerController

```xml
<!-- Zipkin-->
<!--micrometer-tracing指标追踪 2-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing</artifactId>
</dependency>

<!--micrometer-tracing-bridge-brave适配zipkin的桥接包 3-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>

<!--micrometer-observation 4-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-observation</artifactId>
</dependency>

<!-- feign-micrometer 5-->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-micrometer</artifactId>
</dependency>

<!-- zipkin-reporter-brave 6-->
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

```yml
# Zipkin
management:
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
  tracing:
    sampling:
      probability: 1.0 # 采样率默认为0.1（0.1就是10次只能有一次被记录下来），值越大收集越及时
```

```java
@RestController
public class OrderMicrometerController {

    @Resource
    private PayFeignApi payFeignApi;

    @GetMapping(value = "/feign/micrometer/{id}")
    public String myMicrometer(@PathVariable("id") Integer id){
        return payFeignApi.myMicrometer(id);
    }
}
```

------

测试：

启动Zipkin

访问：http://localhost/feign/micrometer/1

![image-20240615152726631](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240615152726631.png)



# 11、Gateway网关

## 11.1、概述

### 11.1.1、是什么？

官网：https://docs.spring.io/spring-cloud-gateway/reference/

Gateway是在Spring生态系统之上构建的API网关服务，基于Spring6，Spring Boot 3和Project Reactor等技术。它旨在为微服务架构提供一种简单有效的统一的API路由管理方式，并在为它们提供跨领域的关注点，例如：安全性、监控、度量和恢复能力。

系统定位：

Cloud全家桶中有个很重要的组件就是网关，在1.x版本中都是采用的Zuul网关；

但是在2.x版本中，Zuul的升级一直跳票，Spring Cloud最后自己研发了一个网关Spring Cloud Gateway替代Zuul



### 11.1.2、微服务架构中网关在哪里？

![image-20240615162128092](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240615162128092.png)



### 11.1.3、能干嘛？

1. 反向代理
2. 鉴权
3. 流量控制
4. 熔断
5. 日志监控



### 11.1.4、总结

Spring Cloud Gateway组件的核心是一系列的过滤器，通过这些过滤器可以将客户端发送的请求转发（路由）到对应的微服务。

Spring Cloud Gateway是加在整个微服务最前沿的防火墙和代理器，隐藏微服务节点IP端口信息，从而加强安全保护。

Spring Cloud Gateway本身也是一个微服务，需要注册进服务注册中心。



## 11.2、Gateway三大核心

官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/glossary.html

- **Route（路由）**: The basic building block of the gateway. It is defined by an ID, a destination URI, a collection of predicates, and a collection of filters. A route is matched if the aggregate predicate is true.
- **Predicate（断言）**: This is a [Java 8 Function Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html). The input type is a [Spring Framework `ServerWebExchange`](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/server/ServerWebExchange.html). This lets you match on anything from the HTTP request, such as headers or parameters.
- **Filter（过滤器）**: These are instances of [`GatewayFilter`](https://github.com/spring-cloud/spring-cloud-gateway/blob/main/spring-cloud-gateway-server/src/main/java/org/springframework/cloud/gateway/filter/GatewayFilter.java) that have been constructed with a specific factory. Here, you can modify requests and responses before or after sending the downstream request.

Route：路由是构建网关的基本模块，它由ID，目标URI，一系列的断言和过滤器组成，如果断言为true，则匹配该路由

Predicate：参考的是Java8的java.util.function.Predicate开发人员可以匹配HTTP请求中的所有内容（例如请求头或者请求参数），如果请求与断言相匹配则进行路由

Filter：指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或之后对请求进行修改



总结：

web前端请求，通过一些匹配条件，定位到真正的服务节点。并在这个转发过程的前后，进行一些精细化控制。

predicate就是匹配条件。

filter就可以理解为一个无所不能的拦截器。有了这两个元素，再加上目标url，就可以实现一个具体的路由了



## 11.3、Gateway工作流程

官网；https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/how-it-works.html

![image-20240615165057630](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240615165057630.png)

客户端向Spring Cloud Gateway发出请求。然后Gateway Handler Mapping中找到与请求相匹配的路由，将其发送到Gateway Wab Handler。Handler再通过执行的过滤器链来将请求发送到实际的服务执行业务逻辑，然后返回。

过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（Pre）或之后（Post）执行业务逻辑。

在“pre”类型的过滤器可以做参数校验、权限校验、流量控制、日志输出、协议转换等。

在“post”类型的过滤器中可以做相应内容、响应头的修改、日志输出、流量监控等有着非常重要的作用。

核心逻辑：路由转发 +  断言判断 + 执行过滤器链



## 11.4、入门配置

建module：cloud-gateway9527

pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloud-gateway9527</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Gateway-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!-- 服务注册中心consul discovery，网关也要注册进服务注册中心同意管控-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
        <!-- 指标监控健康检查的actuator，网关是响应式变成删除掉spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

yml

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway # 以微服务注册进consul或nacos服务列表内
  cloud:
    consul: # 配置consul地址
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true
        service-name: ${spring.application.name}
```

主启动类

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 服务注册和发现
public class Main9527 {
    public static void main(String[] args) {
        SpringApplication.run(Main9527.class, args);
    }
}
```



## 11.5、9527网关如何做路由映射

诉求：不暴露8001服务

8001：PayGatewayController

```java
package com.cloud.controller;

import cn.hutool.core.util.IdUtil;
import com.cloud.entities.Pay;
import com.cloud.resp.ResultData;
import com.cloud.service.PayService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayGatewayController {

    @Resource
    private PayService payService;

    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo() {
        return ResultData.success("gateway info" + IdUtil.simpleUUID());
    }
}
```



Gateway：

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway # 以微服务注册进consul或nacos服务列表内
  cloud:
    consul: # 配置consul地址
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true
        service-name: ${spring.application.name}
    gateway:
      routes:
        - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
          uri: http://localhost:8001 # 匹配后提供服务的路由地址
          predicates:
            - Path=/pay/gateway/get/** # 断言，路径相匹配的进行路由

        - id: pay_routh1
          uri: http://localhost:8001
          predicates:
            - Path=/pay/gateway/info/**
```

访问：

http://localhost:9527/pay/gateway/get/1

http://localhost:9527/pay/gateway/info



1. 修改cloud-api-commons -> PayFeignApi接口

   ```java
   @GetMapping(value = "/pay/gateway/get/{id}")
   public ResultData<?> getById(@PathVariable("id") Integer id);
   
   @GetMapping(value = "/pay/gateway/info")
   public ResultData<String> getGatewayInfo();
   ```

2. cloud-consumer-feign-order80 -> 新建OrderGatewayController

   ```java
   package com.cloud.controller;
   
   import com.cloud.apis.PayFeignApi;
   import com.cloud.resp.ResultData;
   import jakarta.annotation.Resource;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RestController;
   
   @RestController
   public class OrderGatewayController {
       @Resource
       private PayFeignApi payFeignApi;
   
       @GetMapping("/feign/pay/gateway/get/{id}")
       public ResultData<?> getById(@PathVariable("id") Integer id){
           return payFeignApi.getById(id);
       }
   
       @GetMapping(value = "/feign/pay/gateway/info")
       public ResultData<String> getGatewayInfo(){
           return payFeignApi.getGatewayInfo();
       }
   }
   ```

3. 网关开启

   访问：http://localhost/feign/pay/gateway/get/1

4. 网关关闭

   访问：http://localhost/feign/pay/gateway/get/1

   发现两次都可以访问，是因为PayFeignApi绑定的是cloud-payment-service

5. 结论

   所以修改PayFeignApi

   ```java
   @FeignClient(value = "cloud-gateway")
   public interface PayFeignApi {}
   ```

   这样关闭网关，就不能访问了



## 11.6、Gateway高级特性

###  11.6.1、Route以微服务名-动态获取服务URL

> 网关配置：写死服务端口（X）
>
> 官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/configuring-route-predicate-factories-and-filter-factories.html

yml

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway # 以微服务注册进consul或nacos服务列表内
  cloud:
    consul: # 配置consul地址
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true
        service-name: ${spring.application.name}
    gateway:
      routes:
        - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
          uri: lb://cloud-payment-service # 微服务的名字
#          uri: http://localhost:8001 # 匹配后提供服务的路由地址
          predicates:
            - Path=/pay/gateway/get/** # 断言，路径相匹配的进行路由

        - id: pay_routh1
          uri: lb://cloud-payment-service # 微服务的名字
#          uri: http://localhost:8001
          predicates:
            - Path=/pay/gateway/info/**
```



### 11.6.2、Predicate断言（谓词）

> 官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/request-predicates-factories.html
>
> Spring Cloud Gateway matches routes as part of the Spring WebFlux `HandlerMapping` infrastructure. Spring Cloud Gateway includes many built-in route predicate factories. All of these predicates match on different attributes of the HTTP request. You can combine multiple route predicate factories with logical `and` statements.
>
> There are two ways to configure predicates and filters: shortcuts and fully expanded arguments. Most examples below use the shortcut way.
>
> The name and argument names are listed as `code` in the first sentence or two of each section. The arguments are typically listed in the order that are needed for the shortcut configuration.
>
> https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/configuring-route-predicate-factories-and-filter-factories.html

两种方法配置Shortcuts Configuration和Fully Expanded Arguments



**The After Route Predicate Factory**

如何获得ZonedDateTime

```java
ZonedDateTime dateTime = ZonedDateTime.now();
System.out.println(dateTime);
```

```yml
    gateway:
      routes:
        - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
          uri: lb://cloud-payment-service # 微服务的名字
#          uri: http://localhost:8001 # 匹配后提供服务的路由地址
          predicates:
#            - Path=/pay/gateway/get/** # 断言，路径相匹配的进行路由
             # 多久之后才能访问
            -  After=2024-06-15T23:17:08.307826900+08:00[Asia/Shanghai]
```

**The Before Route Predicate Factory**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 多久之前才能访问
        - Before=2024-06-15T23:17:08.307826900+08:00[Asia/Shanghai]
```

**The Between Route Predicate Factory**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 多久之间才能访问
        - Between=2024-06-15T23:17:08.307826900+08:00[Asia/Shanghai],2024-06-15T23:20:08.307826900+08:00[Asia/Shanghai]
```

**The Cookie Route Predicate Factory**

Cookie Route Predicate需要两个参数，一个是Cookie name，一个是正则表达式。

路由规则会通过获取对应的Cookie name值和正则表达式去匹配，如果匹配上，如果没有匹配上则不执行

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 多久之后才能访问
        - After=2024-06-15T23:17:08.307826900+08:00[Asia/Shanghai]
        - Cookie=username, ch.p
```

cmd：

```shell
curl http://localhost:9527/pay/gateway/get/1 --cookie "username=ch.p"
```

**The Header Route Predicate Factory**

请求头要有X-Request-Id属性并且值为整数的正则表达式

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 请求头要有X-Request-Id属性并且值为整数的正则表达式
        - Header=X-Request-Id, \d+
```

cmd：

```shell
curl http://localhost:9527/pay/gateway/get/1 -H "X-Request-Id:123456"
```

**The Host Request Predicate**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 请求头要有Host属性并且值为xxx
        - Host=**.somehost.org,**.anotherhost.org
```

cmd：

```shell
curl http://localhost:9527/pay/gateway/get/1 -H "Host:www.disney.com"
```

**The Path Request Predicate**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 符合哪个路径
         - Path=/red/{segment},/blue/{segment}
```

**The Query Request Predicate**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        # 支持传入两个参数，一个是属性名，一个是属性值，属性值可以是正则表达式
         - Query=green
```

**The Weight Request Predicate**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        - Weight=group1, 2
```

**The Method Request Predicate**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        - Method=GET,POST # 允许的访问方法
```

**RemoteAddr**

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
        uri: lb://cloud-payment-service # 微服务的名字
        predicates:
        - RemoteAddr=192.168.124.1/24 # 外部访问IP限制，最大跨度不超过32，目前是1~24它们是CIDR表示法
```



小总结：

Predicate就是为了实现一组匹配规则，让请求过来找到对应的Route进行处理。

自定义断言：

```java
package com.cloud.MyGateway;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;


//  自定义故意配置userType，按照钻、金gold、银和yml配置的会员等级，以适配是否可以访问
@Component
public class MyRouterPredicateFactory extends AbstractRoutePredicateFactory<MyRouterPredicateFactory.Config> {

    public MyRouterPredicateFactory() {
        super(MyRouterPredicateFactory.Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyRouterPredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                //	http://localhost:9527/pay/gateway/get/1?userType=gold
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");
                if (userType == null) {
                    return false;
                }

//                if (userType.equalsIgnoreCase(config.getUserType())){
//                    return true;
//                }
//                return false;
                //  如果说参数存在，就和config的数据进行比较
                return userType.equalsIgnoreCase(config.getUserType());
            }
        };
    }

    // 这个Config就是路由断言规则
    @Getter
    @Setter
    @Validated
    public static class Config {
        @NotEmpty
        private String userType;
    }
}
```

```yml
spring:
  application:
    name: cloud-gateway # 以微服务注册进consul或nacos服务列表内
  cloud:
    consul: # 配置consul地址
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true
        service-name: ${spring.application.name}
    gateway:
      routes:
        - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
          uri: lb://cloud-payment-service # 微服务的名字
          #          uri: http://localhost:8001 # 匹配后提供服务的路由地址
          predicates:
            - Path=/pay/gateway/info/**
          #            - Path=/pay/gateway/get/** # 断言，路径相匹配的进行路由
          # 多久之后能访问
        #            -  After=2024-06-15T23:17:08.307826900+08:00[Asia/Shanghai]



        - id: pay_routh1
          uri: lb://cloud-payment-service # 微服务的名字
          #          uri: http://localhost:8001
          predicates:
#            - Path=/pay/gateway/info/**
              # 定义的My自定义断言，这样写会报错
#            - MyRouterPredicateFactory=gold
            - name: MyRouterPredicateFactory
              args:
                userType: gold
```

![image-20240617161131943](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240617161131943.png)

访问：http://localhost:9527/pay/gateway/get/1?userType=gold

开启简写

```java
package com.cloud.MyGateway;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;


//  自定义故意配置userType，按照钻、金gold、银和yml配置的会员等级，以适配是否可以访问
@Component
public class MyRouterPredicateFactory extends AbstractRoutePredicateFactory<MyRouterPredicateFactory.Config> {

    public MyRouterPredicateFactory() {
        super(MyRouterPredicateFactory.Config.class);
    }

    //  可以简写方式
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("userType");
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyRouterPredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");
                if (userType == null) {
                    return false;
                }

//                if (userType.equalsIgnoreCase(config.getUserType())){
//                    return true;
//                }
//                return false;
                //  如果说参数存在，就和config的数据进行比较
                return userType.equalsIgnoreCase(config.getUserType());
            }
        };
    }

    // 这个Config就是路由断言规则
    @Getter
    @Setter
    @Validated
    public static class Config {
        @NotEmpty
        private String userType;
    }
}
```

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway # 以微服务注册进consul或nacos服务列表内
  cloud:
    consul: # 配置consul地址
      host: localhost
      port: 8500
      discovery:
        prefer-ip-address: true
        service-name: ${spring.application.name}
    gateway:
      routes:
        - id: pay_routh1 # 路由的ID（类似mysql主键），没有固定规则但要求唯一
          uri: lb://cloud-payment-service # 微服务的名字
          #          uri: http://localhost:8001 # 匹配后提供服务的路由地址
          predicates:
            - Path=/pay/gateway/info/**
          #            - Path=/pay/gateway/get/** # 断言，路径相匹配的进行路由
          # 多久之后能访问
        #            -  After=2024-06-15T23:17:08.307826900+08:00[Asia/Shanghai]



        - id: pay_routh1
          uri: lb://cloud-payment-service # 微服务的名字
          #          uri: http://localhost:8001
          predicates:
#            - Path=/pay/gateway/info/**
              # 定义的My自定义断言，这样写会报错、、开启了简写就可以使用这个方法
            - MyRouterPredicateFactory=gold
#            - name: MyRouterPredicateFactory
#              args:
#                userType: gold
```



### 11.6.3、Filter过滤

官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-mvc/gateway-handler-filter-functions.html

> 类似Spring MVC里面的拦截器Interceptor，Servlet的过滤器
>
> “pre”和“post”分别会在请求被执行前调用和被执行后调用，用来修改请求和响应信息

> 能干嘛？
>
> 请求鉴权、异常处理、记录接口调用时长统计

> 类型
>
> 1. 全局默认过滤器Global Filters
>
>    官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/global-filters.html
>
>    gateway出厂默认已有的，直接用即可，主要作用于所有的路由
>
>    不需要在配置文件中配置，作用在所有的路由上，实现GlobalFilter接口即可
>
> 2. 单一内置过滤器GatewayFilter
>
>    官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories.html
>
>    写法类似于断言
>
>    ```yml
>    spring:
>      cloud:
>        gateway:
>          routes:
>          - id: add_request_header_route
>            uri: https://example.org
>            filters:
>            - AddRequestHeader=X-Request-red, blue
>    ```
>
> 3. 自定义过滤器
>
>    官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/global-filters.html
>
>    MyGlobalFilter
>
>    ```java
>    package com.cloud.MyGateway;
>    
>    import lombok.extern.slf4j.Slf4j;
>    import org.springframework.cloud.gateway.filter.GatewayFilterChain;
>    import org.springframework.cloud.gateway.filter.GlobalFilter;
>    import org.springframework.core.Ordered;
>    import org.springframework.stereotype.Component;
>    import org.springframework.web.server.ServerWebExchange;
>    import reactor.core.publisher.Mono;
>    
>    @Component
>    @Slf4j
>    public class MyGlobalFilter implements GlobalFilter, Ordered {
>    
>        public static final String BEGIN_VISIT_TIME = "begin_visit_time"; //开始调用方法的时间
>    
>        @Override
>        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
>    
>            //1、先记录访问接口的访问时间
>            exchange.getAttributes().put(BEGIN_VISIT_TIME, System.currentTimeMillis());
>            //2、返回统计的各个结果给后台
>            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
>                Long beginVisitTime = exchange.getAttribute(BEGIN_VISIT_TIME);
>                if (beginVisitTime != null) {
>                    log.info("访问接口主机：{}", exchange.getRequest().getURI().getHost());
>                    log.info("访问接口端口：{}", exchange.getRequest().getURI().getPort());
>                    log.info("访问接口URL：{}", exchange.getRequest().getURI().getPath());
>                    log.info("访问接口URL后面参数：{}", exchange.getRequest().getURI().getRawQuery());
>                    log.info("访问接口时长：{}毫秒", System.currentTimeMillis() - beginVisitTime);
>                    log.info("=========分割线========");
>                    System.out.println();
>                }
>            }));
>        }
>    
>        /***
>         * 数字越小，优先级越高
>         * @return int
>         */
>        @Override
>        public int getOrder() {
>            return 0;
>        }
>    }
>    ```
>
>    访问：http://localhost:9527/pay/gateway/info?userType=gold
>
>    访问：http://localhost:9527/pay/gateway/get/1
>
>    访问：http://localhost:9527/pay/gateway/filter
>
>    ```shell
>    2024-06-17T22:30:44.496+08:00  INFO 10708 --- [cloud-gateway] [ctor-http-nio-1] com.cloud.MyGateway.MyGlobalFilter       : 访问接口主机：localhost
>    2024-06-17T22:30:44.496+08:00  INFO 10708 --- [cloud-gateway] [ctor-http-nio-1] com.cloud.MyGateway.MyGlobalFilter       : 访问接口端口：9527
>    2024-06-17T22:30:44.496+08:00  INFO 10708 --- [cloud-gateway] [ctor-http-nio-1] com.cloud.MyGateway.MyGlobalFilter       : 访问接口URL：/pay/gateway/filter
>    2024-06-17T22:30:44.496+08:00  INFO 10708 --- [cloud-gateway] [ctor-http-nio-1] com.cloud.MyGateway.MyGlobalFilter       : 访问接口URL后面参数：null
>    2024-06-17T22:30:44.496+08:00  INFO 10708 --- [cloud-gateway] [ctor-http-nio-1] com.cloud.MyGateway.MyGlobalFilter       : 访问接口时长：11毫秒
>    2024-06-17T22:30:44.496+08:00  INFO 10708 --- [cloud-gateway] [ctor-http-nio-1] com.cloud.MyGateway.MyGlobalFilter       : =========分割线========
>    ```

------

> Gateway内置的过滤器
>
> 是什么？
>
> 官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories.html
>
> 常见的内置过滤器：
>
> 1. 请求头（RequestHeader）相关组
>
>    添加请求头
>
>    8001：PayGatewayController
>
>    ```java
>    @GetMapping(value = "/pay/gateway/filter")
>    public ResultData<String> getGatewayFilter(HttpServletRequest request) {
>        String result = "";
>        Enumeration<String> headers = request.getHeaderNames();
>        while (headers.hasMoreElements()) {
>            String headName = headers.nextElement();
>            String headValue = request.getHeader(headName);
>            System.out.println("请求头名字：" + headName + "\t\t\t" + "请求值：" + headValue);
>            if (headName.equalsIgnoreCase("X-Request-disney1") ||
>                headName.equalsIgnoreCase("X-Request-disney2")) {
>                result = result + headName + "\t" + headValue + " ";
>            }
>        }
>        return ResultData.success("getGatewayFilter 过滤器 test：" + result + "\t" + DateUtil.now());
>    }
>    ```
>
>    9527：yml
>
>    ```java
>            - id: pay_routh3
>              uri: lb://cloud-payment-service # 微服务的名字
>              predicates:
>                - Path=/pay/gateway/filter/**
>              filters:
>                - AddRequestHeader=X-Request-disney1,disney1
>                - AddRequestHeader=X-Request-disney2,disney2
>    ```
>
>    访问：http://localhost:9527/pay/gateway/filter
>
>    ------
>
>    删除某个请求头
>
>    9527：yml
>
>    ```yml
>              filters:
>                - AddRequestHeader=X-Request-disney1,disneyValue1
>                - AddRequestHeader=X-Request-disney2,disneyValue2
>                - RemoveRequestHeader=sec-fetch-site # 删除请求头sec-fetch-site
>    ```
>
>    ------
>
>    修改某个请求头
>
>    9527：yml
>
>    ```yml
>              filters:
>                - AddRequestHeader=X-Request-disney1,disneyValue1
>                - AddRequestHeader=X-Request-disney2,disneyValue2
>                - RemoveRequestHeader=sec-fetch-site # 删除请求头sec-fetch-site
>                - SetRequestHeader=sec-fetch-mode, Blue-updateByDisney # 将请求头sec-fetch-mode对象的值修改为Blue-updateByDisney
>    ```
>
> 2. 请求参数（RequestParameter）相关组
>
>    9527：yml
>
>    新增请求参数、删除请求参数
>
>    ```yml
>    filters:
>      - AddRequestHeader=X-Request-disney1,disneyValue1
>      - AddRequestHeader=X-Request-disney2,disneyValue2
>      - RemoveRequestHeader=sec-fetch-site # 删除请求头sec-fetch-site
>      - SetRequestHeader=sec-fetch-mode, Blue-updateByDisney # 将请求头sec-fetch-mode对象的值修改为Blue-updateByDisney
>      - AddRequestParameter=customerId,9527001 # 新增请求参数Parameter：k,v
>      - RemoveRequestParameter=customerName # 删除URL请求参数customerName，，及时传过来了也是NULL
>    ```
>
>    8001：PayGatewayController
>
>    ```java
>    @GetMapping(value = "/pay/gateway/filter")
>    public ResultData<String> getGatewayFilter(HttpServletRequest request) {
>        String result = "";
>        Enumeration<String> headers = request.getHeaderNames();
>        while (headers.hasMoreElements()) {
>            String headName = headers.nextElement();
>            String headValue = request.getHeader(headName);
>            System.out.println("请求头名字：" + headName + "\t\t\t" + "请求值：" + headValue);
>            if (headName.equalsIgnoreCase("X-Request-disney1") ||
>                    headName.equalsIgnoreCase("X-Request-disney2")) {
>                result = result + headName + "\t" + headValue + " ";
>            }
>        }
>    
>        System.out.println("============================");
>    
>        String customerId = request.getParameter("customerId");
>        System.out.println("request customerId:" + customerId);
>        String customerName = request.getParameter("customerName");
>        System.out.println("request customerName:" + customerName);
>    
>        System.out.println("============================");
>        return ResultData.success("getGatewayFilter 过滤器 test：" + result + "\t" + DateUtil.now());
>    }
>    ```
>
>    访问：http://localhost:9527/pay/gateway/filter
>
>    访问：http://localhost:9527/pay/gateway/filter?customerId=123&customerName=321
>
> 3. 回应头（ResponseHeader）相关组
>
>    添加回应头、设置回应头、删除回应头
>
>    ```yml
>              filters:
>                - AddRequestHeader=X-Request-disney1,disneyValue1
>                - AddRequestHeader=X-Request-disney2,disneyValue2
>                - RemoveRequestHeader=sec-fetch-site # 删除请求头sec-fetch-site
>                - SetRequestHeader=sec-fetch-mode, Blue-updateByDisney # 将请求头sec-fetch-mode对象的值修改为Blue-updateByDisney
>                - AddRequestParameter=customerId,9527001 # 新增请求参数Parameter：k,v
>                - RemoveRequestParameter=customerName # 删除URL请求参数customerName，，及时传过来了也是NULL
>                - AddResponseHeader=X-Response-Disney, BlueResponse # 新增请求参数X-Response-Disney并设值为BlueResponse
>                - SetResponseHeader=Date,2099-11-11 # 设置回应头值为2099-11-11
>                - RemoveResponseHeader=Content-Type # 将默认自带Content-Type回应属性删除
>    ```
>
> 4. 前缀和路径相关组
>
>    自动添加路径前缀
>
>    之前正确的地址：http://localhost:9527/pay/gateway/filter
>
>    ```yml
>            - id: pay_routh3
>              uri: lb://cloud-payment-service # 微服务的名字
>              predicates:
>    #            - Path=/pay/gateway/filter/**
>                - Path=/gateway/filter/** # 断言，为了配合PrefixPath测试过了，暂时注释掉/pay
>              filters:
>                 - PrefixPath=/pay # http://localhost:9527/pay/gateway/filter # 被拆分为 PrefixPath + Path
>    ```
>
>    访问：http://localhost:9527/pay/gateway/filter 404
>
>    访问：http://localhost:9527/gateway/filter 200
>
>    带占位符
>
>    ```yml
>              predicates:
>    #            - Path=/pay/gateway/filter/**
>    #            - Path=/gateway/filter/** # 断言，为了配合PrefixPath测试过了，暂时注释掉/pay
>                - Path=/XYZ/abc/{segment}
>              filters:
>    #            - PrefixPath=/pay # http://localhost:9527/pay/gateway/filter # 被拆分为 PrefixPath + Path
>                - SetPath=/pau/gateway/{segment} #{segment}表示占位符
>    ```
>
>    浏览器访问地址：http://localhost:9527/XYZ/abc/filter
>
>    实际微服务地址：http://localhost:9527/pay/gateway/filter
>
>    重定向
>
>    ```yml
>              predicates:
>                - Path=/pay/gateway/filter/**
>    #            - Path=/gateway/filter/** # 断言，为了配合PrefixPath测试过了，暂时注释掉/pay
>    #            - Path=/XYZ/abc/{segment}
>              filters:
>    #            - PrefixPath=/pay # http://localhost:9527/pay/gateway/filter # 被拆分为 PrefixPath + Path
>    #            - SetPath=/pay/gateway/{segment} #{segment}表示占位符
>                - RedirectTo=302,https://www.baidu.com/ # 访问http://localhost:9527/pay/gateway/filter 跳转到https://www.baidu.com/
>    ```
>
> 5. 其他
>
>    官网：https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories/default-filters.html

------

> Gateway自定义过滤器
>
> ```java
> package com.cloud.MyGateway;
> 
> import lombok.Getter;
> import lombok.Setter;
> import org.springframework.cloud.gateway.filter.GatewayFilter;
> import org.springframework.cloud.gateway.filter.GatewayFilterChain;
> import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
> import org.springframework.http.HttpStatus;
> import org.springframework.http.server.reactive.ServerHttpRequest;
> import org.springframework.stereotype.Component;
> import org.springframework.web.server.ServerWebExchange;
> import reactor.core.publisher.Mono;
> 
> import java.util.Arrays;
> import java.util.List;
> 
> @Component
> public class MyGatewayFilterFactory extends AbstractGatewayFilterFactory<MyGatewayFilterFactory.Config> {
> 
>     public MyGatewayFilterFactory() {
>         super(Config.class);
>     }
> 
>     @Override
>     public GatewayFilter apply(Config config) {
>         return new GatewayFilter() {
>             @Override
>             public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
>                 ServerHttpRequest request = exchange.getRequest();
>                 System.out.println("进入了自定义网关过滤器MyGatewayFilterFactory，status" + config.getStatus());
>                 if (request.getQueryParams().containsKey("disney")) {
>                     return chain.filter(exchange);
>                 } else {
>                     exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
>                     return exchange.getResponse().setComplete();
>                 }
>             }
>         };
>     }
> 
>     @Override
>     public List<String> shortcutFieldOrder() {
>         return Arrays.asList("status");
>     }
> 
>     @Setter
>     @Getter
>     public static class Config {
>         private String status;
>     }
> }
> ```
>
> yml
>
> ```yml
>           filters:
>             #            - PrefixPath=/pay # http://localhost:9527/pay/gateway/filter # 被拆分为 PrefixPath + Path
>             #            - SetPath=/pay/gateway/{segment} #{segment}表示占位符
>             #            - RedirectTo=302,https://www.baidu.com/ # 访问http://localhost:9527/pay/gateway/filter 跳转到https://www.baidu.com/
> 
>             - My=disney
> ```
>
> 访问：http://localhost:9527/pay/gateway/filter
>
> 访问：http://localhost:9527/pay/gateway/filter?disney=java



# 12、SpringCloud Alibaba入门简介

## 12.1、是什么

官网：https://spring-cloud-alibaba-group.github.io/github-pages/2021/en-us/index.html

Github：https://github.com/alibaba/spring-cloud-alibaba

中文文档：https://github.com/alibaba/spring-cloud-alibaba/blob/2023.x/README-zh.md

组件：

- Sentinel：把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个未付保护服务的稳定性。
- Nacos：一个更便于构建云原生应用的动态服务发现、配置管理和服务管理平台。
- RocketMQ：一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高可靠的信息发布与订阅服务。
- Seata：Alibaba开源产品，一个易于使用的高性能微服务分布式事务解决方案。
- Alibaba Cloud OSS：阿里云对象存储服务（Object Storage Service，简称OSS），是阿里云同的海量、安全、低成本、高可靠的云存储服务。可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。
- Alibaba Cloud SchedulerX：阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于Cron表达式）任务调度服务。
- Alibaba Cloud SMS：覆盖全球的短信服务，友好、搞笑、只能和互联化通讯能力，帮助企业迅速搭建客户触发通道。



## 12.2、能干嘛？

- **服务限流降级**：默认支持 WebServlet、WebFlux、OpenFeign、RestTemplate、Spring Cloud Gateway、Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
- **服务注册与发现**：适配 Spring Cloud 服务注册与发现标准，默认集成对应 Spring Cloud 版本所支持的负载均衡组件的适配。
- **分布式配置管理**：支持分布式系统中的外部化配置，配置更改时自动刷新。
- **消息驱动能力**：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
- **分布式事务**：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。
- **阿里云对象存储**：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
- **分布式任务调度**：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
- **阿里云短信服务**：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。



## 12.3、官方手册

官网：https://sca.aliyun.com/docs/2023/user-guide/sentinel/quick-start/?spm=5176.29160081.0.0.74801a153xByvU

# 13、Nacos服务注册和配置中心

## 13.1、是什么

官网：https://nacos.io/

一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

Nacos就是注册中心 + 配置中心的组合

Nacos = Eureka + Config + Bus

Nacos = Spring Cloud Consul



## 13.2、能干嘛

替代Eureka / Consul做服务注册中心

替代（Config + Bus）/ Consul 做服务配置中心和满足动态刷新广播通知



## 13.3、安装

官网：https://nacos.io/download/nacos-server/

启动

```shell
startup.cmd -m standalone
```



## 13.4、各种注册中心比较

| 服务注册与返现框架 | CAP模型 | 控制台管理 | 社区活跃度        |
| ------------------ | ------- | ---------- | ----------------- |
| Eureka             | AP      | 支持       | 低（2.x版本闭源） |
| Zookeeper          | CP      | 不支持     | 中                |
| Consul             | CP      | 支持       | 高                |
| Nacos              | AP      | 支持       | 高                |



## 13.5、基于Nacos的服务提供者

新建Module：cloudalibaba-provider-payment9001

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloudalibaba-provider-payment9001</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>com.cloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

```yml
server:
  port: 9001
spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 配置Nacos地址
```

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Main9001 {
    public static void main(String[] args) {
        SpringApplication.run(Main9001.class,args);
    }
}
```

```java
package com.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PayAlibabaController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/pay/nacos/{id}")
    public String getPayInfo(@PathVariable("id") Integer id) {
        return "nacos registry,ServerPort：" + serverPort + "\t" + "Id：" + id;
    }

}
```

启动服务

![image-20240618164539799](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618164539799.png)

访问：http://localhost:9001/pay/nacos/1



## 13.6、基于Nacos的服务消费者

新建Module：cloudalibaba-consumer-nacos-order83

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloudalibaba-consumer-nacos-order83</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
        </dependency>
        <dependency>
            <groupId>com.cloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

```yml
server:
  port: 83
spring:
  application:
    name: nacos-consumer-order
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 配置Nacos地址
service-url:
  nacos-user-service: http://nacos-payment-provider
```

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Main83 {
    public static void main(String[] args) {
        SpringApplication.run(Main83.class,args);
    }
}
```

```java
package com.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
package com.cloud.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderNaCosController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @GetMapping("/consumer/pay/nacos/{id}")
    public String consumer(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(serverURL + "/pay/nacos/" + id, String.class) + "\t"+"  我是OrderNacosController83调用者";
    }
}
```

访问：http://localhost:83/consumer/pay/nacos/1



## 13.7、负载均衡

新增一个服务9002,

访问：http://localhost:83/consumer/pay/nacos/1

9001/9002交替出现



## 13.8、Nacos Config服务配置中心

### 13.8.1、概述

通过Nacos和spring-cloud-starter-alibaba-nacos-config实现中心化全局配置的动态变更

### 13.8.2、官方文档

官网：https://nacos.io/docs/v2/what-is-nacos/

### 13.8.3、Nacos作为配置中心配置

新建Module：cloudalibaba-config-nacos-client3377

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloudalibaba-config-nacos-client3377</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>com.cloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

bootstrap.yml

```yml
spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # Nacos 服务注册中心地址
      config:
        server-addr: localhost:8848 # Nacos作为配置中心地址
        file-extension: yaml # 指定yaml格式的配置
```

application.yml

```yml
server:
  port: 3377
spring:
  profiles:
    active: dev # 表示开发环境、prod生产环境、test测试环境
```

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Main3377 {
    public static void main(String[] args) {
        SpringApplication.run(Main3377.class,args);
    }
}
```

```java
package com.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 在控制类加入@RefreshScope注解使当前类下的配置支持Nacos的动态刷新功能。
public class NacosConfigClientController {
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }
}
```

> nacos端配置文件DataId的命名规则是：
>
> ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
>
> 得出：nacos-config-client-dev.yaml

![image-20240618221516620](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618221516620.png)

访问：http://localhost:3377/config/info

Welcome to disney, nacos-config-client-dev.yaml, version=1

更改配置

![image-20240618222308642](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618222308642.png)

访问：http://localhost:3377/config/info

Welcome to disney, nacos-config-client-dev.yaml, version=2

历史版本

![image-20240618222536479](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618222536479.png)



### 13.8.4、Nacos数据模型之Namespace-Group-DataId

> 问题一
>
> 实际开发中，通常一个系统会准备
>
> dev开发环境、test测试环境、prod生产环境
>
> 如何保证指定环境启动时服务能正确读取到Nacos上对应环境的配置环境呢？
>
> ```yml
> server:
>   port: 3377
> spring:
>   profiles:
>     active: dev # 表示开发环境、prod生产环境、test测试环境
> ```

> 问题二
>
> 一个大型分布式微服务系统会有很多微服务子项目，每个微服务项目又都会有相应的开发环境、测试环境、预发环境、正式环境等
>
> 那么怎么对这些微服务配置进行分组和命名空间管理呢？

官网：https://nacos.io/docs/v2/architecture/

![image-20240618224446851](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618224446851.png)

是什么：类似Java里面的package名和类名，最外层的Namespace是可以用于区分部署环境的，Group和DataID逻辑上区分两个目标对象

默认值：默认情况：Namespace=public，Group=DEFAULT_GROUP；Nacos默认的命名空间是public，Namespace主要用来实现隔离。比方说我们现在有三个环境：开发、测试、生产环境，我们就可以创建三个Namespace，不同的Namespace之间是隔离的。Group默认是DEFAULT_GROUP，Group可以把不同的微服务划分到同一个分组里面去

Service就是微服务：一个Service可以包含一个或者多个Cluster（集群），Nacos默认Cluster是DEFAULT，Cluster是对指定微服务的一个虚拟划分



新建一个分组

![image-20240618230607119](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618230607119.png)

bootstrap.yml

```yml
spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # Nacos 服务注册中心地址
      config:
        server-addr: localhost:8848 # Nacos作为配置中心地址
        file-extension: yaml # 指定yaml格式的配置
        # 使用定义的分组
        group: PROD_GROUP
```

```yml
server:
  port: 3377
spring:
  profiles:
#    active: dev # 表示开发环境、prod生产环境、test测试环境
#    active: test
    active: prod
```

访问：http://localhost:3377/config/info

Welcome to disney, nacos-config-client-prod.yaml+ PROD_GROUP, version=2



新建命令空间

![image-20240618231041585](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618231041585.png)

当命令空间ID不填会自动生成一个ID

![image-20240618231145365](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618231145365.png)

![image-20240618231259108](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618231259108.png)

![image-20240618231604237](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240618231604237.png)

bootstrap.yml

```yml
spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # Nacos 服务注册中心地址
      config:
        server-addr: localhost:8848 # Nacos作为配置中心地址
        file-extension: yaml # 指定yaml格式的配置
        # 使用定义的分组
        group: PROD_GROUP
        # 使用自定义的命名空间
        namespace: Prod_Namespace

#        nacos端配置文件DataId的命名规则是：
#        ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
```

访问：http://localhost:3377/config/info

Welcome to disney, (Prod_Namespace + PROD_GROUP + nacos-config-client-prod.yaml), version=2

# 14、SpringCloud Alibaba Sentinel实现熔断与限流

## 14.1、是什么？

官网：https://sentinelguard.io/zh-cn/

Github：https://github.com/alibaba/Sentinel/wiki

轻量级的流量控制、熔断降级Java库

随着微服务的流行，服务和服务之间的稳定性变得越来越重要。Sentinel是面向分布式、多语言异构化服务架构的治理组件，只要以流量为切入点，从流量路由、流量控制、流量整形、熔断降级、系统自适应过载保护、热点流量防护等多个维度来帮助开发者保障微服务的稳定性。

Sentinel分为两个部分：

- 核心库（Java客户端）不依赖任何框架/库，能够运行所有Java运行时环境，同时对Dubbo / Spring Cloud等框架也有较好的支持。
- 控制台（Dashboard）基于Spring Boot开发，打包后可以直接运行，不需要额外的Tomcat等应用容器。





> 面试题
>
> 1. 服务雪崩
> 2. 服务降级
> 3. 服务熔断
> 4. 服务限流
> 5. 服务隔离
> 6. 服务超时
> 7. 讲讲什么是缓存穿透？击穿？雪崩？如何解决？



## 14.2、安装Sentinel

Github：https://github.com/alibaba/Sentinel/releases

![image-20240619165208263](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240619165208263.png)

账号：sentinel

密码：sentinel

![image-20240619165302088](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240619165302088.png)



## 14.3、微服务8401整合Sentinel

新建Module：cloudalibaba-sentinel-service8401

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.cloud</groupId>
        <artifactId>Cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>cloudalibaba-sentinel-service8401</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>com.cloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

```yml
server:
  port: 8401
spring:
  application:
    name: cloud-alibaba-sentinel-service8401
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    sentinel:
      transport:
        dashboard: localhost:8080 # 配置Sentinel Dashboard控制服务地址
        port: 8719 # 默认8719端口，假如被占用会自动从8719开始一次+1扫描，直至找到未被占用的端口
```

```java
package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Main8401 {
    public static void main(String[] args) {
        SpringApplication.run(Main8401.class,args);
    }
}
```

```java
package com.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "-----testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "-----testB";
    }
}
```



访问：http://localhost:8401/testA

访问：http://localhost:8401/testB

![image-20240619172316647](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240619172316647.png)



## 14.4、流控规则

### 14.4.1、基本介绍

概述

Sentinel能够对流量进行控制，主要是监控应用的QPS流量或者并发线程等指标，如果达到指定的阈值时，就会被流量进行控制，以避免服务被瞬时的高并发流量击垮，保证服务的高可靠性。



### 14.4.2、流控模式

![image-20240620115717896](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620115717896.png)

> 直接
>
> 默认的流控模式，当接口达到限流条件时，直接开启限流功能。
>
> ![image-20240620115931837](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620115931837.png)
>
> ![image-20240620120003101](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620120003101.png)



> 关联
>
> 当关联的资源达到阈值时，就限流自己
>
> 当与A关联的资源B达到阈值后，就限流A自己
>
> ![image-20240620120538752](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620120538752.png) 



> 链路
>
>  来自不同链路的请求对同一个目标访问时，实施针对性的不同限流措施，比如C请求访问过来就是限流，D请求过来访问就是OK
>
> ```java
> package com.cloud.service;
> 
> import com.alibaba.csp.sentinel.annotation.SentinelResource;
> import org.springframework.stereotype.Service;
> 
> @Service
> public class FlowLimitService {
> 
>     @SentinelResource(value = "common")
>     public void common() {
>         System.out.println("------FlowLimitService------");
>     }
> }
> ```
>
> ```java
> package com.cloud.controller;
> 
> import com.cloud.service.FlowLimitService;
> import jakarta.annotation.Resource;
> import lombok.extern.slf4j.Slf4j;
> import org.springframework.web.bind.annotation.GetMapping;
> import org.springframework.web.bind.annotation.RestController;
> 
> @Slf4j
> @RestController
> public class FlowLimitController {
> 
>     @GetMapping("/testA")
>     public String testA() {
>         return "-----testA";
>     }
> 
>     @GetMapping("/testB")
>     public String testB() {
>         return "-----testB";
>     }
> 
>     @Resource
>     private FlowLimitService flowLimitService;
>     @GetMapping("/testC")
>     public String testC() {
>         flowLimitService.common();
>         return "-----testC";
>     }
> 
>     @GetMapping("/testD")
>     public String testD() {
>         flowLimitService.common();
>         return "-----testD";
>     }
> }
> ```
>
> ```yml
> sentinel:
>   transport:
>     dashboard: localhost:8080 # 配置Sentinel Dashboard控制服务地址
>     port: 8719 # 默认8719端口，假如被占用会自动从8719开始一次+1扫描，直至找到未被占用的端口
>   web-context-unify: false # controller层的方法对service层调用不认为是同一个跟链路
> ```
>
> ![image-20240620132756386](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620132756386.png)
>
> ![image-20240620132833319](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620132833319.png)
>
> 访问：http://localhost:8401/testC
>
> 一秒内访问超过一次链路C会被拒绝访问，而D链路则正常



### 14.4.3、流控效果

1. 直接 - 快速失败（默认流控处理） - 直接失败，抛出异常 - Blocked by Sentinel（Flow limiting）
2. 预热WarmUp
3. 排队等待



## 14.5、熔断规则

除了流量控制以外，降低调用链路中的不稳定资源也是 Sentinel 的使命之一。由于调用关系的复杂性，如果调用链路中的某个资源出现了不稳定，最终会导致请求发生堆积。这个问题和 [Hystrix](https://github.com/Netflix/Hystrix/wiki#what-problem-does-hystrix-solve) 里面描述的问题是一样的。

Sentinel熔断降级会在调用链路中某个资源出现不稳定状态时（例如调用超时或异常比例升高），对这个资源的调用进行限制，让请求快速失败，避免影响到其他资源而导致级联错误。当资源被降级后，在接下来的降级时间窗口之内，对该资源的调用都自动熔断（默认行为是抛出DegardeException）。



### 14.5.1、慢调用比例

> 慢调用比例（SLOW_REQUEST_RATIO）：选择以慢调用比例作为阈值，需要设置允许的慢调用RT（即最大响应时间），请求的响应时间大于该值则统计为慢调用。当单位统计时长（statIntervalMs）内请求数据大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入勘测恢复状态（HALF-OPEN状态），若接下来的一个请求响应时间小于设置的慢调用RT则结束熔断，若大于设置的慢调用RT则会再次被熔断。

```java
@GetMapping("/testF")
public String testF() {
    try {
        TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("测试：新增熔断规则，慢调用比例");
    return "-----testF";
}
```

新增熔断规则

![image-20240620161400998](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620161400998.png) 

![image-20240620161448777](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620161448777.png)



### 14.5.2、异常比例

> 异常比例（ERROR_RATIO）：当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断会进入探测恢复状态（HALF-OPEN状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是[0.0,1.0]，表示0% ~ 100%

```java
@GetMapping("/testG")
public String testG() {
    System.out.println("测试：新增熔断规则，异常比例");
    int age = 10/0;
    return "-----testG";
}
```

![image-20240620162617954](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620162617954.png)



### 14.5.3、异常数

> 异常数（ERROR_COUNT）：当单位时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。

```java
@GetMapping("/testH")
public String testH() {
    System.out.println("测试：新增熔断规则，异常数");
    int age = 10 / 0;
    return "-----testH";
}
```

![image-20240620163836503](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620163836503.png)



## 14.6、@SentinelResource注解

SentinelResource是一个流量防卫防护组件注解，用于指定防护资源，对配置的资源进行流量控制、熔断降级等功能。



### 14.6.1、按照rest地址限流 + 默认限流返回

通过访问rest地址来限流，会返回Sentinel自带默认的限流处理信息

```java
package com.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimitController {

    @GetMapping(value = "/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试";
    }

}
```

![image-20240620172006929](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620172006929.png)



### 14.6.2、按照SentinelResource资源名称限流 + 自定义限流返回

```java
@GetMapping(value = "/rateLimit/byResource")
@SentinelResource(value = "byResourceSentinelResource", blockHandler = "handlerBlockHandler")
public String byResource() {
    return "按照资源名称SentinelResource限流测试";
}
public String handlerBlockHandler(BlockException blockException) {
    return "服务不可用触发了@SentinelResource";
}
```

![image-20240620211818398](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620211818398.png)





### 14.6.3、按照SentinelResource资源名称 + 自定义限流返回 + 服务降级处理

```java
@GetMapping("/rateLimit/doAction/{p1}")
@SentinelResource(value = "doActionSentinelResource", blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
public String doAction(@PathVariable("p1") Integer p1) {
    if (p1 == 0) {
        throw new RuntimeException("p1 is 0");
    }
    return "doAction";
}

public String doActionFallback(@PathVariable("p1") Integer p1, BlockException e) {
    log.error("sentinel配置自定义限流了：" + e);
    return "程序逻辑异常了" + "\t" + e.getMessage();
}

public String doActionFallback(@PathVariable("p1") Integer p1, Throwable e) {
    log.error("逻辑程序异常了：" + e);
    return "程序逻辑异常了" + "\t" + e.getMessage();
}
```

![image-20240620213814538](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620213814538.png)

blockHandler，主要针对sentinel配置后出现的违规情况处理

fallback，程序异常了JVM抛出的异常服务降级



## 14.7、热点规则

官网：https://github.com/alibaba/Sentinel/wiki/%E7%83%AD%E7%82%B9%E5%8F%82%E6%95%B0%E9%99%90%E6%B5%81

热点即经常访问的数据，很多时候我们希望统计或者限制某个热点数据中访问频次最高的TopN数据，并对其访问进行限流或其他操作。

```java
    @GetMapping(value = "/test/HotKey")
    @SentinelResource (value = "testHotKey",blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2
                             ) {
        return "------testHotKey------";
    }
    public String dealHandler_testHotKey(String p1, String p2,BlockException e) {
        return "----dealHandler_testHotKey";
    }
```

![image-20240620220019026](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620220019026.png)

限流模式支持QPS模式，固定写死了（这才叫热点）

@SentinelResource注解的方法参数索引，0代表第一个参数，1代表第二个参数，以此类推

单机阈值以及统计窗口时长表示在此窗口时间超过阈值就限流。

访问：http://localhost:8401/test/HotKey?p1=abc 会被限流

访问：http://localhost:8401/test/HotKey?p1=abc&p2=33 会被限流

访问：http://localhost:8401/test/HotKey?p2=33 不会被限流



另外参数 （常用VIP内部抢票）

![image-20240620221958575](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620221958575.png)

访问：http://localhost:8401/test/HotKey?p1=5

参数值为5时，就不会被限流



## 14.8、授权规则

官网：https://github.com/alibaba/Sentinel/wiki/%E9%BB%91%E7%99%BD%E5%90%8D%E5%8D%95%E6%8E%A7%E5%88%B6

在某些场景下，需要根据调用接口的来源判断是否允许这行本次请求。此时就可以使用Sentinel提供的授权规则来实现，Sentinel的授权规则能够根据请求的来源判断是否允许本次请求通过。

在Sentinel的授权规则中，提供了白名单与黑名单两种授权类型。白名单放行、黑名单禁止。



```java
package com.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmpowerController {

    @GetMapping(value = "/empower")
    public String requestSentinel4(){
        log.info("测试Sentinel授权规则empower");
        return "Sentinel授权规则";
    }
}
```

```java
package com.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        return request.getParameter("serverName");
    }
}
```

![image-20240620225237785](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620225237785.png)

访问：http://localhost:8401/empower?serverName=test

访问：http://localhost:8401/empower?serverName=test2

```tex
Blocked by Sentinel (flow limiting)
```

访问：http://localhost:8401/empower?serverName=test3

```tex
Sentinel授权规则
```



## 14.9、规则持久化

一旦重启微服务应用，Sentinel规则将消失

将限流配置规则持久化进Nacos保存，只要刷新8401某个rest地址，Sentinel控制台的流控规则就能看到，只要Nacos里面的配置不删除，针对8401上Sentinel上的流控规则持续有效。

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

```yml
server:
  port: 8401
spring:
  application:
    name: cloud-alibaba-sentinel-service8401
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    sentinel:
      transport:
        dashboard: localhost:8080 # 配置Sentinel Dashboard控制服务地址
        port: 8719 # 默认8719端口，假如被占用会自动从8719开始一次+1扫描，直至找到未被占用的端口
      web-context-unify: false # controller层的方法对service层调用不认为是同一个跟链路
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            data-id: ${spring.application.name}
            group-id: DEFAULT_GROUP
            data-type: json
            # FlowRule 流量控制规则
            # DegradeRule 熔断降级规则
            # AuthorityRule 访问控制规则
            # SystemRule 系统保护规则
            # ParamFlowRule 热点规则
            rule-type: flow # com.alibaba.cloud.sentinel.datasource.RuleType
```

![image-20240620232539199](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620232539199.png)

```json
[
    {
        "resource":"/rateLimit/byUrl",
        "limitApp":"default",
        "grade":1,
        "count":1,
        "strategy":0,
        "controlBehavior":0,
        "clusterMode":false
	}
]
```

```tex
resource：资源名称
limitApp：来源应用
grade：阈值类型（0表示线程数。1表示QPS）
count：单机阈值
strategy：流控模式（0表示直接，1表示关联，2表示链路）
controlBehavior：流控效果（0表示快速失败，1表示Warm Up，2表示排队等待）
clusterMode：是否集群
```

重启之后，刷一次（懒加载形式），持久化完成

![image-20240620232635099](K:\GitHub\notes\SpringCloud\SpringCloud.assets\image-20240620232635099.png)







## 14.10、OpenFeign和Sentinel集成实现fallback服务降级
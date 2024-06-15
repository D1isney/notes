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
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



### 6.6.5、思考
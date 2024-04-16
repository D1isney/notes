# RabbitMQ

# 1、消息队列

## 1.1、MQ的相关概念

### 1.1.1、什么是MQ

> MQ（Message Queue），从字面意思上看，本质是一个队列，FIFO先入先出，只不过队列中存放的内容是message而已，还是一种跨进程的通信机制，用于上下游传递消息。在互联网架构汇总，MQ是一种非常常见的上下游“逻辑解耦+物理解耦”的消息通信服务。使用了MQ之后，消息发送上游只需要依赖MQ，不用依赖其他服务。

### 1.1.2、为什么要用MQ

1. 流量消峰

   举个例子，如果订单系统最多能处理一万次订单，这个处理能力应付正常时段的下单时绰绰有余，正常时段我们下单一秒后就能返回结果。但是在高峰期，如果有两万次下单操作系统是处理不了的，只能限制订单超过一万后不允许用户下单。使用消息队列做缓冲，我们可以取消这个限制，把一秒内下的订单分散成一段时间来处理，这时有些用户可能在下单几十秒后才能收到下单成功的操作，但是必不能下单的体验要好。
   
2. 应用解耦

   以电商应用为例，应用中有订单系统、库存系统、物流系统、支付系统。用户创建订单后，如果耦合调用库存系统、物流系统、支付系统，任何一个子系统出现了故障，都会造成下单操作异常。当转变成基于消息队列的方式后，系统间调用的问题会减少很多，比如物流系统因为发生故障，需要几分钟来修复。在这几分钟的时间里，物流系统要处理的内存被缓存在消息队列中，用户的下单操作可以正常完成。当物流系统恢复后，继续处理订单信息即可，当中用户感受不到物流系统的故障，提升系统的可用性。

3. 异步处理

   有些服务间调用是异步的，例如A调用B，B需要花费很长时间执行，但是A需要知道B什么时候可以执行完，以前一般有两种方式，A过一段时间去调用B的查询api查询。或者A提供一个callback api，B执行完之后调用api通知A服务。这两种方式都不是很优雅，使用消息总线，可以方便解决这个问题，A调用B服务后，只需要监听B处理完成的消息，当B处理完成后，会发送一条消息个MQ，MQ会将此消息转发个A服务。这样A服务既不用循环调用B的查询api，也不用提供callback api。同样B服务也不用做这些操作。A服务还能及时的得到异步处理成功的消息。



### 1.1.3、MQ的分类

1. [Active MQ](https://activemq.apache.org/)

   - 优点：单机吞吐量万级，时效性ms级，可用性高，基于主从架构实现高可用性，消息可靠性较低的概率丢失数据
   - 缺点：官网社区现在对Active MQ 5.x **维护越来越少，高吞吐量场景较少使用。**

2. [Kafka](https://kafka.apache.org/documentation/streams/)

   大数据的杀手锏，谈到大数据内的消息传输，则绕不开Kafka，这款为**大数据而生**的消息中间件，以其**百万级TPS**的吞吐量名声大噪，迅速成为大数据领域的宠儿，在数据采集、传输、存储的过程中发挥着举足轻重的作用。目前已经被LinkedIn，Uber，Twitter，Netflix等大公司所采纳。

   - 优点：性能卓越，单机写入TPS约在百万条/秒，最大的优点，就是**吞吐量高**。时效性ms级可用性非常高，kafka是分布式的，一个数据多个副本，少数机器宕机，不会丢失数据，不会导致不可用，消费者采用Pull方式获取消息，消息有序，通过控制能够保证所有信息被消费且仅被消费一次；有优秀的第三方Kafka Web管理界面Kafka - Manager；在日志领域比较成熟，被多家公司和开源项目使用；功能支持：功能较为简单，主要支持简单的MQ功能，在大数据领域的实时计算以及**日志采集**被大规模使用。
   - 缺点：Kafka单机超过64个队列/分区，Load会发生明显的飙高现象，队列越多，load越高，发送消息响应时间越长，使用短轮询方式，实时性取决于轮询间隔时间，消费失败不支持重试；支持消息顺序，但是一台代理宕机后，就会产生信息乱序，**社区更新较慢**。

3. [Rocket MQ](https://rocketmq.apache.org/)

   Rocket MQ出自阿里巴巴的开源产品，用Java语言实现，在设计时参考了Kafka，并做出了自己的一些改进。被阿里巴巴广泛应用在订单、交易、流计算、消息推送、日志流式处理、binlog分发等场景。

   - 优点：**单机吞吐量十万级**，可用性非常高，分布式架构，**消息可以做到0丢失**，MQ功能较为完善，还是分布式的，扩展性好，**支持10亿级别的消息堆积**，不会因为堆积导致性能下降，源码是java我们可以自己阅读源码，定制自己公司的MQ。
   - 缺点：**支持的客户端语言不多**，目前是Java及C++，其中C++不成熟；社区活跃度一般，没有MQ核心中去实现JMS等接口，有些系统要迁移需要修改大量代码。

4. [Rabbit MQ](https://www.rabbitmq.com/)

   2007年发布，是一个在AMQP（高级消息队列协议）基础上完成的，可复用在企业消息系统，是**当前最主流的消息中间件之一**。

   - 优点：由于erlang语言的**高并发特性**，性能较好；**吞吐量万级**，MQ功能比较完备、健壮、稳定、易用、跨平台、**支持多种语言**如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等，支持AJAX文档齐全；开源提供的管理界面非常棒，用起来很好，**社区活跃度高**；更新频率相当高
   - 缺点：商业版需要收费，学习成本较高。



### 1.1.4、MQ的选择

1. Kafka

   Kafka主要特点是基于Pull的模式来处理消息消费，追求高吞吐量，一开始的目的就是用于日志收集和传输，适合产生**大量数据**的互联网服务的数据收集业务。**大型公司**建议可以选用，如果有**日志采集**功能，肯定是首选Kafka了

2. Rocket MQ

   天生为**金融互联网领域**而生，对于可靠性要求很高的场景，尤其是电商里面的订单扣款，以及业务削峰，在大量交易涌入时，后端可能无法及时处理的情况。Rocket MQ在稳定性上可能更值得信赖，这些业务场景里阿里双十一已经经历了多次考验，如果业务上有并发场景，建议可以选择Rocket MQ。

3. Rabbit MQ

   结合erlang语言本身的并发优势，性能好**时效性微秒级，社区活跃度也比较高**，管理界面用起来十分方便，如果你的**数据量没有那么大**，中小型公司优先选择功能比较完备的Rabbit MQ。



## 1.2、RabbitMQ

### 1.2.1、RabbitMQ的概念

RabbitMQ是一个消息中间：它接受并转发消息。你可以把它当做一个快递站点，当你要发送一个包裹时，你把你的包裹放到快递站，快递员最终会把你的快递送到收件人那里，按照这种逻辑RabbitMQ是一个快递站，一个快递员帮你传递快件。RabbitMQ与快递站的主要区别在于，它不处理快件而是接受，存储和转发消息数据。



### 1.2.2、四大核心概念

- 生产者

  产生数据发送消息的程序是生产者。

- 交换机

  交换机是RabbitMQ非常重要的一个部件，一方面它接受来自生产者的消息，另一方面它将消息推送到队列中。交换机必须确切知道如何处理它接收到的消息，是将这些消息推送到特定队列还是推送到多个队列，亦或者是把消息丢弃，这个得由交换机类型决定。

- 队列

  队列是RabbitMQ内部使用的一种数据结构，尽管消息流经RabbitMQ和应用程序，但它们只存储在队列中。队列仅受主机的内存和磁盘限制的约束，本质上是一个大的消息缓冲区。许多生产者可以将消息发送到一个队列，许多消费者可以尝试从一个队列接收数据。这就是我们使用队列的方式。

- 消费者

  消费与接收具有相似的含义。消费者大多时候是一个等待接收消息的程序。请注意生产者消费者和消息中间件很多时候并不是在同一机器上。用一个应用程序既可以是生产者又是可以是消费者。



### 1.2.3、RabbitMQ核心部分

![image-20240409223234563](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240409223234563.png)

![image-20240409223340870](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240409223340870.png)



### 1.2.4、各个名词介绍

![image-20240409231315773](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240409231315773.png)

**Broker**：接收和分发消息的应用，RabbitMQ Server就是Message Broker

**Virtual host**：出于多个租户和安全因素设计的，把AMQP的基本组件划分到一个虚拟的分组中，类似于网络中的namespace概念。当多个不同的用户使用同一个RabbitMQ Server 提供的服务时，可以划分出多个vhost，每个用户在自己的vhost创建 exchange / queue 等

**Connection**：publisher / consumer 和 broker 之间的TCP连接

**Channel**：如果每一次访问RabbitMQ都建立一个Connection，在消息量大的时候建立TCPConnection的开销将是巨大的，效率也较低。Channel是在Connection内部建立的逻辑连接，如果应用程序支持多线程，通常每个Thread创建单独的Channel进行通讯，AMQP method包含了Channel id帮助客户端和message broker识别Channel，所以Channel之间是完全隔离的。Channel作为轻量级的**Connection极大减少了操作系统建立TCPConnection的开销**

**ExChange**：message到达Broker的都第一站，根据分发规则，匹配查询表中的routing key，分发消息到queue中去，常用的类型有：direct（point - to - point），topic（publish - subscribe） and fanout（multicast）



### 1.2.5、安装

RabbitMQ版本和Erlang版本兼容性关系：[官网](https://www.rabbitmq.com/which-erlang.html)

安装包中说明，下载对应的安装包	

el6：CentOS 6.x的下载

el7：CentOS 7.x的下载

el8：CentOS 8.x的下载

![image-20240410205100208](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240410205100208.png)



检查时候有GCC环境

```shell
gcc --version
```



创建文件夹、进入文件夹

```shell
cd /
mkdir myerlang
cd myerlang
```



添加 Erlang RPM 仓库：RabbitMQ 依赖于 Erlang/OTP，因此首先需要添加 Erlang RPM 仓库。

```shell
sudo yum install epel-release
```



安装 Erlang/OTP：

```shell
sudo yum install erlang
```



查看安装情况

```shell
erl -version
```



添加 RabbitMQ RPM 仓库：下载并安装 RabbitMQ 的 RPM 仓库配置文件。

```shell
sudo curl -s https://packagecloud.io/install/repositories/rabbitmq/rabbitmq-server/script.rpm.sh | sudo bash
```



安装 RabbitMQ：

```shell
sudo yum install rabbitmq-server
```



启动 RabbitMQ 服务：

```shell
sudo systemctl start rabbitmq-server
```



设置 RabbitMQ 开机自启：

```shell
sudo systemctl enable rabbitmq-server
```



检查 RabbitMQ 服务状态：

```shell
sudo systemctl status rabbitmq-server
```



开启web插件

```shell
rabbitmq-plugins enable rabbitmq_management
```



启动命令（关闭防火墙或者开放5672 15672端口）

```shell
#启动：
sudo systemctl start rabbitmq-server
 
#停止：
sudo systemctl stop rabbitmq-server
 
#重启：
sudo systemctl restart rabbitmq-server

#查看服务状态
sudo systemctl status rabbitmq-server

#查看RabbitMQ版本
sudo rabbitmqctl status | grep "RabbitMQ version"

#开机自动启动
sudo systemctl enable rabbitmq-server

#禁止开机自启
sudo systemctl disable rabbitmq-server
```



访问127.0.0.1:15672（5672端口为rabbitmq暴露端口，15672为web端端口）

```shell
#查看所有用户
rabbitmqctl list_users
 
#添加一个用户
rabbitmqctl add_user admin 123456
 
#配置权限
rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"
 
#查看用户权限
rabbitmqctl list_user_permissions admin
 
#设置用户角色
rabbitmqctl set_user_tags admin administrator
 
#删除用户（安全起见，删除默认用户）
rabbitmqctl delete_user guest
```



# 2、Hello World



## 2.1、依赖

```xml
    <dependencies>        
		<!--    RabbitMQ依赖客户端-->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>5.8.0</version>
        </dependency>
        <!--        操作文档流的一个依赖-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```



## 2.2、生产者代码

```java
package com.demo01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * 生产者：发消息
 */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP，连接队列
        factory.setHost("192.168.129.135");
        // 用户名
        factory.setUsername("admin");
        // 密码
        factory.setPassword("123456");

        // 创建连接
        Connection connection = factory.newConnection();

        // 获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * channel.queueDeclare(队列名，是否需要持久化，是否排它，是否自动删除，队列参数);
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费 false：只能一个消费者消费
         * 4.是否自动删除 最后一个消费者断开连接以后，该队列依据是否自动删除，true自动删除 false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发消息
        String message = "Hello World";

        /**
         * 信道发布消息
         * channel.basicPublish();
         * 1.发送到哪个交换机
         * 2.路由的Key值是哪个 本次的队列的名称
         * 3，其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("=============》消息发送完毕");
    }
}
```

![image-20240411132318335](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411132318335.png)



## 2.3、消费者代码

```java
package com.demo01;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者 接收信息的
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    //  接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //  创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP，连接队列
        factory.setHost("192.168.129.135");
        // 用户名
        factory.setUsername("admin");
        // 密码
        factory.setPassword("123456");

        //  创建新的连接
        Connection connection = factory.newConnection();
        //  创建信道
        Channel channel = connection.createChannel();

        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
//            String message = new String();
            System.out.println(message);
            System.out.println(new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费中断！");
        };

        /**
         * 消费者接收消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
         * 3.消费未成功的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
```

![image-20240411132351857](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411132351857.png)



# 3、Work Queues

> 工作队列（又称任务队列）主要思想是避免立即执行资源密集型任务，而不得不等待它完成。相反我们安排任务在之后执行。我们把任务封装为消息并将其发送到队列。在后台运行的工作进程将淡出任务并最终执行作业。当有多个工作线程时，这些工作线程将一起处理这些任务。



## 3.1、轮询发布消息

在这个案例中我们会启动两个工作线程，一个消息发送线程，我们来看看他们两个是如何工作的。

![image-20240411133750661](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411133750661.png)

### 3.1.1、抽取工作类

```java
/**
 * 创建连接池工具类
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        // 创还能连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.129.135");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
```

### 3.1.2、工作线程代码

```java
package com.demo02;


import com.demo02.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程1
 * 相当于消费者
 */
public class Worker01 {
    // 队列的名称
    public static final String QUEUE_NAME = "hello";

    //  接收信息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 拿到信道
        Channel channel = RabbitMQUtils.getChannel();

        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑！");
        };
        //  接收消息
        /**
         * 消费者接收消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
         * 3.消费未成功回调
         * 4.消费者取消消费的回调
         */
        System.out.println("Consumer1等待接收消息 《========= ");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
```

```java
package com.demo02;


import com.demo02.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程
 * 相当于消费者2
 */
public class Worker02 {
    // 队列的名称
    public static final String QUEUE_NAME = "hello";

    //  接收信息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 拿到信道
        Channel channel = RabbitMQUtils.getChannel();

        // 声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑！");
        };
        //  接收消息
        /**
         * 消费者接收消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
         * 3.消费未成功回调
         * 4.消费者取消消费的回调
         */
        System.out.println("Consumer2等待接收消息 《========= ");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
```



3.1.3、工作队列代码

```java
package com.demo02;

import com.demo02.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * 可以发送大量的消息
 */
public class Task01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    //  发送大量消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 队列的声明
        /**
         * 生成一个队列
         * channel.queueDeclare(队列名，是否需要持久化，是否排它，是否自动删除，队列参数);
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费 false：只能一个消费者消费
         * 4.是否自动删除 最后一个消费者断开连接以后，该队列依据是否自动删除，true自动删除 false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 从控制台当中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 信道发布消息
             * channel.basicPublish();
             * 1.发送到哪个交换机
             * 2.路由的Key值是哪个 本次的队列的名称
             * 3，其他参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }
}
```





## 3.2、消息应答

### 3.2.1、概念

消费者完成一个任务可能需要一段时间，如果其中一个消费者处理一个长的任务并仅只有完成了部分突然它挂掉了，会发生什么情况。RabbitMQ一旦向消费者传递了一条消息，便立刻将该消息标记为删除。在这种情况下，突然有个消费者挂掉了，我们将丢失正在处理的消息。以及后续发送给该消费者的消息，因为它无法接收到。

为了保证消息在发送过程中不丢失，RabbitMQ引入了消息应答禁止，消息应答就是：**消费者在接收到消息并且处理该消息之后，告诉RabbitMQ它已经处理了，RabbitMQ可以把该消息删除了。**



### 3.2.2、自动应答

消息发送后立即被认为已经传功成功，这种模式需要再**高吞吐量和数据传输安全性方面做权衡**，因为这种模式如果消息在接收到之前，消费者那边出现了连接或者channel（信道）关闭，那么消息就丢失了，当然另一方面这种模式消费者那边可以传递过载的信息，**没有对传递的消息数量进行限制**，当然这样有可能使得消费者这边由于接收太多还来不及处理的消息，导致这些消息的积压，最终使得内存消耗尽，最终这些消费者线程被操作系统杀死，**所以这种模式仅适用在消费者可以高效并以某种速率能够处理这些消息的情况下使用**。



### 3.2.3、消息应答的方法

1. Channel.basicACK(用于确认肯定)
   - RabbitMQ已经知道该消息并且成功的处理消息，可以将其他丢弃了 
2. Channel.basicNack(用于否认确定)
3. Channel.basicReject（用于否定确认）
   - 与Channel.basicNack相比少一个参数
   - 不处理该消息了直接拒绝，可以将其丢弃了



### 3.2.4、Multiple的解释

手动应答的好处是可以批量应答并且减少网络拥堵

```java
channel.basicACK(deliveryTag,true);
// true 就是 multiple
```

multiple的true和false代表不同意思

- true代表批量应答channel上未应答的信息

  比如说channel上有传达tag的消息 5,6,7,8 当前tag是8那么此时5-8的这些还未应答的消息都会被确认收到消息应答

- false同上面相比

  只会应答tag=8的消息，5,6,7这三个消息依然不会被确认收到消息应答



### 3.2.5、消息自动重新入队

如果消费者由于某些原因市区连接（其通道已关闭，连接已关闭或TCP连接丢失），导致消息未发送ACK确认，RabbitMQ将了解到消息未完全处理，并将对其重新排队。如果此时其他消费者可以处理，它将很快将其他重新分发给另一个消费者。这样，即使某个消费者偶尔死亡，也可以确保不会丢失任何消息。



### 3.2.6、消息手动应答代码

默认消息采用的是自动应答，所以我们要想实现消息消费过程中不丢失，需要把自动应答改成手动应答，消费这在上面代码的基础上增加下面画红色部分代码。

![image-20240411204900658](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411204900658.png)



消费生产者

```java
package com.demo03;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失的，放回队列中重新消费
 */
public class Task02 {
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个队列
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        //  从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
```

消息消费者

```java
package com.demo03;

import com.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息手动应答
 */
public class Worker03 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        System.out.println("C1等待接收消息，处理时间较短=====");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            SleepUtils.sleep(1);
            System.out.println("接收到消息：" + message);
            // 1.消息标记tag
            // 2.false代表只应答接收到的那个消息，true为应答所有消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        //  手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, callback, consumerTag -> {
            System.out.println(consumerTag + "：消费者取消消费接口逻辑");
        });
    }
}
```

```java
package com.demo03;

import com.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息手动应答
 */
public class Worker04 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        System.out.println("C2等待接收消息，处理时间较长======================");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            SleepUtils.sleep(30);
            System.out.println("接收到消息：" + message);
            // 1.消息标记tag
            // 2.false代表只应答接收到的那个消息，true为应答所有消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        //  手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, callback, consumerTag -> {
            System.out.println(consumerTag + "：消费者取消消费接口逻辑");
        });
    }
}
```

线程沉睡工具类

```java
package com.utils;

/**
 * 线程沉睡工具类
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
```



### 3.2.7、手动应答效果

启动三个程序

![image-20240411215930195](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411215930195.png)

发出9个消息

![image-20240411215951736](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411215951736.png)

消费者一

![image-20240411220135228](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411220135228.png)

消费者二

![image-20240411220148172](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411220148172.png)

等待三十秒，消费者二消费第一个消息

![image-20240411220159927](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411220159927.png)

结束消费者二，消费者一会替消费者二未消费的给消费了

![image-20240411220227012](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411220227012.png)



## 3.3、RabbitMQ持久化

### 3.3.1、概念

刚刚已经看到了如何处理任务不丢失的情况，但是如何保障当RabbitMQ服务停掉以后消费信息生产者发送过来的消息不丢失。默认情况下RabbitMQ退出或者由于某种原因崩溃时，它忽视队列和消息，除非告知它不要这样做。确保消息不会丢失需要做两件事：**需要将队列和消息都标记为持久化**。



### 3.3.2、队列如何实现持久化

之前创建的队列都是非持久化的，RabbitMQ如果重启的话，该队列就会被删掉，如果要队列实现持久化，需要在声明队列的时候把Durable参数设置为持久化。

```java
//	让消息队列持久化
boolean durable = true;
channel.queueDeclare(ACK_QUEUE_NAME,durable,false,false,null);
```

但是需要注意的就是如果之前声明的队列不是持久化的，需要把原先队列先删除，或者重新创建一个持久化的队列，不然会出现错误。

![image-20240411222835947](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411222835947.png)



**以下为控制台中非持久化与持久化队列的UI显示区**

![image-20240411222446531](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411222446531.png)

![image-20240411222933817](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240411222933817.png)

这个时候即使重启了RabbitMQ，队列也依然存在



### 3.3.3、消息实现持久化

要想让消息实现持久化需要在消息生产者修改代码。MessageProperties.PERSISTENT_TEXT_PLAIN添加这个属性

```java
channel.basicPublish("", DURABLE_QUEUE_NAME, null, message.getBytes("UTF-8"));
// 消息持久化
// 要求保存到磁盘上
channel.basicPublish("",DURABLE_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
```

将消息标记为持久化并不能完全保证不会丢失消息。尽管它告诉RabbitMQ将消息保存到磁盘，但是这里依然存在当消息刚准备存储在磁盘的时候，但是还没有存储完，消息还在缓存的一个间隔点。此时并没有真正写入磁盘。持久性保证并不强，但是对于我们的简单队列而言，这已经绰绰有余了。如果需要更强有力的持久化策略，参考发布确认。



### 3.3.4、不公平分发

最开始，RabbitMQ分发消息采用轮询的方式来分发消息，但是在某种情景下这种策略并不是很好，比方说有两个消费者在处理任务，其中有个消费者1处理任务的速度非常快，而另外一个消费者2速度缺很慢，这个时候还是采用轮询的方式分发的话，就会使得处理速度快的消费者有很大一部分时间处于空闲状态，而处理慢的那个消费者一直在干活，这种分配方式在这种情况下其实就不太好，然是RabbitMQ并不知道这种情况，就会依然很公平的进行分发。

为了避免这种情况，可以设置参数channel.basicQos(1);

```java
 int prefetchCount = 1;
 channel.basicQos(prefetchCount);
```

​	

### 3.3.5、预取值

本身消息的发送就是异步发送的，所以在任何时候，channel上肯定不止只有一个消息，另外来自消费者的手动确认本质上也是异步的。因此这里就存在一个未确认的消息缓冲区，因此开发人员就能**限制此缓冲区的大小，以避免缓冲区里面啊无限制的未确认消息问题**。

这个时候就可以通过使用basic.qos方法设置“预取计数”值来完成的。**该值定义通道后上允许的未确认消息的最大数量**。一旦数量达到配置的数量，RabbitMQ将停止在通道上传递更多消息，除非至少有一个未处理的消息被确认，例如，假设在通道上有未确认的消息5,6,7,8，并且通道的预取计数设置为4，此时RabbitMQ将不会在该通道上再传递任何消息，除非最少有一个未应答的消息被ACK。比方说tag=6这个消息刚刚被确认ACK，RabbitMQ将会感知这个情况到并再发送一条消息。消息应答和Qos预取值对用户吞吐量有重大影响。

通常，增加预取将提高向消费者传递消息的速度。**虽然自动应答传递消息速率是最佳的，但是，在这种情况下，已传递但尚未处理的消息数量也会增加，从而增加了消费者的RAM消耗**（随机存取存储器） 应该小心使用具有无限预处理的自动确认模式或手动确认模式，消费者消费了大量的消息如果没有确认的话，会导致消费者连接节点的内存消耗变大，所以找到合适的预取值是一个反复试验的过程，不同的负载该取值也不同，100到300范围内的值通常可以提供最佳的吞吐量，并且不会给消费者带来太大的风向，预取值为1是最保守的。当然这将使得吞吐量变得很低，特别是消费者连接延迟很严重的情况下，特别是在消费者连接等在时间较长的环境中，对于大多数应用来说，稍微高一点的值将是最佳的。

![image-20240412104147026](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412104147026.png)

```java
// 消费者
// 设置不公平分发
int prefetchCount = 2;
channel.basicQos(prefetchCount);
```



# 4、发布确认

## 4.1、发布确认原理

生产者将信道设置成confirm模式，一旦信道进入confirm模式，**所有在该信道上面发布的消息都会将会被指派一个唯一的ID**（从1开始），一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者（包括消息的唯一ID），这就使得生产者知道消息已经确定到达目的的队列了，如果消息和队列是可持久化的，那么确认信息会在将信息写入磁盘之后发出，broker回传给生产者的确认消息中delivery-tag域包含了确认消息的序列号，此外broker也可以设置basic.ack的multiple域，表示到这个序列号之前的所有消息都已经得到了处理。

confirm模式最大的好吃在于它是异步的，一旦发布一条消息，生产者因公程序就可以在等信号返回确认的同时继续发送一条消息，当消息最终得到确认之后，生产者应用便可以通过回调方法来处理该确认消息，如果RabbitMQ因为自身内部错误导致信息丢失，就会发送一条nack消息，生产者应用程序同样可以在回调方法中处理该nack消息。

![image-20240412110956062](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412110956062.png)

![image-20240412111005600](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412111005600.png)





## 4.2、发布确认策略

### 4.2.1、开启发布确认的方法

发布确认默认是没有开启的，如果要开启需要调用方法confirmSelect，每当你要想使用发布确认，都需要在channel上调用该方法。

```java
Channel channel = connection.createChannel();
// 开启发布确认
channel.confirmSelect();
```



```java
//  批量发消息的个数
public static final int MESSAGE_COUNT = 1000;
```



### 4.2.2、单个确认发布

这是一种简单的确认方式，它是一种**同步确认发布**的方式，也就是发布一个消息之后只要它被确认发布，后续的消息才能继续发布，waitForConfirmsOrDie(long)这个方法只有在消息被确认的时候才返回，如果在指定时间范围内这个消息没有被确认那么它将抛出异常。

这种确认方式有一个最大的缺点就是：**发布速度特别慢**，因为如果没有确认发布的消息就会阻塞所有后续消息的发布，这种方式最多提供每秒不超过书白条发布消息的吞吐量。当然对于某些应用程序来说这可能已经足够了。

```java
//  单个确认
public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
    Channel channel = RabbitMQUtils.getChannel();

    // 队列的声明
    String queueName = UUID.randomUUID().toString();
    // 需要让Queue进行持久化
    boolean durable = true;
    channel.queueDeclare(queueName, durable, false, false, null);

    // 开启发布确认
    channel.confirmSelect();
    // 开始时间
    long begin = System.currentTimeMillis();

    // 批量发消息
    for (int i = 0; i < MESSAGE_COUNT; i++) {
        String message = i + "";
        channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
        // 当个消息就马上进行发布确认
        boolean flag = channel.waitForConfirms();
        if (flag) {
            System.out.println("单个消息=========>消息发布成功！");
        }
    }
    //  结束时间
    long end = System.currentTimeMillis();

    System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
}
```





### 4.2.3、批量确认发布

上面那种方式非常慢，与单个等待确认消息相比，县发布一批消息然后一起确认可以极大地提高吞吐量，当然这种方式的缺点就是：当发生故障导致发布出现问题时，不知道是哪个消息出现问题了，我们必须将整个批量处理保存在内存中，以记录重要的消息而后重新发布消息。当然这种方案仍然是同步的，也一样阻塞消息发布。

```java
//  批量发布
public static void publishMessageBatch() throws InterruptedException, IOException, TimeoutException {
    Channel channel = RabbitMQUtils.getChannel();

    // 队列的声明
    String queueName = UUID.randomUUID().toString();
    // 需要让Queue进行持久化
    boolean durable = true;
    channel.queueDeclare(queueName, durable, false, false, null);

    // 开启发布确认
    channel.confirmSelect();
    // 开始时间
    long begin = System.currentTimeMillis();

    //  批量确认消息大小
    int batchSize = 100;

    // 批量发消息 批量发布确认
    for (int i = 0; i < MESSAGE_COUNT; i++) {
        String message = i + "";
        channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
        // 判断达到100条消息的时候，批量确认一次
        if (i % batchSize == 0) {
            //  发布确认
            channel.waitForConfirms();
        }
    }
    //  结束时间
    long end = System.currentTimeMillis();

    System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
}
```



### 4.2.4、异步确认发布

异步确认虽然编程逻辑比上两个复杂，但是性价比最高，无论是可靠性还是效率性都没得说，它是利用回调函数来达到消息可靠性传递的，这个中间件也是通过函数回调来保证是否投递成功。

![image-20240412124622981](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412124622981.png)

```java
// 异步确认发布
public static void publishMessageAsync() throws Exception {
    Channel channel = RabbitMQUtils.getChannel();

    // 队列的声明
    String queueName = UUID.randomUUID().toString();
    // 需要让Queue进行持久化
    boolean durable = true;
    channel.queueDeclare(queueName, durable, false, false, null);
    // 开启发布确认
    channel.confirmSelect();
    // 开始时间
    long begin = System.currentTimeMillis();


    //  1、消息的标记
    //  2、是否批量确认
    //  消息成功回调函数
    ConfirmCallback ackCallback = (deliverTag, multiple) -> {
        System.out.println("确认的消息：" + deliverTag);
    };
    //  消息失败回调函数
    ConfirmCallback nackCallback = (deliverTag, multiple) -> {
        System.out.println("未确认的消息：" + deliverTag);
    };
    //  准备消息的监听器，监听那些消息成功了，哪些消息失败了
    //  单参：只监听成功的
    //  两参：监听成功与失败的
    channel.addConfirmListener(ackCallback, nackCallback);   // 异步通知

    //  批量发布消息
    for (int i = 0; i < MESSAGE_COUNT; i++) {
        String message = "消息：" + i;
        channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
    }
    //  结束时间
    long end = System.currentTimeMillis();

    System.out.println("发布" + MESSAGE_COUNT + "个异步批量确认消息，耗时" + (end - begin) + "ms");
}
```



### 4.2.5、如何处理异步未确认消息

最好的解决的解决方案就是把未确认的消息放到一个基于内存的能被发布线程访问的队列，比如说ConcurrentLinkedQueue这个队列在confirm callbacks与发布线程之间进行消息的传递。

```java
/*
    线程安全 有序的一个哈希表，适用于高并发的情况下
    1、轻松的将序号与消息进行关联
    2、轻松批量删除条目 只要给到序号
    3、支持高并发（多线程）
 */
ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();
```

```java
ConfirmCallback ackCallback = (deliverTag, multiple) -> {
    // 如果是批量的
    if(multiple){
        // 删除已经确认的消息 剩下的就是未确认的消息
        ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliverTag);
        confirmed.clear();
    }else{
        //  如果不是批量
        outstandingConfirms.remove(deliverTag);
    }
    System.out.println("确认的消息：" + deliverTag);
};
```

```java
//  消息失败回调函数
ConfirmCallback nackCallback = (deliverTag, multiple) -> {
    // 打印未确认的消息
    String message = outstandingConfirms.get(deliverTag);
    System.out.println("未确认的消息是：" + message + "=== 未确认的消息tag是：" + deliverTag);
};
```



### 4.2.6、以上3种发布消息确认速度对比

- 单独发布消息

  同步等待确认，简单，但是吞吐量非常有限

- 批量发布消息

  批量同步等待确认，简单，合理的吞吐量，一旦出现问题但很难推断出那条消息除了问题

- 异步批量发布消息

  最佳性能和资源使用，在出现错误的情况下可以很好的控制，但是实现起来稍微难些



```java
package com.demo04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式
 * 1、单个确认模式
 * 2、批量确认
 * 3、异步批量确认
 * <p>
 * 使用的时间来比较哪种确认方式是最好的
 */

public class Task04 {

    //  批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

        /*
          1、单个确认
          2、批量确认
          3、异步批量确认
         */
//        发布1000个单独确认消息，耗时846ms
//        Task04.publishMessageIndividually();
//        发布1000个批量确认消息，耗时86ms
//        Task04.publishMessageBatch();

//        发布1000个异步批量确认消息，耗时50ms
        Task04.publishMessageAsync();


    }

    //  单个确认
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();

        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        // 需要让Queue进行持久化
        boolean durable = true;
        channel.queueDeclare(queueName, durable, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            // 当个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("单个消息=========>消息发布成功！");
            }
        }
        //  结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }


    //  批量发布
    public static void publishMessageBatch() throws InterruptedException, IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        // 需要让Queue进行持久化
        boolean durable = true;
        channel.queueDeclare(queueName, durable, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();

        //  批量确认消息大小
        int batchSize = 100;

        // 批量发消息 批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            // 判断达到100条消息的时候，批量确认一次
            if (i % batchSize == 0) {
                //  发布确认
                channel.waitForConfirms();
            }
        }
        //  结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }


    // 异步确认发布
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        // 需要让Queue进行持久化
        boolean durable = true;
        channel.queueDeclare(queueName, durable, false, false, null);
        // 开启发布确认
        channel.confirmSelect();

        /*
            线程安全 有序的一个哈希表，适用于高并发的情况下
            1、轻松的将序号与消息进行关联
            2、轻松批量删除条目 只要给到序号
            3、支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 开始时间
        long begin = System.currentTimeMillis();

        //  1、消息的标记
        //  2、是否批量确认
        //  消息成功回调函数
        ConfirmCallback ackCallback = (deliverTag, multiple) -> {
            // 如果是批量的
            if (multiple) {
                // 删除已经确认的消息 剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliverTag);
                confirmed.clear();
            } else {
                //  如果不是批量
                outstandingConfirms.remove(deliverTag);
            }
            System.out.println("确认的消息：" + deliverTag);
        };

        //  消息失败回调函数
        ConfirmCallback nackCallback = (deliverTag, multiple) -> {
            // 打印未确认的消息
            String message = outstandingConfirms.get(deliverTag);
            System.out.println("未确认的消息是：" + message + "=== 未确认的消息tag是：" + deliverTag);
        };
        
        //  准备消息的监听器，监听那些消息成功了，哪些消息失败了
        //  单参：只监听成功的
        //  两参：监听成功与失败的
        channel.addConfirmListener(ackCallback, nackCallback);   // 异步通知

        //  批量发布消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息：" + i;
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            /*
              1、此处记录下所有要发送的消息，消息的总和
              2、删除已经确认的消息，剩下的就是未确认的消息 ackCallback
              3、打印一下未确认的消息 nackCallback
             */
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        //  结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "个异步批量确认消息，耗时" + (end - begin) + "ms");
    }
}
```



# 5、交换机

我们创建了一个工作队列。假设的是工作队列背后，每一个任务都恰好交付给一个消费者（工作进程）。在这一部分，将做一些完全不同的事情，将消息传达给多个消费者。这种模式称为“**发布/订阅**“。

为了说明这种模式，我们将构建一个简单的日志系统。它将由两个程序组成：第一个程序将发出日志消息，第二个程序是消费者。其中我们会启动两个消费者，其中一个消费者接收到消息后把日志存储在磁盘，另一个消费者接收到消息后把消息打印到屏幕上，事实上第一个程序发出的日志消息将广播给所有消费者。

![image-20240412212521161](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412212521161.png)



## 5.1、Exchanges

### 5.1.1、Exchanges概念

RabbitMQ消息传递模型的核心思想是：**生产者生产的消息从不会直接发送到队列**。实际上，通常生产者甚至都不知道这些消息传递到哪些队列中。

相反，**生产者只能将消息发送到交换机（exchange）**，交换机工作的内容非常简单，一方面它接受来自生产者的消息，另一方面将他们推入队列中。交换机必须确切知道如何处理收到的消息。是应该把这些消息放到特定队列还是说把它们放到许多队列中，还是说应该丢弃他们，这就由交换机的类型来决定。



### 5.1.2、Exchanges的类型

总共有以下类型：

1. 直接（direct）
2. 主题（topic）
3. 标题（headers）
4. 扇出（fanout）



### 5.1.3、无名exchange

之前对交换机一无所知，但仍然能够将消息发送到队列。是因为使用的是默认交换机，通过空字符串（“”）进行标识。

```java
channel.basicPublish("","hello",null,message.getBytes());
```

第一个参数是交换机的名称。空字符串表示默认或无名交换机：消息能路由发送到队列中其实是由routingKey（bindingkey）绑定key指定的，如果它存在的话。



## 5.2、临时队列

之前使用具有特定名称的队列（hello和ack_queue）。队列的名称至关重要，我们需要指定我们的消费者去消费哪个队列的消息。

每当我们连接到Rabbit时，我们都需要一个全新的空队列，为此我们可以创还能一个具有随机名称的队列，或者能让服务器为我们选择一个随机队列名称那就更好了。其次**一旦我们断开了消费者的连接，队列将被自动删除**。

创建临时队列的方式如下：

```java
String queueName = channel.queueDeclare().getQueueu();
```





## 5.3、绑定

什么 bingding呢，binding其实是exchange和queue之间的桥梁，它告诉我们exchange和哪个队列进行了绑定关系。



## 5.4、Fanout

### 5.4.1、Fanout介绍

Fanout这种类型非常简单，正如从名称中猜到的那种，它是将接受到的所有消息**广播**到它知道的所有队列中。系统中默认有些exchange类型。

![image-20240412223928224](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412223928224.png)



### 5.4.2、Fanout实战

![image-20240412224543449](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412224543449.png)



ReceiveLogs01（消费者）

```java
package com.demo05;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    消息的接收 1
 */
public class ReceiveLogs01 {

    //  交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个叫交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");


        //  声明一个队列 临时的 队列名称是随机的 消费者断开与队列的连接的时候，队列就自动删除
        String queue = channel.queueDeclare().getQueue();

        /*
            绑定交换机与队列
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等到接受消息，把接收到的消息打印到屏幕上......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });
    }
}
```

ReceiveLogs02（消费者）

```java
package com.demo05;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    消息的接收 1
 */
public class ReceiveLogs02 {

    //  交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个叫交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");


        //  声明一个队列 临时的 队列名称是随机的 消费者断开与队列的连接的时候，队列就自动删除
        String queue = channel.queueDeclare().getQueue();

        /*
            绑定交换机与队列
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等到接受消息，把接收到的消息打印到屏幕上......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });
    }
}
```



发消息（交换机）

```java
package com.demo05;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

//  发消息 交换机
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
```

![image-20240412230856113](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412230856113.png)

![image-20240412230730680](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412230730680.png)

这样发送的消息就会一人一份

![image-20240412230949641](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412230949641.png)



## 5.5、Direct exchange

### 5.5.1、回顾

上面，我们构建了一个简单的日志记录系统。我们能够向许多接收者广播日志消息。在这里我们将向其中添加一些特别的功能。比方说我们只让某个消费者订阅发布的部分消息。例如我们只把严重错误信息定向存储到日志文件（以节省磁盘空间），同时仍然能够在控制台上打印所有日志消息。

什么是binding，绑定是交换机和队列之间的桥梁关系。也可以这么理解：**队列支队它绑定的交换机的消息感兴趣**。绑定用参数：routingKey来表示也可以城改参数为Binding Key，创建绑定：channel.queueBind(queueName,EXCHANGE_NAME,"routingKey");**绑定之后的意义由其交换机类型决定**。



### 5.5.2、Direct exchange介绍

日志系统将所有消息广播给所有消费者，对此我们想做出一些改变，例如我们希望将日志消息写入磁盘的程序仅接受严重错误（error），而不存储哪些警告（warning）或信息（info）日志消息避免浪费磁盘空间。fanout这种交换类型并不能给我们带来很大的灵活性-它只能无意识的广播，在这里我们将使用direct这种类型来进行替换，这种类型的工作方式是，消息只去到它绑定的routingKey队列中去。

![image-20240412232715532](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240412232715532.png)

在上面这张图中，我们可以看到X绑定了两个队列，绑定类型是direct。队列Q1绑定键为orange，队列Q2绑定键有两个：一个绑定键为black，另一个键为green。

在这种绑定情况下，生产者发布消息到exchange上，绑定键为orange的消息会被发布到队列Q1。绑定键为black和green的消息会被发布到队列Q2，其他类型的消息将被丢弃。



### 5.5.3、多重绑定

![image-20240413151943552](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413151943552.png)

当然如果echange的绑定类型是direct，**但是它绑定的多个队列的key如果都相同**，在这种情况下，虽然绑定类型是direct**但是它表现的就和fanout有点类似了**，就跟广播差不多，如上图所示。





### 5.5.4、实战

![image-20240413185902473](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413185902473.png)

![image-20240413171400659](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413171400659.png)

ReceiveLogsDirect01

```java
package com.demo06;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个叫交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //  声明一个队列
        channel.queueDeclare("console",false,false,false,null);

        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume("console", true, deliverCallback, consumerTag -> {
        });
    }
}
```

ReceiveLogsDirect02

```java
package com.demo06;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //  声明一个叫交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //  声明一个队列
        channel.queueDeclare("disk", false, false, false, null);

        channel.queueBind("disk", EXCHANGE_NAME, "error");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {
        });
    }
}
```

DirectLogs

```java
package com.demo06;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class DirectLogs {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
```



## 5.6、Topics

### 5.6.1、之前的类型的问题

我们没有使用只能随意广播的fanout交换，而是使用了direct交换机，从而能实现有选择性的接收日志。

尽管使用direct交换机改进了我们的系统，但是它仍然存在局限性 - 比方说想要接收的日志类型有info.base和info.abvantage，某个队列只想info.base的信息，那这个时候direct就办不到了。这个时候只能使用topic类型。



### 5.6.2、Topic的要求

发送到类型为topic交换机的消息的routingKey不能随意乱写，必须满足一定的要求，它**必须是一个单词列表，以点号（.）分割开**。这些单词可以是任意单词，比如说：“stock.usd.nyse"，"nyse.vmw"，"quick.orange.rabbit"。这种类型的，当然这个单词列表最多也不能超过255个字节。

在这个规则列表中，其中有两个替换符是大家需要注意的。

***（星号）可以代替一个单词**

**#（井号）可以代替零个或多个单词**



### 5.6.3、Topic匹配案例

绑定关系如下

1. Q1 ---> 绑定的是

   中间带orange的3个单词的字符串(* . orange . *)

2. Q2 ---> 绑定的是

   最后一个单词是rabbit的3个单词（* . * . rabbig）

   第一个单词是lazy的多个单词（lazy.#）

![image-20240413190109636](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413190109636.png)

上图是一个队列绑定关系图，它们之间的数据接受情况

| routingKey               | 队列接收情况                               |
| ------------------------ | ------------------------------------------ |
| quick.orange.rabbit      | 被队列Q1Q2接收                             |
| lazy.orange.elephant     | 被队列Q1Q2接收                             |
| quick.orange.fox         | 被队列Q1接收到                             |
| lazy.brown.fox           | 被队列Q2接收到                             |
| lazy.pink.rabbit         | 虽然满足两个绑定但只被队列Q2接收一次       |
| quick.brown.fox          | 不匹配任何板顶不会被任何队列接收到会被丢弃 |
| quick.orange.male.rabbit | 是4个单词不匹配任何绑定会被丢弃            |
| lazy.orange.male.rabbit  | 是4个单词但匹配Q2                          |

当列绑定关系是下列这种情况时需要引起注意

1. **当一个队列绑定键是#，那么这个队列将接收所有数据，就有点像fanout了**
2. **如果队列绑定键当中没有#和*出现，那么该队列绑定类型就是direct了**



### 5.6.4、实战

![image-20240413195655694](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413195655694.png)

ReceiveLogsTopic01

```java
package com.demo07;

import com.rabbitmq.client.*;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    声明主题交换机 以相关队列
    消费者C1
 */
public class ReceiveLogsTopic01 {
    //  交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //  接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        //  声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //  声明队列
        String queueName = "Q1";

        // 交换机队列声明
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息.......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：" + queueName + " 绑定键：" +message.getEnvelope().getRoutingKey());
        };
        CancelCallback callback = (consumerTag) -> {

        };

        //  接收消息
        channel.basicConsume(queueName, true, deliverCallback, callback);
    }
}
```

ReceiveLogsTopic02

```java
package com.demo07;

import com.rabbitmq.client.*;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    声明主题交换机 以相关队列
    消费者C2
 */
public class ReceiveLogsTopic02 {
    //  交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //  接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        //  声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //  声明队列
        String queueName = "Q2";

        // 交换机队列声明
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接收消息.......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：" + queueName + " 绑定键：" + message.getEnvelope().getRoutingKey());
        };
        CancelCallback callback = (consumerTag) -> {

        };

        //  接收消息
        channel.basicConsume(queueName, true, deliverCallback, callback);
    }
}
```

EmitLogTopic

```java
package com.demo07;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/*
    生产者
 */
public class EmitLogTopic {
    //  交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        Map<String, String> bindingKeyMap = getStringStringMap();

        for (Map.Entry<String, String> entry : bindingKeyMap.entrySet()) {
            String routingKey = entry.getKey();
            String message = entry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + routingKey + "=====>" + message);
        }
    }

    private static Map<String, String> getStringStringMap() {
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列Q1Q2接收");
        bindingKeyMap.put("lazy.orange.elephant", "被队列Q1Q2接收");
        bindingKeyMap.put("quick.orange.fox", "被队列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox", "被队列Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列Q2接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何板顶不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是4个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是4个单词但匹配Q2");
        return bindingKeyMap;
    }
}
```

![image-20240413200157095](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413200157095.png)



# 6、死信队列

## 6.1、死信概念

先从概念解释上搞清楚这个定义，死信，顾名思义就是无法被消费的信息，字面意思可以这样理解，一般来说，producer将消息投递到broker或直接到queue里了，consumer从queue取出消息进行消费，但某些时候由于特定的原因**导致queue中的某些消息无法被消费**，这样的消息如果没有后续的处理，就变成了死信，有死信自然就有了死信队列。

应用场景：为了保证订单业务的数据不丢失，需要使用到RabbitMQ的死信队列机制，当消息消费发生异常时，将消息投入死信队列中。还有比如说：用户在商城下单成功并点击去支付后在指定时间未支付时自动失效。



## 6.2、死信的来源

1. 消息TTL过期
2. 队列达到最大长度（队列满了，无法再添加数据到MQ中）
3. 消息被拒绝（basic.reject或者basic.nack）并且requeue = false



## 6.3、死信实战

### 6.3.1、代码架构图

![image-20240413204535623](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413204535623.png)



### 6.3.2、消息TTL过期

Consumer01

```java
package com.demo08;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/*
   死信队列实战
   消费者1
 */
public class Consumer01 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //  普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";


    //  死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //  死信交换机名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //  声明队列
        //   Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        //  正常队列，设置过期之后的死信交换机是谁
        //  x-dead-letter-exchange 固定写法
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //  设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //  TTL  单位毫秒 一般生产者设置
        //  arguments.put("x-message-ttl", 1000);


        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);


        //  binding
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息=========》");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01接收的消息：" + new String(message.getBody(), "UTF-8"));
        };
        CancelCallback callback = consumerTag -> {

        };
        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, callback);
    }
}
```

Producer01

```java
package com.demo08;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    死信队列生产者
 */
public class Producer01 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        //  延迟消息（死信消息） 设置TTL的时间
        AMQP.BasicProperties properties
                = new AMQP.BasicProperties()
                .builder()
                .expiration("10000")
                .build();

        for (int i = 1; i < 11; i++) {
            String message = "info：" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes("UTF-8"));
        }

    }
}
```

![image-20240413214527925](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413214527925.png)

Consumer02

```java
package com.demo08;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    消费者2
 */
public class Consumer02 {
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("等待接收消息=========》");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接收的消息：" + new String(message.getBody(), "UTF-8"));
        };
        CancelCallback callback = consumerTag -> {

        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, callback);
    }
}
```

![image-20240413215245222](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413215245222.png)



### 6.3.3、队列达到最大长度

```java
//  设置正常队列长度的限制，只能装6个小希，超过的就会变成死信
arguments.put("x-max-length", 6);
```

Consumer03

```java
package com.demo08;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/*
   死信队列实战
   消费者3
 */
public class Consumer03 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //  普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";


    //  死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //  死信交换机名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //  声明队列
        //   Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        //  正常队列，设置过期之后的死信交换机是谁
        //  x-dead-letter-exchange 固定写法
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //  设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //  TTL  单位毫秒 一般生产者设置
        //  arguments.put("x-message-ttl", 1000);

        //  设置正常队列长度的限制，只能装6个小希，超过的就会变成死信
        arguments.put("x-max-length", 6);


        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);


        //  binding
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息=========》");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer03接收的消息：" + new String(message.getBody(), "UTF-8"));
        };
        CancelCallback callback = consumerTag -> {

        };

        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, callback);
    }
}
```

![image-20240413221553708](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413221553708.png)

生产者

```java
package com.demo08;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
    死信队列生产者
 */
public class Producer02 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        for (int i = 1; i < 11; i++) {
            String message = "info：" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes("UTF-8"));
        }
    }
}
```

![image-20240413222030777](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413222030777.png)

![image-20240413231933545](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413231933545.png)

从消费可得知，发送了1到10的消息，被塞进死信队列的是头四条，尾四条是留在了默认队列的。



### 6.3.4、消息被拒

![image-20240413232418982](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240413232418982.png)

生产者04

```java
package com.demo08;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/*
   死信队列实战
   消费者4
 */
public class Consumer04 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //  普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";

    //  死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //  死信交换机名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //  声明队列
        //   Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        //  正常队列，设置过期之后的死信交换机是谁
        //  x-dead-letter-exchange 固定写法
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //  设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //  TTL  单位毫秒 一般生产者设置
        //  arguments.put("x-message-ttl", 1000);

        //  设置正常队列长度的限制，只能装6个小希，超过的就会变成死信
        //  arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //  binding
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息=========》");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (msg.equals("info：5")) {
                System.out.println("Consumer04接收的消息是：" + msg + "：此消息是被C1拒绝的");
                //  (标签，不放回队列)
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer04接收的消息是：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        //  开启手动应答
        CancelCallback callback = consumerTag -> {
        };
        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, callback);
    }
}
```

Producer02

```java
package com.demo08;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
/*
    死信队列生产者
 */
public class Producer02 {
    //  普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        for (int i = 1; i < 11; i++) {
            String message = "info：" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
```

![image-20240414125531131](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414125531131.png)



# 7、延迟队列

## 7.1、延迟队列概念

延迟队列，队列内部是有序的，最重要的特性就体现在它的延时属性上，延迟队列中的元素是希望在指定时间到了以后或之前取出和处理，简单来说，延时队列就是用来存放需要在指定时间被处理的元素的队列。



## 7.2、延迟队列使用场景

1. 订单在十分钟之内支付则自动取消
2. 新创建的店铺，如果在十天内都没有上传过商品，则自动发送消息提醒
3. 用户注册成功后，如果三天内没有登陆则进行短信提醒
4. 用户发起退款，如果三天没有得到处理则通知相关运营人员
5. 预定会议后，需要在预定的时间点前十分钟通知各个参与会议人员参加会议

这些场景都有一个特点，**需要在某个事件发生之后或者之前的指定时间点完成某一项任务**，如：发生订单生成事件，在十分钟之后检查订单支付状态，然后将未支付的订单进行关闭；看起来似乎使用定时任务，一直轮询数据，每秒查一次，取出需要被处理的数据，然后处理不就完事了吗？如果数据量比较少，确实可以这样做。比如：对于“如果账单一周内未支付则进行自动结算”这样的需求，如果对于时间不是严格限制，而是宽松意义上的一周，那么每天晚上跑个定时任务检查一下所有未支付的账单，确实也是可以可行的方案。但是对于数据量比较大，并且时效性较强的场景，如：“订单十分钟内未支付则关闭”，短期内未支付的订单数据可能会有很多，活动期间甚至会达到百万甚至千万级别，对这么庞大的数据量仍旧使用轮询的方法显然是不可取的，很可能在一秒内无法完成所有订单的检查，同时会给数据库带来很大压力，无法满足业务要求而且性能低下。

![image-20240414144528677](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414144528677.png)



## 7.3、TTL的两种设置

TTL是什么呢？TTl是RabbitMQ中一个消息或队列的属性，表明一条消息或者该队列中的所有消息的最大存活时间，单位是毫秒。

换句话说，如果一条消息设置饿了TTL属性或进入了设置TTL属性的队列，那么这条消息如果在TTL设置的时间内没有被消息费，则会变成死信。如果同时配置了队列的TTL和消息的TTL，那么较小的那个值将会被使用，有两种方式设置TTL。



### 7.3.1、队列设置TTL

在创建队列的时候，设置队列的x-message-ttl属性

```java
Map<String, Object> params = new HashMap<>();
params.put("x-message-ttl",5000);
return QueueBuilder.durable("QA").withArguments(args).build(); // QA 队列的最大存活时间位 5000 毫秒
```



### 7.3.2、消息设置TTL

针对每条消息设置TTL

```java
rabbitTemplate.converAndSend("X","XC",message,correlationData -> {
    correlationData.getMessageProperties().setExpiration("5000");
});
```



### 7.3.3、两者区别

如果设置了队列的TTL属性，那么一旦消息过期，就会被队列丢弃（如果配置了死信队列，就会被丢弃到死信队列中），而第二种方式，消息即使过期，也不一定会被马上丢弃，因为消息是否过期是在即将投递到消费者之前盘点过的，如果当前队列有严重的消息积压情况，则已过期的消息也许还能存活较长时间。

另外，还需要注意的一点事，如果不设置TTL，表示消息永远不会过期，如果将TTL设置为0，则表示除非此时可以直接投递该消息到消费者，否则该消息将会被丢弃。





## 7.4、整合SpringBoot

### 7.4.1、创建项目

创建一个简单的SpringBoot项目



### 7.4.2、添加依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com</groupId>
    <artifactId>RabbitMQ_SpringBoot</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>RabbitMQ_SpringBoot</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.5</version>
        <relativePath/>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.47</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!--RabbitMQ 依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
    </dependencies>
</project>

```



### 7.4.3、application.yml

```yml
server:
  port: 9091

# 配置RabbitMQ
spring:
  rabbitmq:
    host: 192.168.129.135
    port: 5672
    username: admin
    password: 123456
```



### 7.4.4、Swagger2

```java
package com.rabbitmq_springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("WebApi")
                .apiInfo(webApiInfo())
                .select().build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder().title("RabbitMQ接口文档")
                .description("此文档描述了RabbitMQ微服务接口定义")
                .version("1.0")
                .contact(new Contact("Disney","https://blog.csdn.net/weixin_55801899?spm=1000.2115.3001.5343", "zhziqian2022@163.com"))
                .build();
    }
}

```



## 7.5、队列TTL

### 7.5.1、代码结构图

创建两个队列QA和QB，两者队列TTL分别设置为10s和40s，然后创还能一个交换机X和死信交换机Y，它们的类型都是direct，创建一个死信队列QD，它们的绑定关系如下：

![image-20240414163014944](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414163014944.png)



### 7.5.2、配置文件代码

```java
package com.rabbitmq_springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL队列 配置文件类代码
 */
@Configuration
public class TTLQueueConfig {
    //  普通交换机
    private static final String X_EXCHANGE = "X";
    //  普通队列名称
    private static final String QUEUE_A = "QA";
    private static final String QUEUE_B = "QB";

    //  死信交换机
    private static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    //  死信队列名称
    private static final String DEAD_LETTER_QUEUE_D = "QD";

    //  声明xExchange 别名
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //  声明yExchange 别名
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //  声明普通队列
    //  有TTL 10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>();
        //  设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);

        //  设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //  设置TTL    单位是毫秒 ms
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //  声明普通队列
    //  有TTL 40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>();
        //  设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);

        //  设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //  设置TTL    单位是毫秒 ms
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //  死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }


    //  binding
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA
            , @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB
            , @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD
            , @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
```



### 7.5.3、生产者

```java
package com.rabbitmq_springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 * 10s 40s
 */
@Slf4j
@RequestMapping("/ttl")
@RestController()
public class SendMessage {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //  开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个TTL队列：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40s的队列：" + message);
        log.info("发送成功！");
    }
}
```



### 7.5.4、消费者

```java
package com.rabbitmq_springboot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * TTL 消费者
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    //  接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到死信队列的消息：{}", new Date().toString(), msg);
    }
}
```



### 7.5.5、结果

![image-20240414174308683](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414174308683.png)

第一条消息在10s后变成了死信消息，然后被消费者消费掉，第二条消息在40s之后变成了死信消息，然后被消费掉，这样一个延迟队列就打造完成了。

不过，如果这样使用的话，岂不是**每增加一个新的时间需求，就要新增一个队列**，这里只有10s和40s两个时间选择，如果需要一个小时后处理，那么就需要增加TTL为一个小时的队列，如果是预定会议然后提前通知这样的场景，岂不是要增加无数个队列才能满足需求？



## 7.6、延迟队列优化

### 7.6.1、代码架构图

在这里新增了一个队列QC，绑定关系如下，该队列不设置TTL时间

![image-20240414175127159](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414175127159.png)



### 7.6.2、配置文件类代码

```java
    public static final String QUEUE_C = "QC";

    //  声明QC
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>();
        //  设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);

        //  设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC
            , @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
```



### 7.6.3、生产者

```java
//  开始发消息
//  既要发消息 还要发TTL
@GetMapping("/sendExpirationMsg/{message}/{ttl}")
public void sendExpirationMsg(@PathVariable String message, @PathVariable String ttl) {
    log.info("当前时间：{},发送一条时长{}毫秒TTL信息给队列QC：{}"
             , new Date().toString()
             , ttl
             , message);
    rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
        //  发送消息的时候延迟时长
        msg.getMessageProperties().setExpiration(ttl);
        return msg;
    });
}
```



### 7.6.4、缺点

![image-20240414182333071](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414182333071.png)

看起来似乎没什么问题，但是，如果使用在消息属性上设置TTL的方式，消息可能不会按时“死亡”，因为**RabbitMQ只会检查第一个消息是否过期**，如果过期则丢到死信队列，**如果第一个消息的延时时间很长，而第二个消息的延时时间很短，第二个消息并不会优先得到执行**。



## 7.7、RabbitMQ插件实现延迟队列

上面提到的问题，确实是一个问题，如果不能实现在消息力度上的TTL，并使其在设置的TTL时间及时死亡，就无法设计成一个通用的延时队列。

### 7.7.1、安装延时队列插件

官网：https://www.rabbitmq.com/community-plugins

**rabbitmq_delayed_message_exchange**插件，然后解压放置到RabbitMQ的插件目录。

下载

```shell
git clone https://gitee.com/Jiligu/rabbitmq-delayed-message-exchange.ez.git
```

把文件拷贝到

```shell
/usr/lib/rabbitmq/lib/rabbitmq_server-3.10.0/pluginscp /mymq/rabbitmq_delayed_message_exchange-3.10.0.ez /usr/lib/rabbitmq/lib/rabbitmq_server-3.10.0//plugins/
```

执行命令

```shell
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

![image-20240414203329387](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414203329387.png)

![image-20240414203943906](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414203943906.png)



### 7.7.2、代码架构图

在这里新增了一个队列delayed.queue，一个自定义交换机delayed.exchange，绑定关系如下：

![image-20240414204616463](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414204616463.png)



### 7.7.3、基于插件的延迟队列（配置文件类代码）

在我们自定义的交换机中，这是一种新的交换类型，该类型支持延迟投递机制，消息传递后并不会立即投递到目标队列中，而是存储在mnesia（一个分布式数据系统）表中，当达到投递时间时，才投递到目标队列中。

```java
package com.rabbitmq_springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {
    //  交换机
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //  队列
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //  routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //  声明交换机
    /*
    	public CustomExchange(String name, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		super(name, durable, autoDelete, arguments);
		this.type = type;
	}
	1、交换机名称
	2、交换机类型
	3、是否需要持久化
	4、是否需要自动删除
	5、参数
     */
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");

        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message",
                true,
                false,
                arguments);
    }

    //  声明队列
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //  Binding
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange
    ) {
        return BindingBuilder
                .bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }
}
```





### 7.7.4、基于插件的延迟队列（生产者）

```java
//  基于插件的发消息
@GetMapping("/sendDelayMsg/{message}/{delayTime}")
public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
    log.info("当前时间：{},发送一条时长{}毫秒信息给延迟队列delayed queue：{}",
             new Date().toString(),
             delayTime,
             message);
    rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
        //  发送消息的时候延迟时长 单位毫秒
        msg.getMessageProperties().setDelay(delayTime);
        return msg;
    });
}
```





### 7.7.5、基于插件的延迟队列（消费者）

```java
package com.rabbitmq_springboot.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq_springboot.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Date;

//  消费者 基于插件的延迟消息
@Configuration
@Slf4j
public class DelayQueueConsumer {

    //  监听消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到延迟队列的消息：{}", new Date().toString(), msg);
    }
}
```



### 7.7.6、结果

![image-20240414214358082](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414214358082.png)

第二个消息先被消费掉了，符合预期



## 7.8、总结

延时队列在需要延时处理的场景下非常有用，使用RabbitMQ来实现延时队列可以很好的利用RabbitMQ的特性，如：消息可靠发送、消息可靠投递、死信队列来保障消息至少被消费一次以及未被正确处理的消息不会被丢弃。另外，通过RabbitMQ集群的特性，可以很好的解决单点故障问题，不会因为单个节点挂掉导致延时队列不可用或消息丢失。

当然，延时队列还有很多其他选择，比如利用Java的DelayQueue，利用Redis的zset，利用Quarter或利用Kafka的时间轮，这种方式各有各特点，看需要使用的场景。

# 8、发布确认高级

在生产环境中由于一些不明原因，导致RabbitMQ重启，在RabbitMQ重启期间生产者消息投递失败，导致消息丢失，需要手动处理和回复。于是，我们开始及思考，如何才能进行RabbitMQ的消息可靠投递呢？ 特别是再这样比较极端的情况，RabbitMQ集群不可用的时候，无法投递的消息该如何处理呢？



## 8.1、发布确认SpringBoot版本

### 8.1.1、确认机制方案

![image-20240414223927081](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414223927081.png)



### 8.1.2、代码架构图

![image-20240414225245336](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240414225245336.png)



### 8.1.3、配置文件

在配置文档中需要添加

```yml
spring:
  rabbitmq:
    publisher-confirm-type: correlated
```

- NONE

  禁用发布确认模式，默认值

- CORRELATED

  发布消息成功到交换机后会触发回调函数

- SIMPLE

  经测试有两种效果，其一效果是和CORRELATED值一样会触发回调函数，

  其二在发布消息成功后rabbitTemplate调用waitForConfirms或waitForConfirmsOrDie方法

  等待broker节点返回发送结果，根据返回结果来判定下一步的逻辑，要注意的点事waitForConfirmsOrDie方法如果返回false，则会关闭channel，接下来则无法发送消息到broker



### 8.1.4、配置类

```java
package com.rabbitmq_springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    发布确认 配置类
 */

@Configuration
public class ConfirmConfig {
    //  交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    //  队列
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    //  routingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //  声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    //  声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //  绑定
    @Bean
    public Binding queueBindingExchange(
            @Qualifier("confirmQueue") Queue queue,
            @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder.bind(queue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
}
```



### 8.1.5、生产者

```java
package com.rabbitmq_springboot.controller;

import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //  发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message);
        log.info("发送消息内容{}", message);
    }
}
```



### 8.1.6、消费者

```java
package com.rabbitmq_springboot.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

//  接收消息
@Component
@Slf4j
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("接收到的队列消息confirm.queue消息：{}", msg);
    }
}
```



### 8.1.7、回调接口

```java
package com.rabbitmq_springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //  注入
        rabbitTemplate.setConfirmCallback(this);
    }

    //  交换机确认回调方法
    /*
        1、发消息 交换机接收到了回调
        参数1、correlationData 保存回调信息的ID及相关信息
        参数2：ack 交换机收到消息 true
        参数3：失败的原因   cause   null
        2、发消息 交换机接收失败了 回调
        参数1、correlationData 保存回调信息的ID及相关信息
        参数2：ack 交换机收到消息 false
        参数3：失败的原因   cause   问题点
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了消息，ID：{}的消息", id);
        } else {
            log.info("交换机还未收到消息，ID:{}的消息。由于原因：{}", id, cause);
        }
    }
}
```



### 8.1.8、交换机确认

```java
package com.rabbitmq_springboot.controller;

import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //  发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        //  CorrelationData
        CorrelationData correlationData = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message, correlationData);
        log.info("发送消息内容{}", message);
    }
}
```

```java
package com.rabbitmq_springboot.controller;

import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //  发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        //  CorrelationData
        CorrelationData correlationData = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message + "1", correlationData);
        log.info("发送消息内容{}", message + "key1");

        CorrelationData correlationData2 = new CorrelationData("2");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "2",
                message + "2", correlationData);
        log.info("发送消息内容{}", message + "key12");
    }
}
```



### 8.1.9、结果分析

**当交换机不存在时**

![image-20240415172554096](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415172554096.png)

```markdown
交换机还未收到消息，ID:1的消息。由于原因：channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'confirm_exchange2' in vhost '/', class-id=60, method-id=40)
```

****

**当信道不存在时**

![image-20240415173254843](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415173254843.png)

```markdown
当信道不存时，发送消息给交换机，生产者是不知道信道不存在的，消息就会丢失
```



## 8.2、回退消息

### 8.2.1、Mandatory参数

**在仅开启了生产者确认机制的情况下，交换机接收到的消息后，会直接给消息生产者发送确认消息，如果发现该消息不可路由，那么消息会被直接丢弃，此时生产者是不知道消息被丢弃这个时间的**。那么如何让无法被路由的消息帮我们想办法处理一下？通过设置mandatory参数可以在当前消息传递过程中，没有传达到目的地就返回给生产者。

```yml
spring:
  rabbitmq:
    publisher-returns: true
```



### 8.2.2、消息生产者代码

```java
//  发消息
@GetMapping("/sendMessage/{message}")
public void sendMessage(@PathVariable String message) {
    //  CorrelationData
    CorrelationData correlationData = new CorrelationData("1");


    rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                                  ConfirmConfig.CONFIRM_ROUTING_KEY,
                                  message + "1", correlationData);
    log.info("发送消息内容{}", message + "key1");

    CorrelationData correlationData2 = new CorrelationData("2");

    rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                                  ConfirmConfig.CONFIRM_ROUTING_KEY + "2",
                                  message + "2", correlationData);
    log.info("发送消息内容{}", message + "key12");
}
```



### 8.2.3、回调接口

```java
package com.rabbitmq_springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //  注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    //  交换机确认回调方法
    /*
        1、发消息 交换机接收到了回调
        参数1、correlationData 保存回调信息的ID及相关信息
        参数2：ack 交换机收到消息 true
        参数3：失败的原因   cause   null
        2、发消息 交换机接收失败了 回调
        参数1、correlationData 保存回调信息的ID及相关信息
        参数2：ack 交换机收到消息 false
        参数3：失败的原因   cause   问题点
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了消息，ID：{}的消息", id);
        } else {
            log.info("交换机还未收到消息，ID:{}的消息。由于原因：{}", id, cause);
        }
    }

    //  消息回退
    //  只有消息发送不到目的地就会回退
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息{},被交换机{}，回退了，回退原因：{}，路由Key：{}"
                , new String(message.getBody(), StandardCharsets.UTF_8)
                , exchange, replyText, replyCode);
    }
}
```



### 8.2.4、结果分析

![image-20240415174750972](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415174750972.png)



## 8.3、备份交换机

为一个普通交换机添加一个”备胎“，当交换机接收到一条无法路由的消息时，就把消息转发到备份交换机，有备份交换机来转发和处理，该交换机类型一般为广播型（fanout、扇出），这样就可以把所有消息发送到吁气绑定队列中去了，



### 8.3.1、代码架构图

![image-20240415202708399](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415202708399.png)



### 8.3.2、修改配置类

```java
package com.rabbitmq_springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    发布确认 配置类
 */

@Configuration
public class ConfirmConfig {
    //  交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    //  队列
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    //  routingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //  备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    //  备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //  报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    //  声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {

        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME)
                .build();
    }

    //  声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //  绑定
    @Bean
    public Binding queueBindingExchange(
            @Qualifier("confirmQueue") Queue queue,
            @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(queue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }


    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //  备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_EXCHANGE_NAME).build();
    }

    //  报警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //  绑定
    @Bean
    public Binding backupBindingExchange(
            @Qualifier("backupQueue") Queue queue,
            @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }
    @Bean
    public Binding warningBindingExchange(
            @Qualifier("warningQueue") Queue queue,
            @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }
}
```



### 8.3.3、结果分析

报警消费者

```java
package com.rabbitmq_springboot.consumer;


import com.rabbitmq_springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

//  报警消费者
@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWaringMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("报警，路由不过去信息：{}", msg);
    }
}
```

![image-20240415204559032](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415204559032.png)

mandatory（生产者回退消息）参数与备份交换机可以一起使用的时候，如果两者同时开启，消息优先去哪？**备份交换机的优先级更高。**

# 9、RabbitMQ其他知识点

## 9.1、幂等性

### 9.1.1、概念

用户对于同一操作发起的一次请求或多次请求的结果是一致的，不会因为多点击而产生了副作用。举个最简答的例子，那就是支付，用户购物商品后支付，支付扣款成功，但是返回结果的时候网络异常，此时钱已经扣了，用户再次点击按钮，此时会进行第二次扣款，返回结果成功，用户查询余额发现多扣钱了，流水记录变成了两条。在以前的但应用系统中，我们只需要把数据操作系统放入失误即可，发生错误立即回滚，但是再响应客户端的时候也有可能出现网络中断或者异常等等。



### 9.1.2、消息重复消费

消费者在消费MQ的消息时，MQ已把消息发送给消费者，消费者在给MQ返回ACK时网络中断，MQ未收到确认消息，该条消息会重新发送给其他的消费者，或者在网络重连后再次发送给该消费者，但实际上该消费者已经成功消费了该条消息，造成消费者消费了重复的消息。



### 9.1.3、解决思路

MQ消费者的幂等性的解决一般使用全局ID或者写个唯一标识比如时间戳或者UUID或者订单消费者消费MQ中的消息也可以利用MQ的该ID来判断，或者可按自己的规划生成一个全局唯一ID，每次消息消费时用该ID先判断该消息是否已消费过。



### 9.1.4、消费端的幂等性保障

在海量订单生成的业务高峰期，生产端有可能就会重复发生了消息，这时候消费端就要实现幂等性，这就意味着我们的消息永远不会被消费多次，即使我们收到了一样的消息。业界主流的幂等性有两种操作：

1. 唯一ID + 指纹码机制，利用数据库主键去重
2. 利用Redis的原子性去实现



### 9.1.5、唯一ID + 指纹码机制

指纹码：我们的一些规则或时间戳加别的服务给到的唯一信息码，它并不一定是我们系统生成的，基本都是由我们的业务规则拼接而来的，但是一定要保证唯一性，然后就利用查询语句机型判断这个id是否存在数据库中，优势就是实现简单（拼接），然后查询是否重复；劣势就是在高并发的时候，如果是单个数据库就会有写入性能瓶颈，当然也可以采用分库分表来提升性能。



### 9.1.6、Redis原子性

利用Redis执行setnx命令，天然具有幂等性。从而实现不重复消费。



## 9.2、优先级队列

### 9.2.1、使用场景

在我们系统中有一个订单催付的场景，我们的客户在天猫下的订单，淘宝会及时将订单推送给我们，如果在用户设定的时间内未付款那么就会给用户推送一条短信提醒，很简单的一个功能对吧。

但是，天猫商家对我们来说，肯定是要分大客户和小客户的对吧，比如像苹果、小米这样大商家一年起码能给我们创造很大的利润，所以理应当然，他们的订单必须得到优先处理，而曾经我们的后端系统是使用 redis 来存放的定时轮询，大家都知道 redis 只能用 List 做一个简简单单的消息队列，并不能实现一个优先级的场景，所以订单量大了后采用 RabbitMQ 进行改造和优化，如果发现是大客户的订单给一个相对比较高的优先级， 否则就是默认优先级。



### 9.2.2、如何添加

1. 控制台页面添加

   ![image-20240415230630902](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415230630902.png)

2. 代码中添加队列的优先级

   ```java
   package com.demo09;
   
   import com.rabbitmq.client.AMQP;
   import com.rabbitmq.client.Channel;
   import com.utils.RabbitMQUtils;
   
   import java.io.IOException;
   import java.util.HashMap;
   import java.util.Map;
   import java.util.concurrent.TimeoutException;
   
   //  优先级队列
   public class Producer_Queue {
   
       public static final String QUEUE_NAME = "hello_queue";
   
       public static void main(String[] args) throws IOException, TimeoutException {
           Channel channel = RabbitMQUtils.getChannel();
   
           Map<String, Object> arguments = new HashMap<>();
           //  官方允许的是0 - 255 之间，不要设置过大，浪费CPU与内存
           arguments.put("x-max-priority", 10);
           channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
   
           for (int i = 1; i < 11; i++) {
               String info = "info" + i;
               if (i == 5) {
                   AMQP.BasicProperties properties
                           = new AMQP.BasicProperties().builder().priority(5).build();
                   channel.basicPublish("", QUEUE_NAME, properties, info.getBytes());
               } else {
                   channel.basicPublish("", QUEUE_NAME, null, info.getBytes());
               }
           }
       }
   }
   ```

3. 代码中消费者添加优先级

   ```java
   package com.demo09;
   
   import com.rabbitmq.client.CancelCallback;
   import com.rabbitmq.client.Channel;
   import com.rabbitmq.client.DefaultConsumer;
   import com.rabbitmq.client.DeliverCallback;
   import com.utils.RabbitMQUtils;
   
   import java.io.IOException;
   import java.nio.charset.StandardCharsets;
   import java.util.concurrent.TimeoutException;
   
   public class Consumer_Queue {
   
       public static final String QUEUE_NAME = "hello_queue";
   
       public static void main(String[] args) throws IOException, TimeoutException {
           Channel channel = RabbitMQUtils.getChannel();
   
           DeliverCallback deliverCallback = (consumerTag, message) -> {
               System.out.println("接收的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
           };
           CancelCallback callback = consumerTag -> {
   
           };
   
           channel.basicConsume(QUEUE_NAME, true, deliverCallback, callback);
       }
   }
   ```

   ![image-20240415232525032](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240415232525032.png)

   info5就优先被消费了

4. 注意事项

   要让队列实现优先级需要做的事情如果有如下事情：

   1. 队列需要设置为优先队列
   2. 消息需要设置信息的优先级
   3. 消费者需要等待消息已经发送到队列中才去消费，因为这样才有机会对信息进行排序



## 9.3、惰性队列

### 9.3.1、使用场景

RabbitMQ从3.6.0版本开始引入了惰性队列的概念。惰性队列会尽可能的将信息存入磁盘中，而在消费者到相应的消息时才会被加载到内存中，它的一个重要的设计目标是能够支持更长的队列，即支持更多的消息存储。当消费者由于各种各样的原因（比如消费者下线、宕机亦或是由于维护而关闭等）而致使长时间内不能消费造成堆积时，惰性队列就很有必要了。

默认情况下，当生产者将消息发送到RabbitMQ的时候，队列中的消息会尽可能的存储在内存之中，这样可以更加快速的将消息发送给消费者。即使是持久化的消息，在被写入磁盘的同时也会在内存中驻留一份备份。当RabbitMQ需要释放内存的时候，会将内存中的消息换页至磁盘中，这个操作会耗费较长的时间，也会阻塞队列的操作，进而无法接收新的消息。虽然RabbitMQ的开发者们一直在升级相关的算法，但是效果始终不太理想，尤其是在消息量特别大的时候。



### 9.3.2、两种模式

队列具备两种模式：default 和 lazy。默认的为default模式，在3.6.0之前的版本无需做任何变更。lazy模式即为惰性队列的模式，可以通过调用channel.queueDeclare方法的时候在参数中设置，也可以铜鼓Policy的方式设置，如果一个队列同时使用这两种方式设置的话，那么Policy的方式具备更高的优先级。如果要通过声明的方式改变已有队列的模式的话，那么只能先删除队列，然后再重新声明一个新的。

在队列声明的时候可以通过“x-queue-mode”参数来设置队列的模式，取值为“default”或“lazy”。下面示例中演示了一个惰性队列的声明细节：

```java
Map<String,Object> args = new HashMap<String,Object>();
args.put("x-queue-mode","lazy");
channel.queueDeclare("queue_name",false,false,false,args)；
```



### 9.3.3、内存开销对比

![image-20240416131731226](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416131731226.png)

在发送1百万条消息，每条消息大概占1KB的情况下，普通队列占用内存是1.2GB，而惰性队列仅仅占用1.5MB。



# 10、RabbitMQ集群

## 10.1、Clustering

### 10.1.1、使用集群的原因

如果RabbitMQ服务器遇到内润崩溃、机器掉电或者主板故障等情况，该怎么办？单台RabbitMQ服务器可以满足每秒1000条消息的吞吐量，那么如果应用需要RabbitMQ服务满足每秒10万条消息的吞吐量呢？



### 10.1.2、搭建步骤

1. 修改3台机器的主机名

   ```shell
   vim /etc/hostname
   ```

2. 配置各个几点到额host文件，让各个结点都能识别对方

   ```shell
   vim /etc/hosts
   
   192.168.135.1 node1
   192.168.135.2 node2
   192.168.135.3 node3
   ```

3. 以确保各个节点的cookie文件使用的是同一个值

   1. 在node1上执行远程操作命令

      ```shell
      scp /var/lib/rabbitmq/.erlang.cookie root@node2:/var/lib/rabbitmq/.erlang.cookie
      scp /var/lib/rabbitmq/.erlang.cookie root@node3:/var/lib/rabbitmq/.erlang.cookie
      ```

4. 启动RabbitMQ服务，顺带启动Erlang虚拟机和RabbitMQ应用服务（在三台节点上分别执行以下命令）

   ```shell
   rabbitmq-server -datached
   ```

5. 在节点2执行

   ```shell
   # rabbitmqctl stop会将Erlang虚拟机关闭  rabbitmqctl stop_app 只关闭rabbitmq服务
   rabbitmqctl stop_app
   rabbitmqctl reset
   rabbitmqctl join_cluster rabbit@node1
   # 只启动rabbitmq服务
   rabbitmqctl start_app
   ```

6. 在节点3执行

   ```shell
   rabbitmqctl stop_app
   rabbitmqctl reset
   rabbitmqctl join_cluster rabbit@node2
   rabbitmqctl start_app
   ```

7. 集群状态

   ```shell
   rabbitmqctl cluster_status
   ```

8. 需要重新设置用户

   ```markdown
   # 创建账号
   rabbitmqctl add_user admin 123
   # 设置用户角色
   rabbitmqctl set_user_tags admin administrator
   # 设置用户权限
   rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*" 
   ```

   之后在三个集群节点的任意一个可视化界面登录均可

9. 接触集群点，node2和node3分别执行

   ```shell
   rabbitmqctl stop_app
   rabbitmqctl reset
   rabbitmqctl start_app
   rabbitmqctl cluster_status
   # 此项命令均在node1上执行
   rabbitmqctl forget_cluster_node rabbit@node2
   ```



## 10.2、镜像队列

### 10.2.1、使用镜像的原因

如果RabbitMQ集群中只有一个Broker节点，那么该节点的失效将导致整体服务的临时性不可用，并且也可能会导致消息的丢失。可以将所有消息都设置为持久化,并且对应队列的durable属性也设置为true,但是这样仍然无法避免由于缓存导致的问题:因为消息在发送之后和被写入磁盘井执行刷盘动作之间存在一个短暂却会产生问题的时间窗。通过publisherconfirm机制能够确保客户端知道哪些消息己经存入磁盘，尽管如此，一般不希望遇到因单点故障导致的服务不可用。
引入镜像队列（Mirror Queue）的机制，可以将队列镜像到集群中的其他Broker节点之上，如果集群中的一个节点失效了，队列能自动地切换到镜像中的另一个节点上以保证服务的可用性。



### 10.2.2、搭建步骤

1. 启动三台集群节点

2. 随便找个几点添加policy

   ![image-20240416135934857](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416135934857.png)

3. 在node1上创建一个队列发送信息，队列存在镜像队列



## 10.3、Haproxy+Keepalive实现高可用负载均衡

### 10.3.1、整体架构图

![image-20240416142312836](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416142312836.png)



### 10.3.2、Haproxy实现负载均衡

HAProxy.提供高可用性、负载均衡及基于TCPHTTP应用的代理，支持虚拟主机，它是免费、快速并且可靠的一种解决方案，包括Twitter,Reddit,StackOverflow,GitHub.在内的多家知名互联网公司在使用。HAProxy实现了一种事件驱动、单一进程模型，此模型支持非常大的井发连接数。



## 10.4、Federation Exchange

### 10.4.1、使用原因

(broker北京)，(broker深圳)彼此之间相距甚远，网络延迟是一个不得不面对的问题。有一个在北京的业务(Client北京)需要连接(broker北京),向其中的交换器exchangeA.发送消息，此时的网络延迟很小,(Client北京)可以迅速将消息发送至exchangeA.中，就算在开启了publisherconfirm.机制或者事务机制的情况下，也可以迅速收到确认信息。此时又有个在深圳的业务(Client深圳)需要向exchangeA发送消息，那么(Client深圳)(broker北京)之间有很大的网络延迟，(Client深圳)将发送消息至exchangeA会经历一定的延迟，尤其是在开启了publisherconfirm.机制或者事务机制的情况下，(Client深圳)会等待很长的延迟时间来接收(broker北京)的确认信息，进而必然造成这条发送线程的性能降低，甚至造成一定程度上的阻塞。
将业务(Client深圳)部署到北京的机房可以解决这个问题，但是如果(Client深圳)调用的另些服务都部署在深圳，那么又会引发新的时延问题，总不见得将所有业务全部部署在一个机房，那么容灾又何以实现?这里使用Federation插件就可以很好地解决这个问题。



### 10.4.2、搭建步骤

1. 需要保证每台节点单独运行

2. 在每台机器上开启federation相关插件

   ```shell
   # 每台节点均需执行以下命令
   rabbitmq-plugins enable rabbitmq_federation
   rabbitmq-plugns enable rabbitmq_federation_management
   ```

3. 原理图

   ![image-20240416144854614](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416144854614.png)

4. 在下游节点（node2）配置上游节点（node1）

   ![image-20240416145037682](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416145037682.png)

5. 添加policy

   ![image-20240416145125444](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416145125444.png)

   

## 10.5、Federation Queue

### 10.5.1、使用原因

联邦队列可以在多个Broker节点(或者集群)之间为单个队列提供均衡负载的功能。一个联邦队列可以连接一个或者多个上游队列(upstream queue)，并从这些上游队列中获取消息以满足本地消费者消费消息的需求。



### 10.5.2、原理图

![image-20240416145723841](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416145723841.png)



## 10.6、Shovel

### 10.6.1、使用原因

Federation具备的数据转发功能类似，Shovel够可靠、持续地从一个Broker中的队列(作为源端，即source)拉取数据并转发至另一个Broker中的交换器(作为目的端，即destination)。作为源端的队列和作为目的端的交换器可以同时位于同一个Broker，也可以位于不同的Broker上。Shovel可以翻译为"铲子",是一种比较形象的比喻，这个"铲子"可以将消息从一方"铲子"另一方。Shovel行为就像优秀的客户端应用程序能够负责连接源和目的地、负责消息的读写及负责连接失败问题的处理。



### 10.6.2、原理图

![image-20240416150155077](K:\GitHub\notes\RabbitMQ\RabbitMQ.assets\image-20240416150155077.png)












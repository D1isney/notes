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



下载Erlang

```shell
wget http://erlang.org/download/otp_src_21.3.tar.gz
```



解压Erlang

```shell
 tar -zxvf otp_src_21.3.tar.gz
```



配置安装目录

```shell
./configure --prefix=/myerlang
```



安装

```shell
make install
```



添加环境变量

```shell
echo 'export PATH=$PATH:/myerlang' >> /etc/profile
```



刷新环境变量

```shell
source /etc/profile
```



查看安装情况

```shell
erl -version
```



下载RabbitMQ

```shell
wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.7.15/rabbitmq-server-generic-unix-3.7.15.tar.xz
```



没有解压tar.xz格式需要先安装

```shell
yum install -y xz
```



第一次解压

```shell
xz -d rabbitmq-server-generic-unix-3.7.15.tar.xz 
```



第二次解压

```shell
tar -xvf rabbitmq-server-generic-unix-3.7.15.tar 
```



配置环境变量

```shell
echo 'export PATH=$PATH:/mymq/rabbitmq_server-3.7.15/sbin' >> /etc/profile
```



刷新环境变量

```shell
source /etc/profile
```



开启web插件

```shell
rabbitmq-plugins enable rabbitmq_management
```



启动命令（关闭防火墙或者开放5672 15672端口）

```shell
#启动：
rabbitmq-server -detached
 
#停止：
rabbitmqctl stop
 
#状态：
rabbitmqctl status

#开机自动启动
chkconfig rabbitmq-server on
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

这个时候即使重启了RabbitMQ，队里也依然存在



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



当然如果echange的绑定类型是direct，**但是它绑定的多个队列的key如果都相同**，在这种情况下，虽然绑定类型是direct**但是它表现的就和fanout有点类似了**，就跟广播差不多，如上图所示。
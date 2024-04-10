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




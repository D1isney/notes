# Redis7高阶

# 1、Redis单线程 VS 多线程

## 1.1、面试题

`redis到底是单线程还是多线程？`

> 这种问法其实并不严谨，为啥这么说呢？
>
> Redis4之后才慢慢支持多线程，直到Redis6/7后才稳定
>
> Redis的版本很多3.x、4.x、6.x，版本不同架构也是不同的，不限定版本问题是否单线程也不太严谨
>
> 1、版本3.x，最早版本，也就是大家口口相传的Redis是单线程
>
> 2、版本4.x，严格意义来说也不是单线程，而是负责处理客户端请求的线程是单线程，但是**开始加了点多线程的东西（异步删除）**。--貌似
>
> 3、2020年5月版本的6.x后及2020年出的7.0版本后，**告别了大家印象中的单线程，用一种全新的多线程来解决问题。**--实锤

`Redis为什么要选择单线程？`

> Redis是单线程
>
> 主要是指Redis的网络IO和键值对读写是由一个线程来完成的，Redis的处理客户端的请时包括获取（Socket读）、解析、执行、内容返回（Socket写）等都由一个顺序串行的主线程序处理，这就是所谓的“单线程”。这也是Redis对外提供键值存储的主要流程。
>
> ![image-20240225204845701](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225204845701.png)
>
> 但Redis的其他功能，**比如持久化RDB、AOF、异步删除、集群数据同步等等，其实是由额外的线程执行的。Redis命令工作线程是单线程，但是，整个Redis来说，是多线程的**。 
>
> 1. 基于内存操作：Redis的所有数据都存在内存中，因此所有的运算都是内存级别的，所以它的性能比较高。
> 2. 数据结构简单：Redis的数据结构是专门设计的，而这些简单的数据结构的查找和操作的时间大部分复杂度都是O(1)，因此性能比较高。
> 3. 多路复用和非阻塞I/O：Redis使用I/O多路复用功能来监听多个Socket连接客户端，这样就可以使用一个线程连接来处理多个请求，减少线程切换带来的开销，同时避免了I/O阻塞。
> 4. 避免上下文切换：因为是单线程模型，因此就避免了不必要的上下文切换和多线程竞争，这就省去了多线程切换带来的时间和性能上的消耗，而且单线程不会导致死锁问题的发生。



`Redis4.0之前一直采用单线程的主要原因有什么？`

> 简单来说，Redis4.0之前一直采用单线程的主要原因有以下三个：
>
> 1. 使用单线程模型使Redis的开发和维护更简单，因为单线程模型方便开发和调试。
> 2. 即使使用单线程模型也并发的处理了多客户端的请求，主要使用的是I/O多路复用和非阻塞IO。
> 3. 对于Redis系统来说，**主要的性能瓶颈是内存或者网络带宽而并非CPU**。



`既然单线程这么好，为什么逐渐又加入了多线程特性？`

> 正常情况下使用del指令可以很快的删除数据，而当被删除的key是一个非常大的对象时，例如是包含了成千上万个元素的hash集合时，那么del之灵就会造成Redis主线程卡顿。
>
> **这就是Redis3.x单线程时代最经典的故障，大Key删除的头疼问题。**
>
> 由于Redis是单线程，del bigkey
>
> 等待很久这个线程才会释放，类似加了一个synchronized锁，高并发的时候，程序会非常堵。
>
> **解决：**
>
> 1. 使用惰性删除可以有效的避免Redis卡顿的问题。
> 2. 在Redis4.0就引入了多个线程来实现数据的异步惰性删除等功能，但是其处理读写请求的仍然只有一个线程，所以仍然算是狭义上的单线程。



`Unix网络编程中的五种IO模型`

> - Blocking IO -- 阻塞IO
> - NoneBlocking -- 非阻塞IO
> - IO Multiplexing -- IO多路复用
> - Signal Driven IO -- 信号驱动IO
> - Asynchronous IO -- 异步IO



`什么是IO多路复用`

> 1. Linux世界一切接文件
> 2. IO多路复用是什么？
>    1. 一种同步的IO模型，实现**一个线程**监视**多个文件句柄，一旦某个文件句柄就绪**就能够通知对应应程序进行相应的读写操作，**没有文件句柄就绪时**就会阻塞应用程序，从而释放CPU资源
>    2. 概念：
>       - I/O：网络I/O，尤其在操作系统层面指数据在内核态和用户态之间的读写操作
>       - 多路：多个客户端连接（连接就是套接字描述符，即Socket或者Channel）
>       - 复用：复用一个或者几个线程
>       - IO多路复用：也就是说一个或一组线程处理多个TCP连接，使用单进程就能够实现同时处理多个客户端的连接，**无需创建或者维护过多的进程/线程**
> 3. 场景体验，引出Epoll
> 4. 小总结
>    - 只使用一个服务端进程可以同时处理多个套接字描述符连接



`Redis为什么这么快？`

> IO多路复用+epoll函数使用，才是Redis为什么这么快的直接原因，而不是仅仅单线程命令+Redis安装在内存中。



## 1.2、Redis7默认是否开启了多线程？

如果在实际应用中，发现Redis实例的**CPU开销不大但吞吐量却没有提升**，可以考虑使用Redis7的多线程机制，加速网络处理，进而提升实例的吞吐量。

Redis7将所有数据放在内存中，内存的响应时间长大约为100纳秒，对于小数据包，Redis服务器可以处理8W到10W的QPS，这也是Redis处理的极限了，**对于80%的公司来说，单线程的Redis已经足够使用了**。

在Redis6.0及7后，**多线程机制默认是关闭的**，如果需要使用多线程功能，需要在Redis.conf中完成两个设置。

![image-20240225224329078](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225224329078.png)

![image-20240225224614883](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225224614883.png)



## 1.3、小总结

> Redis自身出道就是优秀，基于内存操作、数据结构简单、多路复用和非阻塞I/O、避免了不必要的线程上下文切换等特性，在单线程的环境下依然很快；
>
> 但对于大数据的Key删除还是卡顿厉害，因此在Redis4.0引入了多线程unlink key / flushall async等命令，主要用于Redis数据的异步删除；
>
> 而在Redis6/7中引入了I/O多线程的读写，这样就可以更加高效的处理更多的任务了，**Redis只是将I/O读写变成了多线程**，而**命令执行依旧是由主线程串行执行**，因此在多线程下操作Redis不会出现线程安全的问题了。
>
> **Redis无论是当初的单线程设计，还是如今与放出设计相背的多线程，目的只有一个：让Redis变得越来越快**。

# 2、BigKey

## 2.1、面试题

`阿里广告平台，海量数据里查询某以固定前缀的key`

`小红书，你如何生产上限值key */flushdb/flushall等危险命令以防止误删误用？`

`美团，MEMORY USAGE命令你用过吗?`

`Bigkey问题，多大算big？你如何发现？如何删除？如何解决？`

`BigKey你做过调优吗？惰性释放lazyfree了解过吗？`

`MoreKey问题，生产上redis数据有1000W记录，你如何遍历？key *可以吗？`



## 2.2、MoreKey案例

Linux Bash下执行，插入100W

```bash
for((i=1;i<=100*10000;i++)); do echo "set k$i v$i" >> /tmp/redisTest.txt;done;
```

通过Redis提供的管道 --pipe命令插入100w大批量数据

```bash
cat /tmp/redisTest.txt | redis-cli -h 127.0.0.1 -p 6399 -a zzq121700 --pipe
```

使用DBSIZE检查数据时候插入

```bash
DBSIZE
```

如果执行Keys *，Redis的时间（这次跑的算还好），在线上的Redis执行这个命令，基本就GG了

![image-20240225233619154](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225233619154.png)

<font color="red">keys * 这个执行致命的弊端，在实际环境中最好不要使用</font>

```markdown
这个指令没有offset、limit参数，是要一次性吐出所有满足条件的key，由于Redis是单线程，其所有操作都是原子的，而keys算法是遍历算法，复杂度是O(n)，如果实例中有千万级以上的key，这个指令就会导致Redis服务卡顿，所有读写Redis的其他的指令都会被延后甚至会超时报错，可能会引起缓存雪崩甚至数据库宕机。
```



**生产上限制keys * / flushdb / flushall等命令以防止误删误用？**

通过配置设置禁用这些命令，redis.conf在SECURITY这一项中

![image-20240225234832005](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225234832005.png)

![image-20240225235003742](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225235003742.png)

再次使用keys * ，就会被禁止

![image-20240225235400628](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240225235400628.png)



**不用keys *避免卡顿，那该用什么呢？**

官网：[SCAN](https://redis.io/commands/scan/)

类似mysql limit的**但不完全相同**

Scan命令用于迭代数据库中的数据库键

语法：

```bash
SCAN cursor [MATCH pattern] [COUNT count]
cursor --游标
pattrrn --匹配的模式
count --指定从数据集里返回多少个元素，默认值是10
```

特点：

1. Scan命令是一个基于游标的迭代器，每次被调用之后，都会向用户返回一个新的游标，**用户在下次迭代时需要使用这个新游标作为SCAN命令游标参数**，以此来延续之前的迭代过程。
2. Scan返回一个包含两个元素的数组
   1. 第一个元素适用于进行下一次迭代的新游标
   2. 第二个元素则是一个数组，这个数组中包含了所有被迭代的元素。**如果新游标返回零表示迭代已结束**。
3. SCAN的遍历顺序：**非常特别，他不是从低一维数组的第零位一直遍历到尾，而是采用了高位加法来遍历。之所以使用这样特殊的方式进行遍历，是考虑到字典的扩容和缩容时避免槽位的遍历重复和遗漏**。

使用：

```bash
SCAN 0 match k* count 15
```



## 2.3、BigKey案例

`多大算Big`

> 1. 参考《阿里云Redis开发规范》
>    - 【强制】：拒绝bigkey（防止网卡流量、慢查询）
>    - String类型控制在10KB以内，hash、list、set、zset元素个数不要超过5000
>    - 反例：一个包含200玩个元素的list。
>    - 非字符串的bigkey，不要使用del删除，使用hscan、sscan、zscan方式渐进式删除，同时要注意防止bigkey过期时间自动删除问题（例如一个200万的zset设置1个小时过期，会触发del操作，造成阻塞，而且该操作不会出现在慢查询中（latency可查））
> 2. String和二级结构
>    1. **String是value，最大512MB但是>=10KB就是bigkey**
>    2. **list、hash、set和zset，个数超过5000就是bigkey**

`大key有哪些危害？`

> 1. 内存不均匀，集群迁移困难
> 2. 超时删除，大key删除作梗
> 3. 网络流量阻塞

`如何产生？`

> - 社交类
>   - EG：粉丝列表，典型案例粉丝逐步递增
> - 汇总统计
>   - EG：某个报表，月日年经年累月的积累’

`如何发现？`

> 1. redis-cli --bigkeys
>
>    1. ```bash
>       redis-cli -h 127.0.0.1 -p 6399 -a zzq121700 --bigkeys
>       redis-cli -h 127.0.0.1 -p 6399 -a zzq121700 --bigkeys -i 0.1
>       # 每隔100条scan指令就会休眠0.1s，ops（每秒操作数，Operations Per Second）就不会剧烈抬升，但是扫描的时间会变长
>       ```
>
>    2. 好处：给出每种数据结构Top 1 bigkey，同时给出每种数据类型的键值个数+平均大小
>
>    3. 不足：想查询大于10kb的所有key，--bigkeys参数就无能为力了，**需要用到memory usage来计算每个键值的字节数**。
>
>    4. ![image-20240226151151808](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240226151151808.png)
>
> 2. MEMORY USAGE 键
>
>    1. 计算每个键值的字节数
>    2. ![image-20240226151716405](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240226151716405.png)

`如何删除？`

> 1. 参考《阿里云Redis开发规范》
>    - 【强制】：拒绝bigkey（防止网卡流量、慢查询）
>    - String类型控制在10KB以内，hash、list、set、zset元素个数不要超过5000
>    - 反例：一个包含200玩个元素的list。
>    - 非字符串的bigkey，不要使用del删除，使用hscan、sscan、zscan方式渐进式删除，同时要注意防止bigkey过期时间自动删除问题（例如一个200万的zset设置1个小时过期，会触发del操作，造成阻塞，而且该操作不会出现在慢查询中（latency可查））
> 2. 官网：https://redis.com.cn/commands/scan.html
> 3. 普通命令
>    1. string：一般用del，如果过于庞大用unlink
>    2. hash：
>       1. 使用hscan每次获取少量field-value，再使用hdel删除每个field
>       2. 命令：HSCAN key cursor [ MATCH pattren ] [ COUNT count ]
>    3. list：
>       1. 使用ltrim渐进式逐步删除，直到全部删除完成
>       2. 命令：LTRIM key start stop
>    4. set：
>       1. 使用sscan每次获取部分元素，再使用srem命令删除每个元素
>       2. 命令：SSCAN set 0 =》SREM set key1 key2
>    5. zset：
>       1. 使用zscan每次获取部分元素，在使用ZREMRANGEBYRANK命令删除每个元素
>       2. 命令：ZREMRANGEBYRANK salary 0 1



## 2.4、BigKey生产调优

redis.conf配置文件LAZY FREEING相关说明

![image-20240226160804428](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240226160804428.png)

1. 阻塞和非阻塞删除命令
2. 优化配置
   1. ![image-20240226161639022](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240226161639022.png)
   2. ![image-20240226161733507](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240226161733507.png)





# 3、缓存双写一致性之更新策略探讨

## 3.1、面试题

`只要用缓存，就可能会涉及到redis缓存与数据库双存储双写，只要是双写，就一定会有数据一致性问题，那么你如何解决一致性问题？`

`双写一致性，你先动缓存Redis还是数据库MYSQL哪一个？为什么？`

`延时双删做过吗？会有哪些问题？`

`有这么一种情况，微服务查询redis无，mysql有，为了保证数据双写一致性回写Redis需要注意什么？双检加锁策略了解过吗？如何尽量避免缓存击穿？`

`redis和mysql双写100%会出现纰漏，做不到强一致性，如何保证最终一致性？`



## 3.2、双写一致性

**Redis中有数据**

需要和数据库中的值相同



**Redis中无数据**

数据库中的值是最新值，且准备回写Redis



**缓存按照操作来分**

1. 只读缓存
2. 读写缓存
   1. 同步直写策略
      - 写数据库后也同步写Redis缓存，缓存和数据库中的数据一致
      - 对于读写缓存来说，要想保证缓存和数据库中的数据一致，就要采用同步直写策略
   2. 异步缓写策略
      - 正常业务运行中，MySql数据变动了，但是可以在业务上容许出现一定时间后才作用于Redis，比如仓库、物流系统
      - 异常情况出现了，不得不将失败的动作重新修补，有可能需要借助kafka或者RabbitMQ等消息中间件，实现重试重写



**双检加锁策略：**

**避免突然key失效了，打爆Mysql，做一下预防，尽量不要出现缓存击穿的情况**

多个线程同时去查询数据库的这条数据，那么我们可以在第一个查询数据的请求上**使用一个互斥锁来锁住它**。

其他的线程走到这一步拿不到锁就等着，等第一个线程查到了数据，然后做缓存。

后面的线程进来发现已经有缓存了，就直接走缓存。





## 3.3、数据库和缓存一致性的集中策略

目的：达到最终的一致性

给缓存设置过期时间，定期清理缓存并回写，是保证最终一致性的解决方案。

我们可以对存入缓存的数据设置过期时间，所有的写操作以数据库为准，对缓存操作只是尽最大努力即可。也就是说如果数据库写成功，缓存更新失败，那么只要达到过期时间，则后面的读请求自然会从数据库中读取新值然后回填缓存，达到一致性，**切记，要以为MYSQL的数据库写入库为准**。



可以停机的情况：

1. 挂牌报错，凌晨升级，温馨提示，服务降级
2. 单线程，这样重量级的数据操作最好不要多线程





四种更新策略：

1. 先更新数据库，再更新缓存（ X ）

2. 先更新缓存，再更新数据库（ X ）

3. 先删除缓存，再更新数据库（ X ）

   1. **采用延时双删策略**

   2. 双删方案面试题：

      1. 这个删除该休眠多久呢？

         - 线程A sleep的时间，就需要大于线程B读取数据再写入缓存的时间

         - 这个时间怎么确定？

           1. 第一种方法：在业务程序运行的时候，统计下线程读数据和写缓存的操作时间，自行评估自己的项目的读数据业务逻辑的耗时，以此为基础来进行估算。然后写数据的休眠的时间则在读数据业务逻辑的耗时基础上加**百毫秒**即可。

              这么做的目的，就是确保读请求结束，写请求可以删除读请求造成的缓存脏数据。

           2. 第二种方法：新启动一个后台监控程序，例如看门狗

      2. 这中同步淘汰策略，吞吐量降低怎么办？

         - 将第二次删除作为异步的。自己起一个线程，异步删除
         - 这样，写的请求就不用沉睡一端时间后了，再返回。这么做，加大吞吐量

      3. 后续看门狗WatchDog源码分析

4. 先更新数据库，再删除缓存（ ！）

   1. 异常问题

      | 时间 |        线程A         |                  线程B                  |                   出现的问题                   |
      | :--: | :------------------: | :-------------------------------------: | :--------------------------------------------: |
      |  t1  |   更新数据库中的值   |                                         |                                                |
      |  t2  |                      | 缓存中理科命中，此时B读取的是缓存的旧值 | A还没来得及删除缓存的值，导致B缓存命中读到旧值 |
      |  t3  | 更新缓存的数据，over |                                         |                                                |

      **先更新数据库，再删除缓存：**假如缓存杀出失败或者来不及，导致请求再次访问Redis时缓存命中，**读到的却是缓存旧值**。

   2. 业务指导思想

      1. 微软云：[英文](https://learn.microsoft.com/en-us/azure/architecture/patterns/cache-aside)、[中文](https://learn.microsoft.com/zh-cn/azure/architecture/patterns/cache-aside)
      2. 阿里巴巴的canal

   3. 解决方案

      ![image-20240226225623872](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240226225623872.png)

      | 流程如下所示：                             |
      | ------------------------------------------ |
      | 1.更新数据库数据                           |
      | 2.数据库会将操作信息写入**binlog日志**当中 |
      | 3.订阅程序提取出所有需要的数据以及key      |
      | 4.另外起一段非业务代码，获取该信息         |
      | 5.常识删除缓存操作，发现删除失败           |
      | 6.将这些信息发送至消息队列                 |
      | 7.重新从消息队列中获得该数据，重试操作     |

      1. 可以把要删除的缓存值或者要更新的数据库值暂存到消息队列中（例如使用Kafka或者RabbitMQ等）。
      2. 当程序没有能够成功地删除缓存值或者是更新数据库值时，可以从消息队列中重新取这些值，然后再次进行删除或更新。
      3. 如果能够陈工地删除或更新，我们就要把这些值从消息队列中去除，以免重复操作，此时，我们也可以保证数据库和缓存的数据一致了，否则还需要再次进行重试。
      4. 如果重试超过一定次数后还是没有成功，我们就需要想业务层发送报错信息了，通知运维人员。

   4. 类似经典的分布式事务问题，只有一个权威答案

      - 最终一致性：
        - 流量充值、先发下短信实际充值可能滞后五分钟
        - 电商发货，短信下发但是物流明天见



小总结：

- 如何选择方案？利弊如何

  - 优先**使用先更新数据库，再删缓存的方案（先更库 -> 后删除）**。

    理由：

    1. 先删除缓存值再更新数据库，有可能导致请求因缓存缺失而访问数据库，给数据库带来压力导致打满mysql
    2. 如果业务应用中读取数据库和写缓存的时间不好估算，那么，延迟双删中的等待时间不好设置

  - 如果使用先更新数据库， 再删除缓存的方案：

    如果业务层要求必须读取一致性的数据，那么我们就需要再更新数据库时，先在Redis缓存客户端暂停并发读写请求，等数据库更新完了、缓存值删除后，再读取数据，从而保证数据一致性，这是理论可以达到的效果，但实际，不推荐，因为真实生产环境中，分布式下很难做到实时一致性，**一般都是最终一致性**。

- |             策略             | 高并发多线程条件下 |                     问题                     |                             现象                             |                     解决方案                      |
  | :--------------------------: | :----------------: | :------------------------------------------: | :----------------------------------------------------------: | :-----------------------------------------------: |
  | 先删除redis缓存，在更新mysql |         无         |         缓存删除成功但数据库更新失败         |                  Java程序从数据库中读到旧值                  |               再次更新数据库，重试                |
  |                              |         有         | 缓存删除成功但数据库更新中......有并发读请求 | 并发请求从数据库读到旧值并写回Redis，导致后续都是从redis读取到的旧值 |                     延迟双删                      |
  | 先更新mysql，再删除redis缓存 |         无         |        数据库更新成功，但缓存删除失败        |                  Java程序从Redis中读到旧值                   |                再次删除缓存，重试                 |
  |                              |         有         | 数据库更新成功但缓存删除中......有并发读请求 |                    并发请求从缓存读到旧值                    | 等待Redis删除完成，这段时间有数据不一致，短暂存在 |

  



# 4、Redis与MySQL数据双写一致性工程落地案例

## 4.1、面试题

`mysql有记录改动了（有增删改查操作），立刻同步反应到redis？？该如何做？`

> 有一种技术，能够监听到mysql的变动且能够通知redis =》 canal（阿里巴巴）



## 4.2、canal

**是什么？**

官网：[canal](https://github.com/alibaba/canal/wiki)

![image-20240227221028515](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227221028515.png)

canal，中文翻译为 水道、管道、沟渠、运河，主要用途是用于Mysql数据库增量日志数据的订阅、消费和解析，是阿里巴巴开发并开源，采用Java语言开发；

历史背景是早期阿里巴巴因为杭州和美国双机房部署，存在跨机房数据同步的业务需求，实现主要是基于业务trigger（触发器）获取增量变更。从2010年开始，阿里巴巴逐步尝试采用解析数据库日志获取增量变更进行同步，由此衍生出了canal项目；



**能干嘛？**

- 数据库镜像
- 数据库实时备份
- 索引构建和实时维护（拆分异构索引、倒排索引等）
- 业务cache刷新
- 带业务逻辑的增量数据处理



下载地址：https://github.com/alibaba/canal/releases



## 4.3、工作原理

### 4.3.1、传统的MySQL主从复制工作原理

![image-20240227222158526](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227222158526.png)



MySQL的主从复制将讲过如下步骤：

1. 当master主服务器上的数据发生改变时，则将其改变写入二进制事件日志文件中（binlog）；
2. slave从服务器会在一定时间间隔内对master主服务器上的二进制日志进行探测，探测其是否发生过改变，如果探测到master主服务器的二进制日志发生了改变，则开始一个I/O Thread请求master二进制事件日志；
3. 同时master主服务器为每个I/O Thread启动一个dump Thread，用于向其发送二进制事件日志；
4. slave从服务器将接收到的二进制事件日志保存在自己至自己本地的中继日志文件中；
5. slave从服务器将启动SQL Thread从中继日志中读取二进制日志，在本地重放，使得其数据和主服务器保持一致；



### 4.3.2、canal工作原理

1. canal模拟MySQL的交互协议，伪装自己为MySQL slave，向MySQL master发送dump协议
2. MySQL master收到dump请求，开始推送binary log给slave（即canal）
3. canal解析binary log对象（原始为byte流）





## 4.4、mysql-canal-redis双写一致性coding

### 4.4.1、来源出处

![image-20240227223951592](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227223951592.png)

JAVA：[JavaDemo](https://github.com/alibaba/canal/wiki/ClientExample)



### 4.4.2、MYSQL

- 查看mysql版本

  - ![image-20240227224550542](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227224550542.png)

- 当前的主机二进制日志 SHOW MASTER STATUS;

  - ![image-20240227224626089](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227224626089.png)

- 查看SHOW VARIABLES LIKE 'log_bin';

  - ![image-20240227224533003](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227224533003.png)

- 开启MySQL的binlog写入功能

  - 要是binlog日志的value是off，则打开安装目录下的my.ini

  - 在mysqld下添加

    - ```bash
      log-bin=mysql-bin #	开启binlog
      binlog-format=ROW # 选择ROW模式
      server_id=1 # 配置MySQL replaction需要定义，不要和canal的slaveID重复
      ```

      ROW模式：除了记录sql语句之外，还会记录每个字段的变化情况，能够清楚的记录每行数据的变化历史，但会占用较多的空间；

      STATEMENT模式：只记录sql语句，但是没有记录上下文信息，在进行数据恢复的时候可能会导致丢失情况；

      MIX模式：比较灵活的记录，理论上说当遇到了表结构变更的时候，就会记录为statement模式。当遇到数据更新或者删除情况下就会变成ROW模式；

- 重启mysql

- 再次查看SHOW VARIABLES LIKE 'log_bin';

- 授权canal连接MySQL账号

  - mysql默认的用户在mysql库的user表里

    ![image-20240227230009964](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227230009964.png)

  - 默认没有canal账户，此处新建+授权

    - ```mysql
      DROP USER IF EXISTS 'canal'@'%';
      CREATE USER 'canal'@'%' IDENTIFIED BY 'canal';
      GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' WITH GRANT OPTION;
      FLUSH PRIVILEGES;
      
      SELECT * FROM mysql.user;
      ```

    - ![image-20240227230706555](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240227230706555.png)



### 4.4.4、canal服务端

下载：https://github.com/alibaba/canal/releases/tag/canal-1.1.7

把canal下载到linux服务器上：

```shell
wget https://github.com/alibaba/canal/releases/download/canal-1.1.7/canal.deployer-1.1.7.tar.gz
```

解压：

```shell
tar -zxvf canal.deployer-1.1.7.tar.gz
```

配置：

修改/mycanal/conf/example路径下instance.properties文件

换成自己的mysql主机master的ip地址

```mysql
select SUBSTRING_INDEX(host,':',1) as ip , count(*) from information_schema.processlist group by ip;
```

换成自己在mysql新建的canal账户

![image-20240228000249381](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228000249381.png)

![image-20240228000918612](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228000918612.png)

启动canal：

bin目录下 

```shell
./startup.sh 
```

logs目录下的canal下的canal.log：

```shell
cat canal.log
```

![image-20240228001601501](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228001601501.png)





### 4.4.5、canal客户端（Java编写业务程序）

**SQL脚本：**

```sql
CREATE TABLE `user_images` (
  `id` int NOT NULL COMMENT '头像主键',
  `username` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名',
  `data` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

**建Module：**



**改POM：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com</groupId>
    <artifactId>Redis02_demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Redis02_demo</name>
    <description>Redis02_demo</description>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.10</version>
    </parent>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.16.18</lombok.version>
        <mysql.version>5.1.47</mysql.version>
        <druid.version>1.1.16</druid.version>
        <mapper.version>4.1.5</mapper.version>
        <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
    </properties>

    <dependencies>
        <!-- canal -->
        <dependency>
            <groupId>com.alibaba.otter</groupId>
            <artifactId>canal.client</artifactId>
            <version>1.1.0</version>
        </dependency>
        <!--SpringBoot 通用依赖模块-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <!-- springboot 与 AOP -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
        </dependency>
        <!-- Mysql 数据库驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        <!-- springboot集成druid连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${druid.version}</version>
        </dependency>
        <!-- mybatis和SpringBoot整合 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.spring.boot.version}</version>
        </dependency>
        <!-- persistence -->
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>persistence-api</artifactId>
            <version>1.0.2</version>
        </dependency>
        <!-- 通过Mapper -->
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
            <version>${mapper.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        <!-- jedis -->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>3.8.0</version>
        </dependency>
        <!--        &lt;!&ndash; lettuce &ndash;&gt;-->
        <!--        <dependency>-->
        <!--            <groupId>io.lettuce</groupId>-->
        <!--            <artifactId>lettuce-core</artifactId>-->
        <!--            <version>6.2.1.RELEASE</version>-->
        <!--        </dependency>-->

        <!-- SpringBoot 与Redis整合依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <!-- swagger2 -->
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

        <!-- 通用基础配置 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.2.3</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.26</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.26</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
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
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**写Properties：**

```properties
server.port=8080


spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/blog?characterEncoding=UTF8&autoReconnect=true&serverTimezone=Asia/Shanghai&allowMultiQueries=true&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
```

**主启动：**





**业务类：**

```java
package com.redis02_demo.utils;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
    public static final String REDIS_IP_ADDR = "192.168.129.135";
    public static final String REDIS_PWD="zzq121700";
    public static JedisPool jedisPool;
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig,REDIS_IP_ADDR,6399,10000,REDIS_PWD);
    }
    public static Jedis getJedis() throws Exception {
        if(null!= jedisPool){
            return jedisPool.getResource();
        }
        throw new Exception("JedisPool is not ok");
    }
}
```

```java
package com.redis02_demo.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.redis02_demo.utils.RedisUtils;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisCanalClientExample {
    public static final Integer _60SECONDS = 60;

    public static final String REDIS_IP_ADDR = "192.168.129.135";

    private static void redisInsert(List<Column> columns){
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + " insert = " + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }

        if (columns.size() > 0) {
            try (Jedis jedis = RedisUtils.getJedis()) {
                jedis.set(columns.get(0).getValue(), jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void redisDelete(List<Column> columns){
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + " delete = " + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }

        if (columns.size() > 0) {
            try (Jedis jedis = RedisUtils.getJedis()) {
                jedis.del(columns.get(0).getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void redisUpdate(List<Column> columns){
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + " update = " + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }

        if (columns.size() > 0) {
            try (Jedis jedis = RedisUtils.getJedis()) {
                jedis.set(columns.get(0).getValue(), jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void printEntry(List<Entry> entries){
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString()，e);
            }
            EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    redisDelete(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    redisInsert(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------&gt; before");
                    redisUpdate(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("-----------初始化main方法--------------");

        //  创建canal服务端
        CanalConnector connector =
                CanalConnectors.newSingleConnector(
                        new InetSocketAddress(REDIS_IP_ADDR,11111)
                ,"example","","");

        int batchSize = 1000;
        //  空闲空转计数器
        int emptyCount = 0;
        System.out.println("--------------canal init ok 开始监听mysql变化-------------");
        try {
            connector.connect();
            //  所有库的所有表
            connector.subscribe(".*\\..*");
            //  哪个库的那个表
            connector.subscribe("blog.images");
            connector.rollback();

            int totalEmptyCount = 10 * _60SECONDS;
            while(emptyCount < totalEmptyCount){
                System.out.println("我是canal，每一秒正在监听："+ UUID.randomUUID().toString());
                Message message = connector.getWithoutAck(batchSize);//获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if(batchId == -1 || size ==0){
                    emptyCount++;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }else{
                    //计数器重新置零
                    emptyCount = 0;
                    printEntry(message.getEntries());
                }
                connector.ack(batchId);//确认提交
                //  connector.rollback(batchId);// 处理失败，回滚数据
            }
            System.out.println("已经监听了"+totalEmptyCount+"秒，无任何消息，请重启重试");
        }finally {
            connector.disconnect();
        }
    }
}
```

![image-20240228141720047](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228141720047.png)

**效果：**

Redis缓存中只有两条数据

![image-20240228012718367](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228012718367.png)

数据库没有数据

![image-20240228012741232](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228012741232.png)

启动RedisCanalClientExample

开始监听

![image-20240228012828744](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228012828744.png)

当修改数据库的时候

![image-20240228012854329](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228012854329.png)

监听到有人修改了数据库

![image-20240228012907930](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228012907930.png)

查看Redis，Redis也被回写了

![image-20240228012950284](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228012950284.png)

更改数据库

也监听到数据库被修改

![image-20240228013022055](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228013022055.png)

删除也一样，Redis也会被删除

![image-20240228013104954](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240228013104954.png)





# 5、案例落地实战BitMap/HyperLogLog/GEO

## 5.1、面试题

`抖音电商直播，主播介绍的商品有评论，1个商品对应了1系列的评论，排序+展现+提前10条记录`

`用户在手机APP上的签到打卡信息：1天对应1系列用户的签到记录，新浪微博、钉钉打卡签到，来没来如何统计？`

`应用网站上的网页访问信息：1个页面对应1系列的访问点击，淘宝首页，每天多少人浏览首页？`

`你们公司系统上线后，说一下UV（独立访客，Unique Visitor），PV，DAU分别是多少？`



## 5.2、统计的类型有哪些？

**亿级系统中常见的四种统计：**

1. 聚合统计

   1. 统计多个集合元素的聚合结果，**交差并集集合统计**
   2. 命令
   3. 交并差集和聚合函数的应用

2. 排序统计

   1. 抖音短视频最新评论留言的场景，设计一个展现列表
   2. 案例，回答思路
      - 以抖音vcr最新的浏览评价为案例，所有评论需要两个功能，按照时间排序（正序，反序）+分页显示
      - 能够排序+分页显示的redis数据结构是什么合适？
      - answer
        - zset
        - 在面对需要展示最新列表、排行榜等场景时，如果数据更新拼盘或者需要分页显示，建议使用ZSet

3. 二值统计

   1. 集合元素的取值就只有0和1两种。

      在钉钉上班签到打卡的场景中，我们只用记录有签到（1）或者没签到（0）

   2. bitmap

4. 基数统计

   1. 统计一个集合中**不重复的元素个数**
   2. hyperloglog





## 5.3、HyperLogLog

> 1. 什么是UV？
>
>    Unique Visitor、独立访客，一般理解为客户端IP
>
>    **需要去重考虑**
>
> 2. 什么是PV？
>
>    Page View，页面浏览量
>
>    不用去重
>
> 3. 什么是DAU？
>
>    Daily Active User
>
>    日活跃用户量：**登录或者使用了某个产品的用户数（去重复登录的用户）**
>
>    常用于反映网站、互联网用用或者网络游戏的运营情况
>
> 4. 什么是MAU？
>
>    Monthly Active User
>
>    月活跃用户量



**看需求**

很多计数类场景，比如 每日注册IP数、每日访问IP数、页面实时访问数PV、访问用户数UV等。

因为很多主要的目标高效、巨量地进行技术，所以对存储的数据的内容并不太关心。

也就是说它只能用于统计巨量数量，不太涉及具体的统计对象的内容和精准性。



统计单日一个页面的访问量（PV），单次访问就算一次。

统计单日一个页面的用户量（UV），即按照用户为维度计算，单个用户一天内多次访问也只算一次。

多个key的合并统计，某个门户网站的所有模块的PV聚合统计就是整个网站的总PV。



**需求**

- UV的统计需要去重，一个用户一天内的多次访问只能算作一次
- 淘宝、天猫首页的UV，平均每天是1~1.5个亿左右
- 每天存1.5个亿的IP，访问者来了后先去查是否存在，不存在加入



**方案讨论**

- 用MySQL（X）
- 用redis的hash结构存储（内存太高）
- hyperloglog



**HyperLogLogServer**

```java
package com.redis02_demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HyperLogLogService {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 模拟后台有用户点击淘宝首页，每个用户来自不同的IP地址
     */
    @PostConstruct
    public void initIP() {

        new Thread(() -> {
            String IP = null;
            for (int i = 0; i < 200; i++) {
                Random random = new Random();
                IP =
                        random.nextInt(256) + "."
                                + random.nextInt(256) + "."
                                + random.nextInt(256) + "."
                                + random.nextInt(256);

                Long hll = redisTemplate.opsForHyperLogLog().add("hll", IP);
                log.info("ip={},该IP地址访问首页的次数{}", IP, hll);
                //  暂停三秒钟
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();
    }


    public Long uv() {
        return redisTemplate.opsForHyperLogLog().size("hll");
    }

}
```



**HyperLogLogController**

```java
package com.redis02_demo.controller;

import com.redis02_demo.service.HyperLogLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "淘宝亿级UV的Redis统计方案")
@RestController
@Slf4j
public class HyperLogLogController {

    @Resource
    HyperLogLogService hyperLogLogService;


    @ApiOperation("获得IP去重后的uv统计访问量")
    @RequestMapping(value = "/uv",method = RequestMethod.GET)
    public long uv(){
        return hyperLogLogService.uv();
    }

}
```



## 5.4、GEO

**Service：**

```java
package com.redis02_demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class GEOService {
    public static final String CITY = "city";

    @Resource
    private RedisTemplate redisTemplate;

    public String geoAdd() {
        Map<String,Point> map = new HashMap<>();
        map.put("天安门",new Point(116.403963,39.915119));
        map.put("故宫",new Point(116.403414,39.924091));
        map.put("长城",new Point(116.024067,40.362639));
        redisTemplate.opsForGeo().add(CITY,map);
       return map.toString();
    }

    public Point position(String member){
        // 获取经纬度坐标,这里的member可以有多个，返回的list也可以有多个
        List<Point> list = redisTemplate.opsForGeo().position(CITY, member);
        return list.get(0);
    }

    public String hash(String member) {
        //GeoHash算法生成的base32编码值,这里的member可以有多个，返回的list也可以有多个
        List<String> list = redisTemplate.opsForGeo().hash(CITY,member);
        return list.get(0);
    }

    public Distance distance( String member1,String member2) {
        //获取两个给定位置之间的距离
        Distance distance = redisTemplate.opsForGeo().distance(CITY,member1,member2,RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance;
    }

    public GeoResults radiusByxy() {
        //通过经度，纬度查找附近的，北京王府井位置116.418017, 39.914402
        Circle circle = new Circle(116.418017, 39.914402, Metrics . KILOMETERS . getMultiplier());
        //返@50条:
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults=
                this.redisTemplate.opsForGeo().radius(CITY, circle, args);
        return geoResults;
    }

    public GeoResults radiusByMember() {
        //通过地方查找附近
        String member="天安门";
        //返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates( ).sortAscending().limit(5) ;
        //半径10公里内
        Distance distance=new Distance(10,Metrics.KILOMETERS);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults= this.redisTemplate.opsForGeo().radius(CITY,member,distance,args);
        return geoResults;
    }
}
```

**Controller：**

```java
package com.redis02_demo.controller;

import com.redis02_demo.service.GEOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "美团地图位置附近的酒店推送GEO")
@RestController
@Slf4j
public class GEOController {

    @Resource
    private GEOService geoService;


    @ApiOperation("添加坐标GeoADD")
    @GetMapping("/geoAdd")
    public String geoADD(){
        return geoService.geoAdd();
    }

    @ApiOperation("获取经纬度坐标GEO")
    @GetMapping("/geoPos")
    public Point position(String member){
        return geoService.position(member);
    }


    @ApiOperation("获取经纬度生成的base32编码值geoHash")
    @GetMapping("/geoHash")
    public String hash(String member){
        return geoService.hash(member);
    }

    @ApiOperation("获取两个给定位置之间的距离")
    @GetMapping("/geoDist")
    public Distance distance(String member1 ,String member2){
        return geoService.distance(member1,member2);
    }


    @ApiOperation("通过经纬度查找北京王府井附近的")
    @GetMapping("/geoRadius")
    public GeoResults radiusByXY(){
        return geoService.radiusByXY();
    }

    @ApiOperation("通过地方查找附近，本例写死天安门作为地址")
    @GetMapping("/geoRadiusByMember")
    public GeoResults radiusByMember(){
        return geoService.radiusByMember();
    }
}
```





## 5.5、BitMap

- 日活统计
- 连续签到打卡
- 最近一周的活跃用户
- 统计指定用户一年之中的登录天数
- 某用户按照一年365天，哪几天登陆过？哪几天没有登陆？全年登陆多少天？

![image-20240229221247171](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240229221247171.png)



**小厂解决方法：MYSQL**

```mysql
CREATE TABLE user_sign
keyid BIGINT NOT NULL PRIMARY KEY AUTO INCREMENT,
user_key VARCHAR(200), #京东用户ID
sign_date DATETIME,#签到日期(20220618)
sign_count INT #连续签到天数

INSERT INTO user_sign(user_key,sign_date,sign_count)
VALUES ('28216618-XXXX-XXXX-XXXX-XXXXXXXXXXXX','2022-06-18 15:11:12',1);
        
SELECT
	sign_count
FROM
	user_sign
WHERE 
	user_key =20216618-XXXX-XXXX-XXXX-XXXXXXXXXXXX 
	AND sign date BETWEEN '2020-06-17 00:00:00' AND '2020-06-18 23:59:59'
ORDER BY
	sign_date DESC
	LIMIT 1;
```

**困难和解决思路**

方法正确但是难以落地实现。 签到用户量较小时这么设计能行，但京东这个体量的用户(估算3000W签到用户，一天一条数据，一个月就是9亿数据)对于京东这样的体量，如果一条签到记录对应着当日签到记录，那会很恐怖......

如何解决这个痛点?

1. 一条签到记录对应一条记录，会占据越来越大的空间。
2. 一个月最多31天，刚好我们的int类型是32位，那这样一个int类型就可以搞定一个月，32位大于31天，当天来了就是1没来就是0。
3. 一条数据直接存储一个月的签到记录，不再是存储一天的签到记录。



**大厂方法，基于Redis的Bitmap实现签到日历**

建表-按位-redis-bitmap

在签到统计时，每个用户一天的签到用1个bit位就能表示

一个月(假设是31天)的签到情况用31个bit位就可以，一年的签到也只需要用365个bit位，根本不用太复杂的集合类型





# 6、布隆过滤器BloomFilter

## 6.1、面试题

`现有50亿个电话号码，现有10万个电话号码，如何要快速准确的判断这些电话号码是否已经存在？`

`判断是否存在，布隆过滤器了解过吗？`

`安全连接网址，全球数10亿的网址判断`

`命令按校验，识别垃圾邮件`

`白名单校验，识别出合法用户进行后续处理`



## 6.2、是什么

由一个初值为零的bit数组和多个哈希函数构成，用来快速判断集合中是否存在某个元素

目的：减少内存占用

方式：不保存数据信息，只是在内存中做一个是否存在标记的flag

本是是判断具体数据是否存在于一个大的集合中



## 6.3、能干嘛？

高效地插入和查询，占用空间少，返回的结果是不确定性+不够完美

布隆过滤器可以添加元素，但是**不能删除元素**，由于设计hashcode判断依据，删掉元素会导致误判率增加

**重点：一个元素如果判断结果存在时，元素不一定存在，但是判断为不存在时，则一定不存在。**

**小总结：**

1. 有，是可能有

2. 无，是肯定无

   可以保证的是，如果布隆过滤器判断一个元素不在一个集合中，那这个元素一定不在集合中
   
3. 使用时最好不要让实际元素数量远大于初始化数量，一次给够避免扩容

4. 当实际元素数量超过初始化数量时，应该对布隆过滤器进行重建，重新分配一个size更大的过滤器，再讲所有的历史元素批量add进去





## 6.4、布隆过滤器原理

> **布隆过滤器原理**
>
> 布隆过滤器（Bloom Filter）是一种专门用来解决去重问题的高级数据结构。
>
> 实质就是**一个大型*位数组*和几个不同的无偏hash函数**（无偏表示分布均匀）。由一个初值都为零的bit数组和多个哈希函数构成，用来快速判断某个数据是否存在。但是跟HyperLogLog一样，他也一样有那么一点不精确，也存在一定的误判概率



使用3步骤：

1. 初始化BitMap
   - 布隆过滤器，本质上是由长度为m的位向量或为列表（仅包含0或1位值的列表）组成，最初所有的值均设置为0
2. 添加占坑位
   - 当我们向布隆过滤器中添加数据时，为了尽量地址不冲突，**会使用多个hash函数对key进行运算**，算得一个下标索引值，然后对位数组长度进行**取模**得到一个位置，每个hash函数都会算得一个不同的位置。再把位数组这几个位置都设置为1就完成了add操作。
3. 判断是否存在
   - 向布隆过滤器查询某个key是否存在时，先把这个key通过相同的**多个hash函数进行运算**，查看对应的位置是否都为1，**只要有一个位为零，那么说明布隆过滤器中这个key不存在**；
   - **如果这几个位置全都是1，那么说明极有可能存在**；





## 6.5、布隆过滤器的使用场景

1. 解决缓存穿透的问题，和redis的bitmap结合使用
2. 黑名单校验、识别垃圾邮件
3. 安全连接网址，全球上十亿的网址判断



**缓存穿透是什么？**

一般情况下，先查询缓存redis是否有该条数据，缓存中没有时，在查询数据库。

当数据库也不存在该条数据时，每次查询都要访问数据库，这就是缓存穿透。

缓存穿透带来的问题是，当有大量请求查询数据库不存在的数据时，就会给数据库带来压力，甚至会拖垮数据库。



**可以使用布隆过滤器解决缓存穿透的问题**

把已存在数据的key存在布隆过滤器中，相当于redis前面挡着一个布隆过滤器。

当有有新的请求时，先到布隆过滤器中查询是否存在；

如果布隆过滤器中不存在该条数据则直接返回；

如果不弄过滤器中已存在，才去查询缓存redis，如果redis里没有查询到则查询MySql数据库



**黑名单校验，识别垃圾邮件**

发现存在黑名单中的，就执行特定操作。比如：识别垃圾邮件，只要是邮件在黑名单中的邮件，就识别为垃圾邮件。

假设黑名单的数量是数以亿计的，存放起来就是非常耗费存储空间的，布隆过滤器则是一个较好的解决方案。把所有黑名单都放在布隆过滤器中，在收到邮件时，判断邮件地址时是否在布隆过滤器中即可。





## 6.6、手写布隆过滤器

> SpringBoot+Redis+mybatis案例基础与一键编码环境整合
>
> MyBatis通用Mapper4
>
> mybatis-generator ： http://mybatis.org/generator/
>
> MyBatis通用Mapper4官网 ： https://github.com/abel533/Mapper

**t_customer用户表SQL**

```mysql
CREATE TABLE `t_customer` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `cname` varchar(50) NOT NULL,
  `age` int(10) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `sex` tinyint(4) NOT NULL,
  `birth` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_cname` (`cname`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
```

**建立SpringBoot项目**

**改POM**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.10</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com</groupId>
    <artifactId>Redis03_bloom</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Redis03_bloom</name>
    <description>Redis03_bloom</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <java.version>1.8</java.version>
        <hutool.version>5.5.8</hutool.version>
        <druid.version>1.1.18</druid.version>
        <mapper.version>4.1.5</mapper.version>
        <pagehelper.version>5.1.4</pagehelper.version>
        <mysql.version>5.1.39</mysql.version>
        <swagger2.version>2.9.2</swagger2.version>
        <swagger-ui.version>2.9.2</swagger-ui.version>
        <mybatis.spring.version>2.1.3</mybatis.spring.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- mybatis 通用mapper tk单独使用，自己到这版本号 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>
        <!-- mybatis-spring -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.spring.version}</version>
        </dependency>
        <!-- mybatis Generator -->
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.4.0</version>
            <scope>compile</scope>
            <optional>true</optional>
        </dependency>
        <!-- 通用mapper -->
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
            <version>${mapper.version}</version>
        </dependency>
        <!-- persistence -->
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>persistence-api</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
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
                <version>1.3.6</version>
                <configuration>
                    <configurationFile>${basedir}/src/main/resources/generatorConfig.xml</configurationFile>
                    <overwrite>true</overwrite>
                    <verbose>true</verbose>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>${mysql.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>tk.mybatis</groupId>
                        <artifactId>mapper</artifactId>
                        <version>${mapper.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
```

**config.properties**

```properties
package.name = com.redis03_bloom

jdbc.driverClass = com.mysql.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/mybatis
jdbc.user = root
jdbc.password = 123456
```

**generatorConfig.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <properties resource="config.properties"/>

    <context id= "Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat" >
        <property name="beginningDelimiter" value="`" />
        <property name="endingDelimiter" value="`" />

        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value= "tk.mybatis.mapper.common.Mapper" />
            <property name="caseSensitive" value="true" />
        </plugin>

        <jdbcConnection driverClass="${jdbc.driverClass}"
                        connectionURL="${jdbc.url}"
                        userId="${jdbc.user}"
                        password="${jdbc.password}">
        </jdbcConnection>

        <javaModelGenerator targetPackage="${package.name}.entities" targetProject="src/main/java" />
        <sqlMapGenerator targetPackage="${package.name}.mapper" targetProject="src/main/java" />
        <javaClientGenerator targetPackage="${package.name}.mapper" targetProject="src/main/java" type="XMLMAPPER" />

        <table tableName="t_customer" domainObjectName="Customer">
            <generatedKey column="id" sqlStatement="JDBC"/>
        </table>
    </context>
</generatorConfiguration>
```

**主启动**

```java
package com.redis03_bloom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.redis03_bloom.mapper")
public class Redis7StudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(Redis7StudyApplication.class, args);
    }
}
```

**业务类**

- CustomerService

  ```java
  package com.redis03_bloom.service;
  
  import com.redis03_bloom.entities.Customer;
  import com.redis03_bloom.mapper.CustomerMapper;
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.data.redis.core.RedisTemplate;
  import org.springframework.stereotype.Service;
  
  import javax.annotation.Resource;
  import java.util.concurrent.TimeUnit;
  
  @Service
  @Slf4j
  public class CustomerService {
  
      public static final String CACHE_KEY_CUSTOMER = "customer:";
  
      @Resource
      private CustomerMapper customerMapper;
      @Autowired
      private RedisTemplate redisTemplate;
  
      public void addCustomer(Customer customer) {
          int i = customerMapper.insertSelective(customer);
          if (i > 0) {
              // mysql插入成功，需要重新查询一次将数据捞出来，写进Redis
              Customer result = customerMapper.selectByPrimaryKey(customer.getId());
              // redis 缓存key
              String key = CACHE_KEY_CUSTOMER + result.getId();
              redisTemplate.opsForValue().set(key, result);
          }
      }
  
      public Customer findCustomerById(Integer customerId) {
          Customer customer = null;
          // 缓存redis的key名称
          String key = CACHE_KEY_CUSTOMER + customerId;
          // 查看redis是否存在
          customer = (Customer) redisTemplate.opsForValue().get(key);
  
          // redis 不存在，取MySQL中查找
          if (null == customer) {
              // 双端加锁策略
              synchronized (CustomerService.class) {
                  customer = (Customer) redisTemplate.opsForValue().get(key);
                  if (null == customer) {
                      customer = customerMapper.selectByPrimaryKey(customerId);
                      if (null == customer) {
                          // 数据库没有放入redis设置缓存过期时间
                          redisTemplate.opsForValue().set(key, customer, 60, TimeUnit.SECONDS);
                      } else {
                          //	把mysql查询出来的数据回写redis，保持一致性
                          redisTemplate.opsForValue().set(key, customer);
                      }
                  }
              }
  
          }
          return customer;
      }
  }
  ```
```
  
- CustomerController

  ```java
  package com.redis03_bloom.controller;
  
  import com.redis03_bloom.entities.Customer;
  import  com.redis03_bloom.service.CustomerService;
  import io.swagger.annotations.Api;
  import io.swagger.annotations.ApiOperation;
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RestController;
  
  import java.time.LocalDateTime;
  import java.time.ZoneId;
  import java.util.Date;
  import java.util.Random;
  
  @Api(tags = "客户Customer接口+布隆过滤器讲解")
  @RestController
  @Slf4j
  public class CustomerController {
  
      @Autowired
      private CustomerService customerService;
  
      @ApiOperation("数据库初始化两条Customer记录")
      @PostMapping(value = "/customer/add")
      public void addCustomer() {
          for (int i = 0; i < 2; i++) {
              Customer customer = new Customer();
              customer.setCname("customer" + i);
              customer.setAge(new Random().nextInt(30) + 1);
              customer.setPhone("12332112312");
              customer.setSex((byte)new Random().nextInt(2));
              customer.setBirth(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
  
              customerService.addCustomer(customer);
          }
      }
  
      @ApiOperation("单个customer查询操作")
      @PostMapping(value = "/customer/{id}")
      public Customer findCustomerById(@PathVariable int id) {
          return customerService.findCustomerById(id);
      }
  
  }
  
```



新增布隆过滤器

- BloomFilterInit（白名单）：@PostConstruct初始化白名单数据

  ```java
  package com.redis03_bloom.filter;
  
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.data.redis.core.RedisTemplate;
  import org.springframework.stereotype.Component;
  
  import javax.annotation.PostConstruct;
  
  /**
   * 布隆过滤器白名单初始化工具类，又开始就设置一部分数据为白名单所有
   * 白名单业务默认规定：布隆过滤器有，Redis是极大可能有
   * 白名单：whitelistCustomer
   */
  @Component
  @Slf4j
  public class BloomFilterInit {
  
      @Autowired
      private RedisTemplate redisTemplate;
  
      @PostConstruct
      public void init() {
          // 1 白名单客户加载到布隆过滤器
          String key = "customer:12";
          // 2 计算hashvalue，由于存在计算出来负数的可能，需要取绝对值
          int hashValue = Math.abs(key.hashCode());
          // 3 通过hashValue和2^32取余，获得对应的下标坑位
          long index = (long) (hashValue % Math.pow(2, 32));
          log.info(key + "对应的坑位index：{}", index);
          // 4 设置Redis 里面的bitmap对应白名单类型的坑位，并设置为1
          redisTemplate.opsForValue().setBit("whitelistCustomer", index, true);
      }
  }
  ```

- CheckUtils

  ```java
  package com.redis03_bloom.utils;
  
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.data.redis.core.RedisTemplate;
  import org.springframework.stereotype.Component;
  
  @Component
  @Slf4j
  public class CheckUtils {
  
      @Autowired
      private RedisTemplate redisTemplate;
      
      public boolean checkWithBloomFilter(String checkItem, String key) {
          int hashValue = Math.abs(key.hashCode());
          long index = (long) (hashValue % Math.pow(2, 32));
          Boolean exitOk = redisTemplate.opsForValue().getBit(checkItem, index);
          log.info("---> key：{}对应坑位下标index：{}是否存在：{}", key, index, exitOk);
          return exitOk;
      }
  }
  ```

- CustomerService

  ```java
  /**
       * BloomFilter -> redis -> mysql
       * @param customerId
       * @return
       */
  public Customer findCustomerByIdWithBloomFilter(Integer customerId) {
      Customer customer = null;
      // 缓存redis的key名称
      String key = CACHE_KEY_CUSTOMER + customerId;
  
      // 布隆过滤器check
      if (!checkUtils.checkWithBloomFilter("whitelistCustomer", key)) {
          log.info("白名单无此顾客，不可以访问，{}", key);
          return null;
      }
  
      // 查看redis是否存在
      customer = (Customer) redisTemplate.opsForValue().get(key);
      // redis 不存在，取MySQL中查找
      if (null == customer) {
          // 双端加锁策略
          synchronized (CustomerService.class) {
              customer = (Customer) redisTemplate.opsForValue().get(key);
              if (null == customer) {
                  customer = customerMapper.selectByPrimaryKey(customerId);
                  if (null == customer) {
                      // 数据库没有放入redis设置缓存过期时间
                      redisTemplate.opsForValue().set(key, customer, 60, TimeUnit.SECONDS);
                  } else {
                      redisTemplate.opsForValue().set(key, customer);
                  }
              }
          }
  
      }
      return customer;
  }
  ```

- CustomerController

  ```java
  @ApiOperation("BloomFilter, 单个customer查询操作")
  @PostMapping(value = "/customerBloomFilter/{id}")
  public Customer findCustomerByIdWithBloomFilter(@PathVariable int id) {
      return customerService.findCustomerByIdWithBloomFilter(id);
  }
  ```



## 6.7、布隆过滤器的优缺点

- 优点：高效地插入和查询，内存占用bit空间少
- 缺点：
  1. 不能删除元素。因为删掉元素会导致误判率增加，因为hash冲突同一个位置可能存的东西是多个共有的，删除一个元素的同时可能也把其他的删除了。
  2. 存在误判，不能进准过滤
     1. 有，是可能有
     2. 无，是肯定无





## 6.8、了解布谷鸟过滤器

为了解决布隆过滤器不能删除元素的问题，布谷鸟过滤器横空出世。



# 7、缓存预热 + 缓存雪崩 + 缓存击穿

## 7.1、面试题

`缓存预热、雪崩、穿透、击穿分别是什么？你遇到过哪几个情况？`

`缓存预热你是怎么做的？`

`如何避免或者减少缓存雪崩？`

`穿透和击穿有什么区别？它两是一个意思还是截然不同？`

`穿透和击穿你有什么解决方案？如何避免？`

`假如出现了缓存不一致,你有哪些修补方案？`





## 7.2、缓存预热

提前把数据存在redis

mysql有100条新纪录

1. 比较懒，什么都不做，对mysql做了数据新增， 利用redis的回写机制，让它逐步实现100条新增记录的同步。最好提前晚上部署发布版本的时候，由自己人提前做一次，让redis同步了，不要把这个问题留给客户。
2. 通过中间件或者程序自行完成。



@PostConstruct初始化白名单数据



## 7.3、缓存雪崩

- 发生：
  1. redis主机挂了，Redis全盘崩溃，偏硬件运维
  2. redis中有大量key同时过期大面积失效，偏软件开发
- **预防+解决**：
  1. redis中key设置为永不过期 OR 过期时间错开
  2. redis缓存集群实现高可用
     1. 主从 + 哨兵
     2. Redis Cluster（集群）
     3. 开启Redis持久化机制AOF/RDB，尽快恢复缓存集群
  3. 多缓存结合预防雪崩
     - ehcache本地缓存 + redis缓存
  4. 服务降级
     - Hystrix或者阿里sentinel限流&降级
  5. 充钱
     - 阿里云 - 云数据库Redis版



## 7.4、缓存穿透

**是什么**

1. 请求去查询一条记录，先查Redis无，后查mysql无，**都查询不到该条记录**，但是请求每次都会到打到数据库上面去，导致后台数据库压力暴增，这种现象我们称为缓存穿透，Redis就成了摆设。
2. 简单说就是，既不在Redis缓存库，也不再Mysql，数据库存在被多次暴击风险。

**解决**

缓存穿透  ->  恶意攻击 

​	空对象缓存、BloomFilter过滤器

- 方案一：空对象缓存或缺省值 ---- 回写增强

  Mysql也查不到的话也让Redis存入刚刚查不到的key并保护MySQL。第一次查询uid：abcdxxx，Redis和MySQL都没有，返回null给调用者，但是增强回写后第二次来查uid：abcdxxx，此时Redis就有值。可以直接从Redis中读取default缺省值返回给业务应用程序，避免了把大量请求发送给MySQL处理，打爆MySQL。

  **但是，此方法架不住黑客的恶意攻击，有缺陷，只能解决key相同的情况**

  黑客或者恶意攻击

  1. 黑客会对你的系统进行攻击，拿一个不存在的id去查询数据，会产生大量的请求到数据库去查询。可能会导致你的数据库由于压力过大而宕机。
  2. key相同打系统 -- 第一次打到mysql，空对象缓存后第二次就返回defaultNull缺省值，避免mysql被攻击，不用再到数据库中了
  3. **key不同打系统** -- 由于存在空对象和缓存回写，redis中的无光紧要的key也会越写越多（ **记得设置redis过期时间**）

- 方案二：Google布隆过滤器Guava解决缓存穿透

  1. Guava中布隆过滤器的实现算是比较权威的，所以实际项目中我们可以直接使用Guava布隆过滤器

  2. [Guava‘s BloomFilter源码出处](https://github.com/D1isney/guava/blob/master/guava/src/com/google/common/hash/BloomFilter.java)

  3. 白名单过滤器

     1. 白名单架构说明：白名单里面有的才让通过，没有直接返回，但是存在误判，由于误判率很小，1%的打到MySQL，可以接受。注意：所有key都需要往redis和bloomFilter里面放入

     2. 误判问题，但是概率小可以接受，不能从布隆过滤器删除

     3. 全部合法的key都需要放入Guava版本布隆过滤器+Redis里面，不然数据就是返回null

     4. **Coding实战**

        1. POM

           ```xml
           <dependency>
             <groupId>com.google.guava</groupId>
             <artifactId>guava</artifactId>
             <version>33.0.0-jre</version>
             <!-- or, for Android: -->
             <version>33.0.0-android</version>
           </dependency>
           ```

        2. BloomFilter

           ```java
           @Test 
           public void testGuava() { 
               // 1 创建Guava 版本布隆过滤器 
               BloomFilter bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 100); 
               // 2 判断指定的元素是否存在 
               System.out.println(bloomFilter.mightContain(1));// false
               System.out.println(bloomFilter.mightContain(2));// false 
               System.out.println(); 
               // 3 将元素新增进入布隆过滤器 
               bloomFilter.put(1);
               bloomFilter.put(2);
               System.out.println(bloomFilter.mightContain(1));// true
               System.out.println(bloomFilter.mightContain(2));// true 
           }
           ```

        3. **新建Guava案例** GuavaBloomFilterController

           ```java
           package com.redis03_bloom.controller;
           
           import com.redis03_bloom.service.GuavaBloomFilterService;
           import io.swagger.annotations.Api;
           import io.swagger.annotations.ApiOperation;
           import lombok.extern.slf4j.Slf4j;
           import org.springframework.beans.factory.annotation.Autowired;
           import org.springframework.web.bind.annotation.GetMapping;
           import org.springframework.web.bind.annotation.RestController;
           
           @Api(tags = "google工具Guava处理布隆过滤器")
           @RestController
           @Slf4j
           public class GuavaBloomFilterController {
           
               @Autowired
               private GuavaBloomFilterService guavaBloomFilterService;
           
               @ApiOperation("guava布隆过滤器插入100万样本数据并额外10万测试是否存在")
               @GetMapping("/guavafilter")
               public void guavaBloomFIlterService() {
                   guavaBloomFilterService.guavaBloomFilterService();
               }
           }
           ```

        4. **GuavaBloomFilterService**

           ```java
           package com.redis03_bloom.service;
           
           import com.google.common.hash.BloomFilter;
           import com.google.common.hash.Funnels;
           import lombok.extern.slf4j.Slf4j;
           import org.springframework.stereotype.Service;
           
           import java.util.ArrayList;
           
           @Service
           @Slf4j
           public class GuavaBloomFilterService {
           
               public static final int _1w = 10000;
               public static final int SIZE = 100 * _1w;
               // 误判率，它越小误判的个数也就越少
               public static double fpp = 0.03;
               // 创建Guava 版本布隆过滤器
               BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), SIZE, fpp);
           
               public void guavaBloomFilterService() {
                   // 1 先让bloomFilter加入100w白名单数据
                   for (int i = 0; i < SIZE; i++) {
                       bloomFilter.put(i);
                   }
                   // 2 故意取10w不在合法范围内的数据，来进行误判率演示
                   ArrayList<Integer> list = new ArrayList<>(10 * _1w);
                   // 3 验证
                   for (int i = SIZE; i < SIZE + (10 * _1w); i++) {
                       if(bloomFilter.mightContain(i)) {
                           log.info("被误判了：{}", i);
                           list.add(i);
                       }
                   }
                   log.info("误判的总数量：{}", list.size());
               }
           }
           // 运行之后，结果为： 误判的总数量：3033
           ```

           ![image-20240307143425638](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240307143425638.png)

  4. 黑名单使用

  

## 7.5、缓存击穿

**是什么**

1. 大量的请求同时查询一个key时，此时这个key正好失效了，就会导致导量的请求都打到数据库上面去
2. **简单来说就是热点key突然失效了，暴打了MySQL**
3. 穿透和击穿，截然不同
   1. **穿透**：从头到尾，至始至终都是没有的，**本来无一物，两库都没有**
   2. *击穿**：一切正常，一开始Redis有，mysql有，**突然出现高频访问的热点key失效**。这里有两种情况：1、自然过期时间的，突然时间到了，没了，全都打到mysql。2、delete老的key，换上新的key，del动作的时候，key突然失效了。



**危害**

- 会造成某一时刻数据库请求量过大，压力剧增。
- 一般技术部门需要知道**热点key是哪些个**？做到心里有数防止击穿



**解决**

缓存击穿  ->  热点key失效 -> 互斥更新、随机退避、差异失效时间

1. 热点key失效：
   1. 时间到了自然清除但还被访问到
   2. delete掉的key，刚巧又被访问
2. 方案一： 差异失效时间，对于访问频繁的热点key，干脆就不设置过期时间
3. **方案二：互斥更新，采用双检加锁策略**



**案例**

- 天猫聚划算功能实现+防止缓存击穿

- 模拟高并发的天马聚划算案例code

- 分析过程

  - 百分百高并发，绝对不可以用mysql实现
  - 先把mysql里面参加果冻的数据抽取进redis，一般采用定时器扫描来决定上线活动还是下线取消
  - 支持分页功能，一页20条记录

- **业务**

  - entity对象

    ```java
    package com.redis03_bloom.entities;
    
    import io.swagger.annotations.ApiModel;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value="聚划算活动Product信息")
    public class Product {
    
        // 产品id
        private Long id;
        // 产品名称
        private String name;
        // 产品价格
        private Integer price;
        // 产品详情
        private String detail;
    
    }
    ```

  - JHSTaskService：采用其那个时期将参数与聚划算活动的特价商品新增进redis中

    ```java
    package com.redis03_bloom.service;
    
    import com.redis03_bloom.entities.Product;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.stereotype.Service;
    
    import javax.annotation.PostConstruct;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;
    
    @Service
    @Slf4j
    public class JHSTaskService {
    
        public static final String JHS_KEY = "jhs";
        public static final String JHS_KEY_A = "jhs:a";
        public static final String JHS_KEY_B = "jhs:b";
    
        @Autowired
        private RedisTemplate redisTemplate;
    
        /**
         * 假设此处是从数据库读取，然后加载到redis
         * @return
         */
        private List<Product> getProductsFromMysql() {
            ArrayList<Product> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Random random = new Random();
                int id = random.nextInt(10000);
                Product product = new Product((long) id, "product" + i, i, "detail");
                list.add(product);
            }
            return list;
        }
    
        @PostConstruct
        public void initJHS() {
            log.info("模拟定时任务从数据库中不断获取参加聚划算的商品");
            // 1 用多线程模拟定时任务，将商品从数据库刷新到redis
            new Thread(() -> {
                while(true) {
                    // 2 模拟从数据库查询数据
                    List<Product> list = this.getProductsFromMysql();
                    // 3 删除原来的数据
                    redisTemplate.delete(JHS_KEY);
                    // 4 加入最新的数据给Redis参加活动
                    redisTemplate.opsForList().leftPushAll(JHS_KEY, list);
                    // 5 暂停1分钟，模拟聚划算参加商品下架上新等操作
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "t1").start();
        }
    }
    ```

  - JHSTaskController

    ```java
    package com.redis03_bloom.controller;
    
    import com.redis03_bloom.entities.Product;
    import io.swagger.annotations.Api;
    import io.swagger.annotations.ApiOperation;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.util.CollectionUtils;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    import java.util.List;
    
    @Api(tags = "模拟聚划算商品上下架")
    @RestController
    @Slf4j
    public class JHSTaskController {
    
        public static final String JHS_KEY = "jhs";
        public static final String JHS_KEY_A = "jhs:a";
        public static final String JHS_KEY_B = "jhs:b";
    
        @Autowired
        private RedisTemplate redisTemplate;
    
        @ApiOperation("聚划算案例，每次1页，每页5条数据")
        @GetMapping("/product/find")
        public List<Product> find(int page, int size) {
            long start = (page - 1) * size;
            long end = start + size - 1;
            List list = redisTemplate.opsForList().range(JHS_KEY, start, end);
            if (CollectionUtils.isEmpty(list)) {
                // todo Redis找不到，去数据库中查询
            }
            log.info("参加活动的商家: {}", list);
            return list;
        }
    }
    ```
  
- **Bug和隐患**

  1. 热点Key突然失效导致可怕的缓存击穿

     - delete命令执行的一瞬间有空隙，其他请求线程继续找Redis为null
     - 大量打到MySQL，被打穿了

  2. again

     | 缓存问题     | 产生原因               | 解决方案                               |
     | ------------ | ---------------------- | -------------------------------------- |
     | 缓存更新方式 | 数据变更、缓存时效性   | 同步更新、失效更新、异步更新、定时更新 |
     | 缓存不一致   | 同步更新失败、异步更新 | 增加重试、补偿任务、最终一致           |
     | 缓存穿透     | 恶意攻击               | 空对象缓存、BloomFilter                |
     | 缓存击穿     | 热点key失效            | 互斥更新、随机退避、差异失效时间       |
     | 缓存雪崩     | 缓存挂掉               | 快速失败熔断、主从模式、集群模式       |

  3. 最终目的：两条命令原子性还是其次，主要是**防止热Key突然师兄暴击MySQL打爆系统

- 进一步升级加固案例

  - 互斥更新，采用双检加锁策略

  - 差异失效时间

    - 如何解决缓存击穿
      - 1、新建：开辟两块缓存，主A从B，**先更新B再更新A**，严格按照这个顺序
      - 2、查询：先查询主缓存A，如果A没有（消失或者失效了）再查询从缓存B

  - JHStaskService

    ```java
    // 双缓存
    @PostConstruct
    public void initJHSAB() {
        log.info("模拟定时任务从数据库中不断获取参加聚划算的商品");
        // 1 用多线程模拟定时任务，将商品从数据库刷新到redis
        new Thread(() -> {
            while(true) {
                // 2 模拟从数据库查询数据
                List<Product> list = this.getProductsFromMysql();
                // 3 先更新B缓存且让B缓存过期时间超过A缓存，如果突然失效还有B兜底，防止击穿
                redisTemplate.delete(JHS_KEY_B);
                redisTemplate.opsForList().leftPushAll(JHS_KEY_B, list);
                // 设置过期时间为1天+10秒
                redisTemplate.expire(JHS_KEY_B, 86410L, TimeUnit.SECONDS);
                // 4 在更新缓存A
                redisTemplate.delete(JHS_KEY_A);
                redisTemplate.opsForList().leftPushAll(JHS_KEY_A, list);
                redisTemplate.expire(JHS_KEY_A, 86400L, TimeUnit.SECONDS);
                // 5 暂停1分钟，模拟聚划算参加商品下架上新等操作
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();
    }
    ```

  - JHSProductController

    ```java
    @ApiOperation("聚划算案例，AB双缓存，防止热key突然失效")
    @GetMapping("/product/findab")
    public List<Product> findAB(int page, int size) {
        List<Product> list = null;
        long start = (page - 1) * size;
        long end = start + size - 1;
        list = redisTemplate.opsForList().range(JHS_KEY_A, start, end);
        if (CollectionUtils.isEmpty(list)) {
            //  Redis找不到，去数据库中查询
            log.info("A缓存已经失效或活动已经结束");
            list = redisTemplate.opsForList().range(JHS_KEY_B, start, end);
            if (CollectionUtils.isEmpty(list)) {
                // todo Redis找不到，去数据库中查询
            }
        }
        log.info("参加活动的商家: {}", list);
        return list;
    }
    ```






## 7.6、总结

| 缓存问题     | 产生原因               | 解决方案                               |
| ------------ | ---------------------- | -------------------------------------- |
| 缓存更新方式 | 数据变成、缓存时效性   | 同步更新、失效更新、异步更新、定时更新 |
| 缓存不一致   | 同步更新失败、异步更新 | 增加重试、补偿任务、最终一致           |
| 缓存穿透     | 恶意攻击               | 空对象缓存、BloomFilter                |
| 缓存击穿     | 热点key失效            | 互斥更新、随机退避、差异失效时间       |
| 缓存雪崩     | 缓存挂掉               | 快速失败熔断、主从模式、集群模式       |



# 8、手写Redis分布式锁

## 8.1、面试题

`Redis除了拿来做缓存，还见过基于Redis的什么用法？`

1. 数据共享、分布式Session
2. 分布式锁
3. 全局ID
4. 计算器、点赞
5. 位统计
6. 购物车
7. 轻量级消息队列
8. 抽奖
9. 点赞、签到、打卡
10. 差集交集并集、用户关注、可能认识的人、推荐模型
11. 热点新闻、热搜排行榜



`Redis做分布式锁的时候有什么注意的问题？`

`公司自己实现的分布式锁是否用的setnx命令实现？这个是最合适的吗？你如何考虑分布式锁的可冲入问题？`

`如果是Redis是单点部署的，会带来什么问题？`

`Redis集群模式下，比如主从模式，CAP方面有没有什么问题呢？`

- CAP：redis集群是AP，redis单机C，一致性



`简单的介绍一下Redlock？简单聊聊Redission`

`Redis分布式锁如何续期？看门狗知道吗？`





## 8.2、锁的种类

- **单机版同一个JVM虚拟机内**，synchronized或者Lock接口
- **分布式多个不同JVM虚拟机**，单机的线程锁机制不再起作用，资源类在不同的服务器之间共享了



## 8.3、一种靠谱分布式锁需要具备的条件和刚需

1. 独占性：Only One，任何时刻只能有且仅有一个线程持有
2. 高可用
   1. 若redis集群环境中，不能因为某一个节点挂了而出现获取锁和释放锁失败的情况
   2. 高并发请求下，依旧性能OK
3. 防死锁：杜绝死锁，必须有超时控制机制或者撤销操作，有个兜底终止跳出方案
4. 不乱抢： 防止张冠李戴，不能私下unlock别人的锁，只能自己加锁自己释放
5. 重入性：同一个节点的同一个线程如果获得锁之后，它也可以再次获取这个锁。



## 8.4、分布式锁

1. setnx key value
2. set key value[ Ex seonds ] [ PX milliseconds ] [ NX|XX ]
   1. EX：key在多少秒之后过期
   2. PX：key在多少毫秒之后过期
   3. NX：当key**不存在**的时候，才创建key，效果等同于setnx
   4. XX：当key**存在**的时候，覆盖key



## 8.5、重点

> JUC中AQS锁的规范落地参考+可重入锁考虑+Lua脚本+Redis命令一步步实现分布式锁



## 8.6、Base案例（Boot+Redis）

使用场景：多个服务间保证同一时刻同一时间段内同一用户只能有一个请求（防止关键业务出现并发攻击）

- pom

  ```xml
  <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <java.version>1.8</java.version>
      <maven.compiler.source>1.8</maven.compiler.source>
      <maven.compiler.target>1.8</maven.compiler.target>
      <lombok.version>1.16.18</lombok.version>
  </properties>
  
  <dependencies>
      <!--SpringBoot 通用依赖模块-->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      <!-- SpringBoot 与Redis整合依赖 -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-redis</artifactId>
      </dependency>
      <dependency>
          <groupId>org.apache.commons</groupId>
          <artifactId>commons-pool2</artifactId>
      </dependency>
      <!-- swagger2 -->
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
  
      <dependency>
          <groupId>cn.hutool</groupId>
          <artifactId>hutool-all</artifactId>
          <version>5.2.3</version>
      </dependency>
  
      <!-- 通用基础配置 -->
      <dependency>
          <groupId>org.projectlombok</groupId>
          <artifactId>lombok</artifactId>
          <version>${lombok.version}</version>
          <optional>true</optional>
      </dependency>
  
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-test</artifactId>
          <scope>test</scope>
      </dependency>
  </dependencies>
  ```

- YML

  ```yml
  server.port=8081
  spring.application.name=redis_distributed_lock2
  # =====================swaqqer2=====================
  # http://localhost:7777/swagger-ui.html
  swagger2.enabled=true
  spring.mvc.pathmatch.matching-strategy=ant_path_matcher
  # =====================redis单机=====================
  spring.redis.database=0
  #修改为自己真实IP
  spring.redis.host=127.0.0.1
  spring.redis.port=6379
  spring.redis.password=zzq121700
  spring.redis.lettuce.pool.max-active=8
  spring.redis.1ettuce.pool.max-wait=-1ms
  spring.redis.1ettuce.pool.max-idle=8
  spring.redis.lettuce.pool.min-idle=0
  ```

- 业务类

  **InventoryService**

  ```java
  @Service
  @Slf4j
  public class InventoryService {
      
      @Autowired
      private StringRedisTemplate stringRedisTemplate;
      @Value("${server.port}")
      private String port;
  
      private Lock lock = new ReentrantLock();
  
      public String sale() {
          String resMessgae = "";
          lock.lock();
          try {
              // 1 查询库存信息
              String result = stringRedisTemplate.opsForValue().get("inventory01");
              // 2 判断库存书否足够
              Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
              // 3 扣减库存，每次减少一个库存
              if (inventoryNum > 0) {
                  stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                  resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum;
                  log.info(resMessgae + "\t" + "，服务端口号：" + port);
              } else {
                  resMessgae = "商品已售罄。";
                  log.info(resMessgae + "\t" + "，服务端口号：" + port);
              }
          } finally {
              lock.unlock();
          }
          return resMessgae;
      }
  }
  ```

  **InventoryController**

  ```java
  @RestController
  @Api(tags = "redis分布式锁测试")
  public class InventoryController {
      @Autowired
      private InventoryService inventoryService;
  
      @GetMapping("/inventory/sale")
      @ApiOperation("扣减库存，一次卖一个")
      public void sale() {
          inventoryService.sale();
      }
  }
  ```

  



## 8.7、手写分布式锁思路分析

1. 初始化版本简单添加

   1. 业务类
      1. InventoryService
      2. 将8081的业务逻辑拷贝到8082
      
   2. nginx分布式微服务架构
      1. 问题
         1. 分布式部署后，单机锁还是出现超卖现象，需要分布式锁
         2. Nginx配置负载均衡
         3. 启动两个微服务访问
         4. bug-why
            - 问题：**超卖现象**，nginx和Jmeter压测后，不满足高并发分布式锁的性能要求
            - 原因：单机的JVM加锁无法解决两个服务的同时访问
         5. 分布式锁出现
      2. 解决
      
   3. redis分布式锁

      1. **InventoryService**

         ```java
         public String sale() {
             String resMessgae = "";
             String key = "DisneyRedisLocak";
             String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
         
             Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue);
             // 抢不到的线程继续重试
             if (!flag) {
                 // 线程休眠20毫秒，进行递归重试
                 try {
                     TimeUnit.MILLISECONDS.sleep(20);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 sale();
             } else {
                 try {
                     // 1 抢锁成功，查询库存信息
                     String result = stringRedisTemplate.opsForValue().get("inventory01");
                     // 2 判断库存书否足够
                     Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                     // 3 扣减库存，每次减少一个库存
                     if (inventoryNum > 0) {
                         stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                         resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum + "\t" + "，服务端口号：" + port;
                         log.info(resMessgae);
                     } else {
                         resMessgae = "商品已售罄。" + "\t" + "，服务端口号：" + port;
                         log.info(resMessgae);
                     }
                 } finally {
                     stringRedisTemplate.delete(key);
                 }
             }
             return resMessgae;
         }
         ```

         通过递归的方式来完成重试，不断获取锁

         但是依旧有问题：手工设置5000个线程来抢占锁，压测OK，但是容易导致StackOverflowError，不推荐，需要进一步完善

      2. **InventoryService**

         多线程判断想想JUC里面的虚假唤醒，用while替代if，自旋锁代替递归重试

         ```java
         public String sale() {
             String resMessgae = "";
             String key = "DisneyRedisLocak";
             String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
         
             // 不用递归了，高并发容易出错，我们用自旋代替递归方法重试调用；也不用if，用while代替
             while (!stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue)) {
                 // 线程休眠20毫秒，进行递归重试
                 try {TimeUnit.MILLISECONDS.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
             }
         
             try {
                 // 1 抢锁成功，查询库存信息
                 String result = stringRedisTemplate.opsForValue().get("inventory01");
                 // 2 判断库存书否足够
                 Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                 // 3 扣减库存，每次减少一个库存
                 if (inventoryNum > 0) {
                     stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                     resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum + "\t" + "，服务端口号：" + port;
                     log.info(resMessgae);
                 } else {
                     resMessgae = "商品已售罄。" + "\t" + "，服务端口号：" + port;
                     log.info(resMessgae);
                 }
             } finally {
                 stringRedisTemplate.delete(key);
             }
             return resMessgae;
         }
         ```

   4. 宕机与过期+防止死锁

      - 问题：部署了微服务的Java程序挂了，代码层面根本没有走到finally这块，没办法保证解锁（无过期时间该key一直存在），这个key没有被删除，需要加入一个过期时间限定key

      - 解决：

        1. ！高并发多线程下的一致性和原子性，设置了key+过期时间分开了，必须合并成一行具备原子性

           ```java
           while (!stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue)) {
                       // 线程休眠20毫秒，进行递归重试
                       try {TimeUnit.MILLISECONDS.sleep(20);} catch (InterruptedException e){e.printStackTrace();}
           }
           
           // 设置过期时间
           stringRedisTemplate.expire(key, 30L, TimeUnit.SECONDS);
           ```

        2. 加锁和过期时间设置必须同一行，保证原子性

           ```java
           	while (!stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue, 30L, TimeUnit.SECONDS)) {
               // 线程休眠20毫秒，进行递归重试
               try {TimeUnit.MILLISECONDS.sleep(20);} catch (InterruptedException e){e.printStackTrace();}
               }
           ```

   5. 防止误删key的问题

      1. 问题：实际业务中，如果处理时间超过了设置的key的过期时间，那删除key的时候，岂不是张冠李戴，删除了别人的锁

         ![image-20240327160640670](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240327160640670.png)

      2. 解决：

         1. 只能自己删除自己的，不许动别人的

         2. ```java
            public String sale() {
                String resMessgae = "";
                String key = "DisneyRedisLocak";
                String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
            
                // 不用递归了，高并发容易出错，我们用自旋代替递归方法重试调用；也不用if，用while代替
                while (!stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue, 30L, TimeUnit.SECONDS)) {
                    // 线程休眠20毫秒，进行递归重试
                    try {TimeUnit.MILLISECONDS.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
                }
            
                try {
                    // 1 抢锁成功，查询库存信息
                    String result = stringRedisTemplate.opsForValue().get("inventory01");
                    // 2 判断库存书否足够
                    Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                    // 3 扣减库存，每次减少一个库存
                    if (inventoryNum > 0) {
                        stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                        resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum + "\t" + "，服务端口号：" + port;
                        log.info(resMessgae);
                    } else {
                        resMessgae = "商品已售罄。" + "\t" + "，服务端口号：" + port;
                        log.info(resMessgae);
                    }
                } finally {
                    // 改进点，判断加锁与解锁是不同客户端，自己只能删除自己的锁，不误删别人的锁
                    if (stringRedisTemplate.opsForValue().get(key).equalsIgnoreCase(uuidValue)) {
                        stringRedisTemplate.delete(key);
                    }
                }
                return resMessgae;
            }
            ```

   6. finally代码块的判断+del删除不是原子性的

      1. ![image-20240327165159892](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240327165159892.png)
      2. 解决：启用lua脚本编写redis分布式锁判断+删除判断代码
      3. 官网解释：https://redis.io/docs/manual/patterns/distributed-locks/
      4. Lua脚本
         1. Lua是一种轻量小巧的脚本语言，用标准C语言编写并以源代码形式开放，其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。

         2. Lua是一个小巧的脚本语言。它是巴西里约热内卢天主教大学（Pontifical Catholic University of Rio de Janeiro）里的一个由Roberto Ierusalimschy、Waldemar Celes 和 Luiz Henrique de Figueiredo三人所组成的研究小组于1993年开发的。

         3. 设计目的：其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。

         4. eval luascript numkeys [key [key ..]] [arg [arg ..]]

         5. Lua特性：
            1. 轻量级：它用标准C语言编写并以源代码形式开放，编译后仅仅一百余k，可以很方便的嵌入到别的程序里。
            2. 可扩展：Lua提供了非常易于使用的扩展接口和机制；由宿主语言（通常是C或C++）提供这些功能，Lua可以使用它们，就像本来就内置的功能一样。

         6. 写法 eval "return ' hello lua' " 0

         7. 调用Redis操作 EVAL "redis.call('set','k1','v1') redis.call('expire','k1','30') return redis.call('get','k1')" 0

         8. EVAL "return redis.call('mset',KEYS[1],ARGV[1],KEYS[2],ARGV[2])" 2 k1 k2 lua1 lua2

         9. ```shell
            if redis.call("get",KEYS[1]) == ARGV[1] then
                return redis.call("del",KEYS[1])
            else
                return 0
            end
            ```

         10. 解决：

             ```java
             public String sale() {
                 String resMessgae = "";
                 String key = "DisneyRedisLocak";
                 String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
                 // 不用递归了，高并发容易出错，我们用自旋代替递归方法重试调用；也不用if，用while代替
                 while (!stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue, 30L, TimeUnit.SECONDS)) {
                     // 线程休眠20毫秒，进行递归重试
                     try {TimeUnit.MILLISECONDS.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
                 }
             
                 try {
                     // 1 抢锁成功，查询库存信息
                     String result = stringRedisTemplate.opsForValue().get("inventory01");
                     // 2 判断库存书否足够
                     Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                     // 3 扣减库存，每次减少一个库存
                     if (inventoryNum > 0) {
                         stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                         resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum + "\t" + "，服务端口号：" + port;
                         log.info(resMessgae);
                     } else {
                         resMessgae = "商品已售罄。" + "\t" + "，服务端口号：" + port;
                         log.info(resMessgae);
                     }
                 } finally {
                     // 改进点，修改为Lua脚本的Redis分布式锁调用，必须保证原子性，参考官网脚本案例
                     String luaScript =
                         "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                         	"return redis.call('del',KEYS[1]) " +
                         "else " +
                         	"return 0 " +
                         "end";
                     stringRedisTemplate.execute(new DefaultRedisScript(luaScript, Boolean.class), Arrays.asList(key), uuidValue);
                 }
                 return resMessgae;
             }
             ```

   7. **可重入锁 + 设计模式**

      1. 问题：

         - 如何兼顾锁的可重入性问题？

      2. 可重入锁（又名递归锁）

         - 说明：是指在同一线程在外层方法获取锁的时候，再进入改线程的内层方法会自动获取锁（前提，锁对象得是同一个对象）。不会因为之前已经获取过还没释放而阻塞。

         - 如果是一个有synchronized修饰的递归调用方法，程序第二次进入被自己阻塞了，出现了作茧自缚。所以**Java中ReentrantLock和synchronized都是可重入锁**，可重入锁的一个优点是可一定程度避免死锁。

         - 解释：一个线程中多个流程可以获取同一把锁，持有这把同步锁可以再次进入。自己可以获取自己的内部锁。

         - 可重入锁的种类：

           - 隐式锁：即synchronized关键字使用的锁，默认是可重入锁

             > 指的是可重复可递归调用的锁，在外层使用锁之后，在内层仍然可以使用，并且不发生死锁，这样的锁就叫做可重入锁。
             >
             > 简单的来说就是：**在一个synchronized修饰的方法或代码块内部调用本类的其他synchronized修饰的方法或代码块时，是永远可以得到锁的**。
             >
             > ```java
             > 	final Object obj = new Object();
             >     public void entry01(){
             >         new Thread(()->{
             >             synchronized (obj){
             >                 System.out.println(Thread.currentThread().getName()+"\t"+"外层调用");
             >                 synchronized (obj) {
             >                     System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
             >                     synchronized (obj) {
             >                         System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
             >                     }
             >                 }
             >             }
             >         },"t1").start();
             >     }
             > 
             >     public void entry02(){
             >         m1();
             >     }
             > 
             >     private synchronized void m1() {
             >         System.out.println(Thread.currentThread().getName()+"\t"+"外层调用");
             >         m2();
             >     }
             > 
             >     private synchronized void m2() {
             >         System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
             >         m3();
             >     }
             > 
             >     private synchronized void m3() {
             >         System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
             >     }
             > ```

           - Synchronized的重入的实现机理

             > **每个锁对象拥有一个锁计数器和一个指向持有该锁的线程的指针。**
             >
             > 当执行monitrenter时，如果目标锁对象得计数器为零，那么说明它没有被其他线程所持有，Java虚拟机会将该锁对象的持有线程设置为当前线程，并且将其计数器加1。
             >
             > 在目标锁对象的计数器不为零的情况下，如果锁对象的持有线程是当前线程，那么Java虚拟机可以将其计数器加1，否则需要等待，直至持有线程释放该锁。
             >
             > 当执行monitorexit时，Java虚拟机则需要将锁对象的计数器减1。计数器为零代表锁已被释放。

           - 显示锁（即Lock）也有ReentrantLock这样的可重入锁

             > 在一个Synchronized修饰的方法或代码块的内部调用本类的其他Synchronized修饰的方法或代码块时，是永远可以得到锁的。
             >
             > ```java
             > Lock lock = new ReentrantLock();
             >     public void entry03() {
             >         new Thread(() -> {
             >             lock.lock();
             >             try {
             >                 System.out.println(Thread.currentThread().getName() + "\t" + "外层调用");
             >                 lock.lock();
             >                 try {
             >                     System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
             >                     lock.lock();
             >                     try {
             >                         System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
             >                     } finally {
             > //                        t4就拿不到这个锁
             >                         lock.unlock();
             >                     }
             >                 } finally {
             >                     lock.unlock();
             >                 }
             >             } finally {
             >                 lock.unlock();
             >             }
             >         }, "t3").start();
             > 
             >         //  暂停两秒
             >         try {
             >             TimeUnit.MILLISECONDS.sleep(2);
             >         } catch (InterruptedException e) {
             >             e.printStackTrace();
             >         }
             >         new Thread(() -> {
             >             lock.lock();
             >             try {
             >                 System.out.println(Thread.currentThread().getName() + "\t" + "外层调用+t4");
             >             } finally {
             >                 lock.unlock();
             >             }
             >         }, "t4").start();
             >     }
             > ```

      3. lock/unlock配合可重入锁进行AQS源码分析

         > lock几次就要unlock几次

      4. 可以重入锁计算问题，redis中哪个数据类型可以代替

         1. k k v	=>	hset
         2. Map<String,Map<Object,Object>>
         3. setnx，只能解决有无的问题
         4. hset，不但解决有无，还解决可重入问题

      5. 加锁Lua脚本lock

         - 先判断redis分布式锁这个key是否存在

           > EXISTS key：
           >
           > 返回零说明不存在，hset新建当前线程属于自己的锁，field key格式为UUID:ThreadID，valua为加锁次数；
           >
           > 返回1说明已经有锁，需要进一步判断是不是当前线程自己的 -> 
           >
           > HEXISTS key uuid:ThreadID：返回0说明不是自己的锁；返回1说明是自己的锁，自增1次表示重入
           >
           > HINCRBY key UUID:ThreadID 1

         - 按照上述设计修改Lua脚本

           > **V1版本**
           >
           > ```shell
           > // 加锁的Lua脚本，对标我们的lock方法
           > if redis.call('exists', 'key') == 0 then
           > 	redis.call('hset', 'key', 'uuid:threadid', 1)
           > 	redis.call('expire', 'key', 50)
           > 	return 1
           > elseif redis.call('hexists', 'key', 'uuid:threadid') == 1 then
           > 	redis.call('hincrby', 'key', 'uuid:threadid', 1)
           > 	redis.call('expire', 'key', 50)
           > 	return 1
           > else
           > 	return 0
           > end
           > ```
           >
           > **V2版本**
           >
           > 当key不存在的时候，HINCRBY可以自动创建这个key并且自增
           >
           > ```shell
           > // V2 合并相同的代码，用hincrby替代hset，精简代码
           > if redis.call('exists', 'key') == 0 or redis.call('hexists', 'key', 'uuid:threadid') == 1 then
           > 	redis.call('hincrby', 'key', 'uuid:threadid', 1)
           > 	redis.call('expire', 'key', 50)
           > 	return 1
           > else
           > 	return 0
           > end
           > ```
           >
           > **V3版本**
           >
           > ```shell
           > // V3 脚本OK，换上参数来替代写死的key，value
           > if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
           > 	redis.call('hincrby', KEYS[1], ARGV[1], 1)
           > 	redis.call('expire', KEYS[1], ARGV[2])
           > 	return 1
           > else
           > 	return 0
           > end
           > ```
           >
           > **测试**
           >
           > ```shell
           > -- 已完成验证
           > if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then redis.call('hincrby', KEYS[1], ARGV[1], 1) redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 0 end
           > 
           > eval "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then redis.call('hincrby', KEYS[1], ARGV[1], 1) redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 0 end" 1 DisneyRedisLock 001122:1 50
           > ```
         
      6. 解锁Lua脚本unlock

         - 设计思路：有锁且还是自己的锁 -> HEXISTS key uuid:ThreadID

           > 返回零，说明根本没有锁，程序返回nil
           >
           > 不是零，说明有锁且是自己的锁，直接调用HINCRBY -1，表示每次减1，解锁一次知道它变成零表示可以删除该锁key，del 锁

         - 设计修改为Lua脚本

           > **V1版本**
           >
           > ```shell
           >   // 解锁的Lua脚本，对标我们的lock方法
           >   if redis.call('hexists', 'key', uuid:threadid) == 0 then
           >   	return nil
           >   elseif redis.call('hincrby', key, uuid:threadid, -1) == 0 then
           >       return redis.call('del', key)
           >   else 
           >       return 0
           >   end
           > ```
           >
           > **V2版本**
           >
           > ```shell
           > if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then
           > 	return nil
           > elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then
           >     return redis.call('del', KEYS[1])
           > else 
           >     return 0
           > end
           > ```
           >
           > **测试**
           >
           > ```shell
           > if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then return nil elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then return redis.call('del', KEYS[1]) else return 0 end
           > 
           > eval "if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then return nil elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then return redis.call('del', KEYS[1]) else return 0 end " 1 DisneyRedisLock 001122:1
           > ```

   8. Lua脚本整合进微服务Java程序

      1. 复原程序为初始无锁的状态

         ```java
         public String sale() {
             String resMessgae = "";
             try {
                 // 1 抢锁成功，查询库存信息
                 String result = stringRedisTemplate.opsForValue().get("inventory01");
                 // 2 判断库存书否足够
                 Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                 // 3 扣减库存，每次减少一个库存
                 if (inventoryNum > 0) {
                     stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                     resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum + "\t" + "，服务端口号：" + port;
                     log.info(resMessgae);
                 } else {
                     resMessgae = "商品已售罄。" + "\t" + "，服务端口号：" + port;
                     log.info(resMessgae);
                 }
             } finally {
         
             }
             return resMessgae;
         }
         ```

      2. 新建RedisDistributedLock类并实现JUC里面的Lock接口

         ```java
         package com.redis04_lock.utils;
         
         import java.util.concurrent.TimeUnit;
         import java.util.concurrent.locks.Condition;
         import java.util.concurrent.locks.Lock;
         
         //  自己的Redis分布式锁
         public class RedisDistributedLock implements Lock {
         
             @Override
             public void lock() {
         
             }
         
         
             @Override
             public boolean tryLock() {
                 return false;
             }
         
             @Override
             public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                 return false;
             }
         
             @Override
             public void unlock() {
         
             }
         
             //  ---暂时用不到这两个，暂时不用重写
         
             @Override
             public void lockInterruptibly() throws InterruptedException {
         
             }
         
             @Override
             public Condition newCondition() {
                 return null;
             }
         }
         ```

      3. 满足JUC里面AQS对Lock锁的接口规范定义来进行实现落地代码

      4. 结合设计模式开发属于自己的Redis分布式锁工具类

         - Lua脚本加锁

           ```shell
           if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
           	redis.call('hincrby', KEYS[1], ARGV[1], 1)
           	redis.call('expire', KEYS[1], ARGV[2])
           	return 1
           else
           	return 0
           end
           ```

           ```java
               @Override
               public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                   if (time == -1L) {
                       String str =
                               "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then "
                                       + "redis.call('hincrby', KEYS[1], ARGV[1], 1) "
                                       + "redis.call('expire', KEYS[1], ARGV[2]) "
                                       + "return 1 "
                                       + "else "
                                       + "return 0 "
                                       + "end";
           
                       Boolean execute = stringRedisTemplate.execute(
                               new DefaultRedisScript<>(str, Boolean.class),
                               Arrays.asList(lockName),
                               uuidValue,
                               String.valueOf(expireTime));
                       while (!execute) {
                           //暂停60毫秒
                           try {
                               TimeUnit.MILLISECONDS.sleep(60);
                           }catch (InterruptedException e){
                               e.printStackTrace();
                           }
                       }
                       return true;
                   }
                   return false;
               }
           ```

         - Lua脚本解锁

           ```shell
           if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then
           	return nil
           elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then
               return redis.call('del', KEYS[1])
           else 
               return 0
           end
           ```

           ```java
               @Override
               public void unlock() {
                   String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then   "
                           + "return nil   "
                           + "elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then     "
                           + "return redis.call('del', KEYS[1]) " +
                           "else"
                           + "    return 0 " +
                           "end";
                   Long execute = stringRedisTemplate.execute(
                           new DefaultRedisScript<>(script, Long.class),
                           Arrays.asList(lockName),
                           uuidValue,
                           String.valueOf(expireTime));
           
                   if (execute == null){
                       throw new RuntimeException("this lock doesn't exists !!");	
                   }
               }
           ```

         - 工厂设计模式引入

           - 通过实现JUC里面的Lock接口，实现Redis分布式锁RedisDistributedLock

           - InventoryService直接使用上面的代码设计，有什么问题

           - 考虑扩展，本次是redis实现分布式锁，以后zookeeper、mysql实现？

             ```java
             package com.redis04_lock.utils;
             
             
             import org.springframework.beans.factory.annotation.Autowired;
             import org.springframework.data.redis.core.StringRedisTemplate;
             import org.springframework.stereotype.Component;
             
             import java.util.concurrent.locks.Lock;
             
             @Component
             public class DistributedLockFactory {
             
                 @Autowired
                 private StringRedisTemplate stringRedisTemplate;
                 private String lockName;
             
                 public Lock getDistributedLock(String lockType){
                     if (lockType == null) return null;
                     if (lockType.equalsIgnoreCase("REDIS")){
                         this.lockName = "DisneyRedisLock";
                         return new RedisDistributedLock(stringRedisTemplate,lockName);
                     } else if (lockType.equalsIgnoreCase("ZOOKEEPER")) {
                         this.lockName = "DisneyZookeeperLock";
                         // TODO zookeeper版本的分布式锁
                     } else if (lockType.equalsIgnoreCase("MYSQL")) {
                         this.lockName = "DisneyMYSQLrLock";
                         // TODO MYSQL版本的分布式锁
                     }
                     return null;
                 }
             }
             ```

           - 引入工厂模式改造

             ```java
             package com.redis04_lock.service;
             
             import com.redis04_lock.utils.DistributedLockFactory;
             import com.redis04_lock.utils.RedisDistributedLock;
             import lombok.extern.slf4j.Slf4j;
             import org.springframework.beans.factory.annotation.Autowired;
             import org.springframework.beans.factory.annotation.Value;
             import org.springframework.data.redis.core.StringRedisTemplate;
             import org.springframework.stereotype.Service;
             
             import java.util.concurrent.locks.Lock;
             import java.util.concurrent.locks.ReentrantLock;
             import java.util.logging.Logger;
             
             @Service
             @Slf4j
             public class InventoryService02 {
             
                 @Autowired
                 private StringRedisTemplate stringRedisTemplate;
                 @Value("${server.port}")
                 private String port;
             
                 @Autowired
                 private DistributedLockFactory distributedLockFactory;
                 public String sale() {
                     String resMessgae = "";
             
                     Lock lock = distributedLockFactory.getDistributedLock("REDIS");
                     lock.lock();
                     try {
                         // 1 查询库存信息
                         String result = stringRedisTemplate.opsForValue().get("inventory01");
                         // 2 判断库存书否足够
                         Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                         // 3 扣减库存，每次减少一个库存
                         if (inventoryNum > 0) {
                             stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                             resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum;
                             System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
                         } else {
                             resMessgae = "商品已售罄。";
                             System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
                         }
                     } finally {
                         lock.unlock();
                     }
                     return resMessgae;
                 }
             }
             ```

           - 单机+并发测试通过

           - **可重入锁的问题**

             ```java
             package com.redis04_lock.service;
             
             import com.redis04_lock.utils.DistributedLockFactory;
             import com.redis04_lock.utils.RedisDistributedLock;
             import lombok.extern.slf4j.Slf4j;
             import org.springframework.beans.factory.annotation.Autowired;
             import org.springframework.beans.factory.annotation.Value;
             import org.springframework.data.redis.core.StringRedisTemplate;
             import org.springframework.stereotype.Service;
             
             import java.util.concurrent.locks.Lock;
             import java.util.concurrent.locks.ReentrantLock;
             import java.util.logging.Logger;
             
             @Service
             @Slf4j
             public class InventoryService02 {
             
                 @Autowired
                 private StringRedisTemplate stringRedisTemplate;
                 @Value("${server.port}")
                 private String port;
             
                 @Autowired
                 private DistributedLockFactory distributedLockFactory;
                 public String sale() {
                     String resMessgae = "";
             
                     Lock lock = distributedLockFactory.getDistributedLock("REDIS");
                     lock.lock();
                     try {
                         // 1 查询库存信息
                         String result = stringRedisTemplate.opsForValue().get("inventory01");
                         // 2 判断库存书否足够
                         Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                         // 3 扣减库存，每次减少一个库存
                         if (inventoryNum > 0) {
                             stringRedisTemplate.opsForValue().set("inventory01", String.valueOf(--inventoryNum));
                             resMessgae = "成功卖出一个商品，库存剩余：" + inventoryNum;
                             System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
                             testReEntry();
                         } else {
                             resMessgae = "商品已售罄。";
                             System.out.println(resMessgae + "\t" + "，服务端口号：" + port);
                         }
                     } finally {
                         lock.unlock();
                     }
                     return resMessgae;
                 }
             
                 private void testReEntry() {
             
                     Lock lock = distributedLockFactory.getDistributedLock("redis");
                     lock.lock();
                     try {
                         System.out.println("========测试可重入锁=========");
                     }finally {
                         lock.unlock();
                     }
                 }
             }
             ```

             ![image-20240330192151963](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330192151963.png)

             ![image-20240330192221169](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330192221169.png)

             可重入Bug，UUID不一致，ThreadID一致

           - **解决方案**

             DistributedLockFactory -> 新增一个无参构造函数

             ```java
             public DistributedLockFactory() {
                 this.uuid = IdUtil.simpleUUID();
             }
             ```

             RedisDistributedLock -> 修改构造方法

             ```java
             public RedisDistributedLock(StringRedisTemplate stringRedisTemplate, String lockName, String uuid) {
                 this.stringRedisTemplate = stringRedisTemplate;
                 this.lockName = lockName;
                 this.uuidValule = uuid + ":" + Thread.currentThread().getId();
                 this.expireTime = 50L;
             }
             ```

             使用@Autowired创建的工厂类是一个单例的，在Spring进行注入的时候已经初始化好了，所以所有线程产生的UUID都是一样的。

             ![image-20240330193448521](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330193448521.png)

   9. **自动续期**

      1. 确保RedisLock过期时间大于业务执行时间的问题

         > 时间到了，业务执行完需要自动续期

      2. CAP

         1. C的全拼是Consistency，代表一致性
         2. A的全拼是Availability，代表可用性
         3. P的全拼是Partition tolerance，代表分区容错性

         > - Redis集群是AP
         >
         >   Redis异步复制造成锁的丢失，例如：主节点没来得及把刚刚set进来的这条数据的从节点，master就挂了，从机上位但是从机上无数据
         >
         > - Zookeeper集群是CP
         >
         >   ![image-20240330194856060](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330194856060.png)
         >
         >   故障：
         >
         >   ![image-20240330194938866](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330194938866.png)
         >
         > - Eureka集群是AP
         >
         >   ![image-20240330195135483](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330195135483.png)
         >
         > - Nacos集群是AP
         >
         >   ![image-20240330195204581](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240330195204581.png)

      3. 加时，Lua脚本

         ```shell
         // 自动续期的LUA脚本
         if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
             return redis.call('expire', KEYS[1], ARGV[2])
         else
             return 0
         end
         ```

         测试验证

         ```shell
         
         if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end
         ---
         hset DisneyRedisLock test 001
         expire DisneyRedisLock 30
         eval "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end" 1 DisneyRedisLock test 1
         ```

         

      4. 自动续期功能

         ```java
         @Override
         public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
             if (-1 == time) {
                 String script =
                     "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then " +
                     "redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                     "redis.call('expire', KEYS[1], ARGV[2]) " +
                     "return 1 " +
                     "else " +
                     "return 0 " +
                     "end";
                 System.out.println("lock() lockName:" + lockName + "\t" + "uuidValue:" + uuidValue);
         
                 // 加锁失败需要自旋一直获取锁
                 while (!stringRedisTemplate.execute(
                     new DefaultRedisScript<>(script, Boolean.class),
                     Arrays.asList(lockName),
                     uuidValue,
                     String.valueOf(expireTime))) {
                     // 休眠60毫秒再来重试
                     try {TimeUnit.MILLISECONDS.sleep(60);} catch (InterruptedException e) {e.printStackTrace();}
                 }
                 // 新建一个后台扫描程序，来检查Key目前的ttl，是否到我们规定的剩余时间来实现锁续期
                 resetExpire();
                 return true;
             }
             return false;
         }
         
         // 自动续期
         private void resetExpire() {
             String script =
                 "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then " +
                 "return redis.call('expire', KEYS[1], ARGV[2]) " +
                 "else " +
                 "return 0 " +
                 "end";
             new Timer().schedule(new TimerTask() {
                 @Override
                 public void run() {
                     if (stringRedisTemplate.execute(
                         new DefaultRedisScript<>(script, Boolean.class),
                         Arrays.asList(lockName),
                         uuidValue,
                         String.valueOf(expireTime))) {
                         // 续期成功，继续监听
                         System.out.println("resetExpire() lockName:" + lockName + "\t" + "uuidValue:" + uuidValue);
                         resetExpire();
                     }
                 }
             }, (this.expireTime * 1000 / 3));
         }
         ```


## 8.8、总结

> synchronized单机版OK； -> v1.0
>
> Nginx分布式微服务，轮询多台服务器，单机锁不行；-> v2.0
>
> 取消单机锁，上redis分布式锁setnx，中小企业使用没问题；-> v3.1
>
>  只是加锁了，没有释放锁，出异常的话，可能无法释放锁，必须要在代码层面finally释放锁 -> v3.2
>
>  如果服务宕机，部署了微服务代码层面根本就没有走到finally这块，没办法保证解锁，这个Key没有被删除，需要对锁设置过期时间 -> v3.2
>
>  为redis的分布式锁key增加过期时间，还必须要保证setnx+过期时间在同一行，保证原子性 -> v4.1
>
>  程序由于执行超过锁的过期时间，所以在finally中必须规定只能自己删除自己的锁，不能把别人的锁删除了，防止张冠李戴 -> v5.0
>
> 将Lock、unlock变成LUA脚本保证原子性； -> v6.0
>
> 保证锁的可重入性，hset替代setnx+Lock变成LUA脚本，保障可重入性； -> v7.0
>
> 锁的自动续期 -> v8.0



# 9、Redlock算法和底层源码分析

## 9.1、面试题

> 自研一把分布式锁，面试回答的主要考点

1. 按照JUC里面java.util.concurrent.locks.Lock接口规范编写

2. lock()加锁关键逻辑

   - 加锁：加锁实际上就是在redis中，给key设置一个值，为了避免死锁，并给一个过期时间

   - 可重入：加锁的LUA脚本，通过redis里面的hash数据类型，加锁和可重入性都要保证

   - 自旋：加锁不成，需要while进行重试并自旋，AQS

   - 续期：在过期时间内，一定时间内业务还未完成，自动给锁续期

     ![image-20240331164140052](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331164140052.png)

3. unlock()解锁关键逻辑

   - 将redis的key删除，但是也不能乱删，不能说客户端1的请求将客户端2的锁给删掉，只能自己删除自己的锁

   - 考虑可重入性的递减，加锁几次就需要删除几次

   - 最后到零了，直接del删掉掉

     ![image-20240331164257448](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331164257448.png)



## 9.2、RedLock红锁算法

官网说明：https://redis.io/docs/manual/patterns/distributed-locks/

![image-20240331165056227](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331165056227.png)

> **使用Redis的分布式锁**  使用Redis的分布式锁模式 
>
> 分布式锁在许多环境中是非常有用的原语，在这些环境中，不同的进程必须以互斥的方式使用共享资源进行操作。 有许多库和博客文章描述了如何使用Redis实现DLM（分布式锁管理器），但是每个库都使用不同的方法，许多库使用简单的方法，与稍微复杂一些的设计相比，保证率较低。 本页描述了一个更规范的算法来实现Redis的分布式锁。我们提出了一个称为Redlock的算法，它实现了一个DLM，我们认为它比普通的单实例方法更安全。我们希望社区能够对其进行分析，提供反馈，并将其作为实施或更复杂或替代设计的起点。

**为什么学习这个？怎么产生的？**

手写的分布式锁有什么缺点？

![image-20240331165455000](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331165455000.png)

![image-20240331165655335](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331165655335.png)

> 有时候在特殊情况，例如在故障期间，多个客户端可以同时持有锁是完全没问题的。如果是这种情况，您可以使用基于复制的解决方案。否则，我们建议实施文档中描述的解决方案。
>
> **线程1首先获取锁成功，将键值对写入Redis的master节点，在Redis将该键值对同步到slave节点之前，master发生了故障；**
>
> Redis触发故障转移，其中一个slave升级为新的master，此时新上位的master并不包含线程1写入的键值对，因此线程2尝试获取锁也可以成功拿到锁，此时相当于两个线程获取了锁，可能会导致各种预期之外的情况发生，例如最常见的脏数据。
>
> 我们加的是排它独占锁，同一时间只能有一个建Redis锁成功并持有锁，严禁出现2个以上的请求线程拿到锁。

**RedLock算法设计理念**

- Redis之父提出了RedLock算法解决上面这个一锁被多建的问题

  Redis也提供了RedLock算法，用来实现基于多个实例的分布式锁。锁变量由多个实例维护，即使有实例发生了故障，锁变量仍然是存在的，客户端还是可以完成锁操作。RedLock算法是实现高可靠分布式锁的一种有效解决方案，可以在实际开发中使用。

  ![image-20240331170454556](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331170454556.png)

  ![image-20240331170527307](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331170527307.png)

  - ​	**设计理念**

    > 该方案也是基于(set加锁、Lua脚本解锁）进行改良的，所以redis之父antirez只描述了差异的地方，大致方案如下：假设我们有N个Redis主节点，例如N= 5这些节点是完全独立的，我们不使用复制或任何其他隐式协调系统，为了取到锁客户端执行以下操作：
    >
    > 1. 获取当前时间，以毫秒为单位
    > 2. 一次尝试从5个实例，使用相同的key和随机值（例如UUID）获取锁，当Redis请求获取锁时，客户端应该设置一个超时时间，这个超时时间应该小于锁的失效时间。例如你的锁自动失效时间为10秒，则超时时间应该在5-50毫秒之间。这样可以防止客户端再试与一个宕机的Redis节点对话时长时间处于阻塞状态。如果一个实例不可用，客户端应该尽快去尝试另一个Redis实例请求获取锁；
    > 3. 客户端通过当前时间减去步骤1记录的时间来计算获取锁使用的时间。当且仅当从大多数(N/2+1，这里是3个节点)的Redis节点都取到锁，并且获取锁使用的时间小于锁失效时间时，锁才算获取成功;
    > 4. 如果取到了锁，其真正有效时间等于初始有效时间减去获取锁所使用的时间（步骤3计算的结果）。
    > 5. 如果由于某些原因未能获得锁（无法在至少N/2＋1个Redis实例获取锁、或获取锁的时间超过了有效时间)，客户端应该在所有的Redis 实例上进行解锁（即便某些Redis实例根本就没有加锁成功，防止某些节点获取到锁但是客户端没有得到响应而导致接下来的一段时间不能被重新获取锁）。

    该方案为了解决数据不一致的问题，直接舍弃了异步复制只使用master节点，同时由于舍弃了slave，为了保证可用性，引入了N个节点。

    客户端只有在满足下面的两个条件时，才能认为是加锁都成功。

    条件1：客户端从超过半数（大于等于N/2+1）的Redis实例上成功获取到了锁。

    条件2：客户端获取锁的总耗时没有超过锁的有效时间。

  - **解决方案**

    ![image-20240331172930540](K:\GitHub\notes\Redis\Redis_AD.assets\image-20240331172930540.png)

    为什么是奇数：N = 2X + 1 （N是最终部署机器数，X是容错机器数）



## 9.3、Redisson实现

Redisson是Java的Redis客户端之一，提供了一些API方便操作Redis

Redisson：[官网](https://redisson.org)、[Github](https://github.com/redisson/redisson/wiki)、[解决分布式锁](https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers)





# 10、Redis的缓存过期淘汰策略

# 11、Redis经典五大类型源码及底层实现

# 12、Redis为什么快？高性能设计之epoll和IO多路复用深度解析

# 13、终章总结
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

在Redis6.0及7后，**多线程机制默认是关闭的，如果需要使用多线程功能，需要在Redis.conf中完成两个设置。

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











## 5.5、BitMap







# 6、布隆过滤器BloomFilter

# 7、缓存预热 + 缓存雪崩 + 缓存击穿

# 8、手写Redis分布式锁

# 9、Redlock算法和底层源码分析

# 10、Redis的缓存过期淘汰策略

# 11、Redis经典五大类型源码及底层实现

# 12、Redis为什么快？高性能设计之epoll和IO多路复用深度解析

# 13、终章总结
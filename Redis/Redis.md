# Redis7.0.0

# 1、Redis入门概述

## 1.1、Redis是什么

Redis：[官网](https://redis.io/)

Remote Dictionary Server（远程字典服务）是完全开源的，使用ANSIC语言编写遵守BSD协议，是一个高性能的Key-Value数据库提供了丰富的数据结构，例如String、Hash、List、Set、SortedSet等等。数据是存在内存中的，同时Redis支持事务、持久化、LUA脚本、发布/订阅、缓存淘汰、流技术等多种功能特性提供了主从模式、Redis Sentinel和Redis Cluster集群架构方案。

```markdown
Redis is an open source (BSD licensed), in-memory data structure store used as a database, cache, message broker, and streaming engine. Redis provides data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs, geospatial indexes, and streams. Redis has built-in replication, Lua scripting, LRU eviction, transactions, and different levels of on-disk persistence, and provides high availability via Redis Sentinel and automatic partitioning with Redis Cluster.
Redis是一个开源的（BSD许可）内存数据结构存储，用作数据库、缓存、消息代理和流引擎。Redis提供数据结构，如字符串、散列、列表、集合、带范围查询的排序集合、位图、超日志、地理空间索引和流。Redis具有内置的复制、Lua脚本、LRU逐出、事务和不同级别的磁盘持久性，并通过Redis Sentinel和Redis Cluster的自动分区提供高可用性。
```



Redis之父：**Salvatore Sanfilippo**，通常被称为Antirez，是一位意大利程序员，因创造了Redis而闻名。Redis是一种流行的键值存储数据库，广泛应用于许多知名科技公司，如Uber、Instacart、Slack、Hulu和Twitter等。



## 1.2、Redis的主流功能

1. 分布式缓存，挡在MySql数据库之前的带刀护卫
2. 内存存储和持久化（RDB+AOF）redis支持异步将内存中的数据写到硬盘上，同时不影响继续服务
3. 高可用架构搭配
4. 缓存穿透、击穿、雪崩
5. 分布式锁
6. 队列
7. 排行榜+点赞
8. .....



**与传统数据库关系（MySql）**

Redis是Key-Value数据库（NoSQL一种），mysql是关系数据库

Redis数据操作主要在内存，而MySql主要存储在磁盘

Redis在某一些场景使用中要明显优于MySql，比如计数器，点赞，排行榜等方便

Redis通常用于一些特定场景，需要与MySql一起配合使用

两者并不是相互替换和竞争关系，而是通用和配合关系。



## 1.3、Redis的优势

1. 性能极高 - Redis能读的速度是110000次/秒，写得速度是81000次/秒
2. Redis数据类型丰富，不仅仅支持简单的Key-Value类型的数据，同时还提供list、set、zest、hash等数据结构的存储
3. Redis支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用
4. Redis支持数据的备份，即master-slave（主从）模式的数据备份





# 2、Redis安装配置



## 2.1、Redis下载

[Redis.io](https://redis.io/download/)

[Redis.cn](http://www.redis.cn/)

[Redis中文文档](https://redis.com.cn/)

[Redis在线测试](https://try.redis.io/)

[Redis命令参考](http://doc.redisfans.com/)

[Redis源码地址](https://github.com/redis/redis)



## 2.2、安装和配置

1. 检查Linux是否有gcc环境：gcc -v
2. 没有gcc环境：yum - y install gcc - c++
3. 检查Linux是否有Redis：redis- server - v



1. 下载获得redis-7.0.0.tar.gz后将它放入我们的Linux目录/opt
2. /opt目录下解压redis
3. 进入目录
4. 在redis-7.0.0.0目录下执行make命令
5. 查看默认安装目录：usr/local/bin
   1. redis-benchmark：性能测试工具，服务启动后运行该命令，看看自己本子性能如何
   2. redis-check-aof：修复有问题的AOF
   3. redis-check-dump：修复有问题的dump.rdb文件
   4. redis-cli：客户端，操作入口
   5. redis-sentinel：redis集群使用
   6. redis-server：Redis服务器启动命令
6. 将默认的redis.conf拷贝到自己定义好的一个路径下，比如/myredis/
7. 修改/myredis目录下redis.conf配置文件做初始化设置
8. 启动服务
9. 连接服务
10. Redis默认端口为6379
11. HelloWorld
12. 关闭



# 3、Redis10大数据类型

1. Strings（字符串）
2. Lists（列表）
3. Sets（集合）
4. Hashes（哈希表）
5. Sorted sets（ZSet、有序列表）
6. Streams（流）
7. Geospatial（地理空间）
8. HyperLogLog（基数统计）
9. Bitmaps（位图）
10. Bitfileds（位域）



## 3.1、常见数据类型操作命令

- keys *：查看当前库所有的key
- exists key：判断某个key是否存在
- type key：查看key值是什么类型
- del key：删除指定的key数据
- unlink key：非阻塞删除，仅仅将keys从keyspace元数据中删除，真正的删除会在后续异步中操作
- ttl key：查看还有多少秒过期，-1表示永不过期，-2表示已过期
- expire key：秒钟，为给定的key设置过期时间
- move key dbindex[0-15]：将当前数据库的key移动到给定的数据库db当中
- select dbindex：切换数据库[0-15]，默认值为0
- dbsize：查看当前数据库key的数量
- flushdb：清空当前库
- flusshall：通杀全部库



## 3.2、String

- SET key value：设置指定的key值
- GET key：获取指定key的值
- GETRANGE key start end：返回key中字符串值的子字符
- GETSET key value：将给定key的值设为value，并返回key的旧值（old value）
- GETBIT key offset：对key所存储的字符串值，获取指定偏移量上的位（bit）
- MGET key1 [ key2 ]：获取所有（一个或多个）给定key的值
- SETBIT key offset value：对key所存储的字符串值，设置或清除指定偏移量上的位（bit）
- SETEX key seconds value：将值value关联到key，并将key的过期时间设为seconds（以秒为单位）
- SETNX sey value：只要key不存在时设置key的值
- SETRANGE key offset value：用value参数覆写给定key所存储的字符串量，从偏移量开始
- STRLEN key：返回key所存储的字符串的长度
- MSET key value [ key value ... ]：同时设置一个或者多个键值对
- PSETEX key milliseconds value：这个命令和SETEX命令相似，但它以毫秒为单位设置key的生存时间，而不是像SETEX命令那样，以秒为单位
- INCR key：将key中存储的数字值增1
- INCRBY key increment：将key所存储的值加上给定的增量值（increment）
- INCRBYFLOAT key increment：将key所存储的值加上给定的浮点增量值（increment）
- DECR key：将key存储的数字值减1
- DECRBY key decrement：key所存储的值减去给定的减量值
- APPEND key value：如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值的末尾



## 3.3、List

- BLPOP key1 [ key2 ] timeout：移出并获得列表的第一个元素，如果列表没有元素会阻塞列表知道等待超时或发现可弹出元素为止
- BRPOP key1 [ key2 ] timeout：移出并获得列表的最后一个元素，如果列表没有元素会阻塞列表知道等待超时或发现可弹出元素为止
- BRPOPLPUSH source destination timeout：从列表中弹出一个值，将弹出的元素插入到另一个列表中并返回它；如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
- LINDEX key index：通过索引获取列表中的元素
- LINSERT key BEFORE|AFTER pivot value：在列表的元素前或者后插入元素
- LLEN key：获取列表长度
- LPOP key：移出并获取列表的第一个元素
- LPUSH key value1 [ value2 ]：将一个或者多个值插入到列表头部
- LPUSHX key value1 [ value2 ]：将一个或者多个值插入到已存在的列表头部
- LRANGE key start stop：获取列表指定范围内的元素
- LREM key count value：移除列表元素
- LSET key index value：通过索引设置列表元素的值
- LTRIM key start stop：对一个列表进行修剪（trim），就是说列表只保留指定区间内的元素，不在指定区间内的元素都将被删除
- RPOP key：移除并获取列表最后一个元素
- RPOPLPUSH source destination：移除列表的最后一个元素，并将该元素添加到另一个列表并返回
- RPUSH key value1 [ value2 ]：在列表中添加一个或者多个值
- RPUSHX key value：为已存在的列表添加值



## 3.4、Set

- SADD key member1 [ member2 ]：向集合添加一个或者多个成员
- SCARD key：获取集合的成员数
- SDIFF key1 [ key2 ]：返回给定所有集合的差集
- SDIFFSTORE destination key1 [ key2 ]：返回给定所有集合的差集并存储在destination中
- SINTERSTORE destination key1 [ key2 ]：返回给定所有集合的交集并存储在destination中
- SISMEMBER key member：判断menber元素是否是集合key的成员
- SMEBERS key：返回集合中的所有成员
- SMOVE source destination menber：将member元素从source集合移动到destination集合
- SPOP key：移除并返回集合中的一个随机元素
- SRANDMEMBER key [ count ]：返回集合中一个或者多个随机数
- SREM key member1 [ member2 ]：移除集合中一个或者多个成员
- SUNION key1 [ key2 ]：返回所有给定集合的并集
- SUNIONSTORE destination key1 [ key2 ]：所有给定集合的并集存储在destination集合中
- SSCAN key cursor [ MATCH pattem ] [ COUNT count ]：迭代集合中的元素



## 3.5、Hash

> Map<String,Map<Object,Object>>

- HDEL key field2 [ field2 ]：删除一个或者多个哈希表字段
- HEXISTS key field：查看哈希表中key，指定的字段是否存在
- HGET key field：获取存储在哈希表中指定字段的值
- HGETALL key：获取在哈希表中指定key的所有字段和值
- HINCRBY key field increment：为哈希表key中指定字段的整数值添加上增量increment
- HINCRBYFLOAT key field increment：为哈希表key中指定字段的浮点数值添加上增量increment
- HKEYS：获取所有哈希表中的字段
- HLEN key：获取哈希表中字段的数量
- HMGET key field1 [ field2 ]：获取所有给定字段的值
- HMSET key field1 value1 [ field2 value2 ]：同时将多个field-value（域-值）对设置到哈希表key中
- HSET key field value：将哈希表key中的字段field的值设为value
- HSETNX key field value：只有在字段field不存在时，设置哈希表字段的值
- HVALS key：获取哈希表中所有的值
- HSCAN key cursor [ MATCH pattem ] [ COUNT count ]：迭代哈希表中的键值对





## 3.6、Sorted set（ZSet）

> 在set基础上，每个val值前添加一个score分数值
>
> 之前set是 k1 v1 v2 v3
>
> 现在zset是k1 score1 v1 score2 v2

- ZADD key score menber1 [ score2 menber2 ]：向有序集合添加一个或者多个成员，或者更新已存在的成员分数
- ZCARD key：获取有序集合的元素个数
- ZCOUNT key min max：可计算在有序集合中指定区间分数的成员
- ZINCRBY key increment menber：有序集合中对指定成员的分数加上增量increment
- ZINTERSTORE destination numkeys key [ key... ]：计算给定的一个或者多个有序集的交集并将结果集存储在新的有序集合key中
- ZLEXCOUNT key min max：在有序集合中计算指定字典区间内成员数量
- ZRANGE key start stop [ WITHSCORES ]：通过索引区间返回有序集合成指定区间内的成员
- ZRANGEBYLEX key min max [ LIMIT offset count ]：通过字典区间返回有序集合的成员
- ZRANGEBYSCORE key min max [ WITHSCORES ] [ LIMIT ]：通过分数返回有序集合指定区间内的成员
- ZRANK key menber：返回有序集合中指定成员的索引
- ZREM key menber [ member ]：移除有序集合中的一个或多个成员









## 3.7、Streams

- 队列相关指令
  - XADD：添加消息到队列末尾
  - XTRIM：限制Stream的长度，如果已经超长会进行截取
  - XDEL：删除信息
  - XLEN：获取Stream中的消息长路
  - XRANGE：获取消息队列（可以指定范围），忽略删除的信息
  - XREVRANGE：和XRANGE相比区别在于反向获取，ID从大到小
  - XREAD：获取消息（阻塞/非阻塞），返回大于指定ID的消息
- 消费组相关指令
  - XGROUP CREATE：用于创建消费者组
  - XREADGROUP GROUP：用于消费 Redis Stream 数据结构中的消息，并且允许多个消费者以消费者组的形式进行协调消费
  - XPENDING：查询每个消费组内所有消费者【已读、尚未确认】的消息以及查看某个消费者具体读了哪些数据
  - XACK：向消息队列确认消息处理已完成
- XINFO用于打印Stream\Consumer\Group的详细信息



## 3.8、Geospatial

- GEOADD：多个经度（longitude）、维度（latitude）、位置名称（member）添加到指定的key中
- GEOPOS：从键里面返回所有给定位置元素的位置（经度和维度）
- GEODIST：返回两个给定位置之间的距离
- GEORADIUS：以给定的维度为中心，返回与中心的距离不超过给定最大距离的所有位置元素
- GEORADIUSBYMEMBER：跟GEORADIUS类似
- GEOHASH：返回一个或多个位置元素的Geohash表示





## 3.9、HyperLogLog

- PFADD key element [ element ]：添加指定元素到 HyperLogLog中
- PFCOUNT key [ key... ]：返回给定HyperLogLog的基数估算值
- PFMERGE destkey sourcekey [ sourcekey.... ]：将多个HyperLogLog合并为一个HyperLogLog



## 3.10、Bitmap

> 由0和1状态表现的二进制位的bit数组
>
> 说明：用String类型作为底层数据结构实现的一种统计二值状态的数据类型
>
> 位图本质是数组，它是基于String数据类型的按位的操作。该数组由多个二进制位组成，每个二进制位都赌赢一个偏移量（我们称之为一个索引）。
>
> Bitmap支持的最大位数是2^32位，它可以极大的节约存储空间，使用512M内存就可以存储多大42.9亿的字节信息（2^32 = 4294967296）

- setbit key offset val：给指定key的值的第offset赋值val            时间复杂度：O（1）
- getbti key offset：获取指定key的第offset位                             时间复杂度：O（1）
- bitcount key start end：返回指定key中[ start,end ]中为1的数量                                                   时间复杂度：O（n）
- bitop operation destkey key：对不同的二进制存储数据进行运算（AND、OR、NOT、XOR）  时间复杂度：O（1）



## 3.11、Bitfiled

- BITFIELD key[GET type offset]：该命令允许用户在 Redis 字符串类型中对位进行读取、设置或修改。 "GET type offset" 是该命令中的一个选项，用于指定读取操作的位域类型和偏移量
- BITFIELD key[SET type offset value]：允许您在 Redis 字符串类型中设置位的值。"SET type offset value" 是该命令的选项之一，用于指定要设置的位域类型、偏移量和相应的值
- BITFIELD key[INCRBY type offset increment]：它允许您增加 Redis 字符串类型中指定位的值。"INCRBY type offset increment" 是该命令的选项之一，用于指定要增加的位域类型、偏移量和增量值
- 溢出控制OVERFLOW [WRAP|SAT|FAIL]：在 Redis 中，当执行位域操作时，如果操作导致位域的值超出了其允许的范围，则可以使用"溢出控制"选项来定义处理方式



# 4、Redis持久化

## 4.1、总体介绍

官网地址：https://redis.io/docs/management/persistence/

持久性是指将数据写入持久存储，如固态磁盘（SSD）。Redis提供了一系列的持久化选项。其中包括：

- RDB（Redis Database）：RDB持久性在指定的时间间隔执行数据集的时间点快照。
- AOF（Append Only File，仅附加文件）：AOF持久性记录服务器接收到的每个写操作。然后，可以在服务器启动时再次重放这些操作，从而重新构建原始数据集。命令的记录格式与Redis协议本身相同。
- 无持久性：您可以完全禁用持久性。这有时会在快取时使用。
- RDB + AOF：您也可以在同一个实例中联合收割机AOF和RDB。



## 4.2、持化双雄

### 4.2.1、RDB（Redis DataBase）

- RDB（Redis Database）：RDB持久性在指定的时间间隔执行数据集的时间点快照。
- **手动触发**
  - Redis提供了两个命令来生成RDB文件，分别是save和bgsave
    - Save：在主程序中执行**会阻塞当前redis服务器**，直到持久化工作完成执行save命令期间，Redis不能处理其他命令，**线上禁止使用**
    - ***BGSAVE（默认）***：
      1. Redis会在后台异步进行快照操作，**不阻塞**快照同时还可以响应客户端请求，该出发方式会fork一个子进程由子进程复制持久化过程
      2. [官网说明](https://redis.io/commands/bgsave/)
      3. Redis会使用bgsave对当前内存中的所有数据做快照，这个操作个子进程在后台完成的，这就允许主进程同时可以修改数据
      4. [fork是什么](https://linux.die.net/man/2/fork)？
         1. git角度克隆一分，不影响主进程
         2. 操作系统角度：在linux程序中，fork()会产生一个和父进程完全相同的子进程，但子进程在此后多会exec系统调用，处于效率考虑，尽量避免膨胀。
      5. LASTSAVE：可以通过LASTSAVE命令获取最后一次成功执行快照的时间
         1. [root@localhost dumpfiles]# date -d @1708017890
            2024年 02月 16日 星期五 01:24:50 CST



- **自动触发**
  - Redis7版本，按照redis.conf里配置的 save <seconds><changes>
  - 设定出发时间：save 5 2    =>    5秒内2次修改
  - 修改dump文件保存路径     =>    默认 dir ./    ->   dir /myredis/dumpfiles
  - 修改dump文件名称            =>    默认 dbfilename dump.rdb      ->    dump6379.rdb
  - 触发备份                            =>    两种情况，需要在设定是时间内，并且触发设定的次数，才会触发
  - 如何恢复                            =>    1.将备份文件dump.rdb移动到redis安装目录并启动服务即可。2.备份成功后故意用flushdb清空redis，看看是否可以恢复数据。3.物理恢复，一定服务和备份分机隔离。 （使用了FLUSHDB清空了redis，就会自动保存一分dump.rdb）



**优势：**

- RDB is a very compact single-file point-in-time representation of your Redis data. RDB files are perfect for backups. For instance you may want to archive your RDB files every hour for the latest 24 hours, and to save an RDB snapshot every day for 30 days. This allows you to easily restore different versions of the data set in case of disasters.
- RDB is very good for disaster recovery, being a single compact file that can be transferred to far data centers, or onto Amazon S3 (possibly encrypted).
- RDB maximizes Redis performances since the only work the Redis parent process needs to do in order to persist is forking a child that will do all the rest. The parent process will never perform disk I/O or alike.
- RDB allows faster restarts with big datasets compared to AOF.
- On replicas, RDB supports [partial resynchronizations after restarts and failovers](https://redis.io/topics/replication#partial-resynchronizations-after-restarts-and-failovers).

```markdown
RDB是Redis数据的一个非常紧凑的单文件时间点表示。RDB文件非常适合备份。例如，您可能希望在最近24小时内每小时归档一次RDB文件，并在30天内每天保存一个RDB快照。这使您可以在发生灾难时轻松恢复数据集的不同版本。 
RDB非常适合灾难恢复，是一个可以传输到远程数据中心或Amazon S3（可能加密）的单个紧凑文件。 
RDB最大化了Redis的性能，因为Redis父进程为了持久化所需要做的唯一工作就是派生一个子进程，子进程将完成所有其余的工作。父进程永远不会执行磁盘I/O或类似操作。 
与AOF相比，RDB允许更快地重新启动大数据集。 
在副本上，RDB支持重启和故障转移后的部分重定向。
```

小总结：

1. 适合大规模的数据恢复
2. 按照业务定时备份
3. 对数据完整性和一致性要求不高
4. RDB文件在内存中的加载速度要比AOF快得多





**劣势：**

- RDB is NOT good if you need to minimize the chance of data loss in case Redis stops working (for example after a power outage). You can configure different *save points* where an RDB is produced (for instance after at least five minutes and 100 writes against the data set, you can have multiple save points). However you'll usually create an RDB snapshot every five minutes or more, so in case of Redis stopping working without a correct shutdown for any reason you should be prepared to lose the latest minutes of data.
- RDB needs to fork() often in order to persist on disk using a child process. fork() can be time consuming if the dataset is big, and may result in Redis stopping serving clients for some milliseconds or even for one second if the dataset is very big and the CPU performance is not great. AOF also needs to fork() but less frequently and you can tune how often you want to rewrite your logs without any trade-off on durability.

```markdown
如果你需要在Redis停止工作的情况下最大限度地减少数据丢失的机会（例如停电后），RDB就不好了。您可以在生成RDB的位置配置不同的保存点（例如，在对数据集进行至少5分钟和100次写入之后，您可以有多个保存点）。然而，你通常会每五分钟或更长时间创建一个RDB快照，所以如果Redis因为任何原因没有正确关闭而停止工作，你应该做好丢失最新数据的准备。 
RDB需要经常使用fork（），以便使用子进程在磁盘上持久化。如果数据集很大，fork（）可能会很耗时，如果数据集很大，CPU性能不好，可能会导致Redis停止服务客户端几毫秒甚至一秒。AOF也需要fork（），但频率较低，您可以调整重写日志的频率，而不会对持久性产生任何影响。
```

小总结：

1. 在一定间隔时间做一次备份，所以如果redis意外down掉的话，就会丢失从当前至最近一次快照期间的数据，快照之间的数据会丢失
2. 内存数据的全量同步，如果数据量太大会导致I/O严重影响服务器性能
3. RDB依赖于子进程的fork，在更大的数据集中，这可能会导致服务请求的瞬间延迟。fork的时候内存中的数据克隆了一份，大致2倍的膨胀性，需要考虑





**修复RDB文件：**

```code block
cd /usr/local/bin
```

```code block
redis-check-rdb /myredis/dumpfiles/dump6379.rdb 
```



**哪些情况会触发RDB快照？**

1. 配置文件中 默认的快照设置
2. 手动save/bgsave命令
3. 执行flushall/flushdb命令也会产生dump.rdb文件，但是里面是空的，无意义
4. 执行SHUTDOWN且没有设置开启AOF持久化
5. 主从复制时，主节点自动触发



**如何禁止快照？**

1. 动态停止所有RDB保存规则的方法：redis-cli config set save""
2. 快照禁止



**RDB优化参数**

- 配置文件SNAPSHOTTING模块
  - save<seconds><changes>        => 设置快照发生事件时间
  - dbfilename                                     => dump.rdb文件名
  - dir                                                  => 修改dump文件保存的地址
  - stop-writes-on-bgsave-error          => 默认yse，表示你不在乎数据不一致或有其他的手段发现和控制这种不一致，那么在快照写入失败时，也能确保redis继续接受新的写请求
  - rdbcompression                            => 默认yes，对于存储到磁盘中的快照，可以设置是否进行压缩存储。如果是的话，redis会采用LZF算法进行压缩。如果不你不想消耗CPU来进行压缩的话，可以设置为关闭此功能
  - rdbchecksum                                =>  默认yes，在存储快照后，还可以让redis使用CRC64算法来进行数据校验，但是这样做会增加大约10%的性能消耗，如果希望获取到最大的性能提升，可以关闭此功能
  - rdb-del-sync-files                          => 在没有持久性的情况下删除复制中使用的RDB文件启用。默认情况下no，此选项是禁用的



**总结：**

- RDB是一个非常紧凑的文件。
- RDB在保存RDB文件时父进程唯一需要做的就是fork出一个子进程，接下来的工作全部由子进程来做，父进程不需要再做其他IO操作，所以RDB持久化方式可以最大化redis的性能。
- 与AOF相比，在恢复大的数据集的时候，RDB方式会更快一些。
- *数据丢失风险大。*
- *RDB需要经常fork子进程来保存数据集到硬盘上，当数据集比较大的时候，fork的过程是非常耗时的，可能会导致Redis在一些毫秒级不能响应客户端请求。*

------





### **4.2.2、AOF（Append Only File）**

- AOF (Append Only File): AOF持久性记录服务器接收的每个写操作。然后，可以在服务器启动时再次重放这些操作，从而重新构建原始数据集。命令的记录格式与Redis协议本身相同。
- **以日志的形式来记录每个写操作**，将Redis执行过的所有指令记录下来（读操作不记录），只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就会根据日志文件的内容写指令从前到后执行一次以完成数据的恢复工作。
- 默认情况下，redis是没有开启AOF（append only file）的。
- 开启AOF功能需要设置配置：appendonly yes

```markdown
Snapshotting is not very durable. If your computer running Redis stops, your power line fails, or you accidentally kill -9 your instance, the latest data written to Redis will be lost. While this may not be a big deal for some applications, there are use cases for full durability, and in these cases Redis snapshotting alone is not a viable option.
```

快照不是很持久。如果运行Redis的计算机停止运行，电源线出现故障，或者你意外地杀死了你的实例，最新写入Redis的数据将丢失。虽然这对某些应用程序来说可能不是什么大问题，但也有完全持久性的用例，在这些情况下，单独使用Redis快照并不是一个可行的选择。



**AOF持久化工作流程**

1. Client作为命令的来源，会有多个源头以及源源不断的请求命令。
2. 在这些命令到达Redis Server以后并不是直接写入AOF文件，会将其这些命令有限放入AOF缓存中进行保存。这里的AOF缓冲区实际上是内存中的一片区域，存在的目的当这些命令到达一定量以后再写入磁盘，避免频繁的磁盘IO操作。
3. AOF缓冲会根据AOF缓冲区**同步文件的三种协会策略**将命令写入磁盘上的AOF文件。
4. 随着写入AOF内容的增加为了避免文件膨胀，会根据规则机型命令的合并（又称**AOF重写**），从而起到AOF文件压缩的目的。
5. 当Redis Server服务器重启的时候会从AOF文件载入数据。



**AOF缓冲区三种协会策略**

1. Always：同步写回，每个命令执行完立刻同步地将日志写回磁盘。（频繁的磁盘IO）
2. **everysec**：每秒写回，每个写命令执行完，只是先把日志写到AOF文件的内存缓冲区，每隔一秒把缓冲区中的内容写入磁盘。
3. no：操作系统控制着写回，每个写命令执行完，只是先把日志写到AOF文件内存缓冲区，由操作系统决定何时将缓冲区内容写回磁盘。（数据容易丢失）

|  配置项  |      写回时机      |           优点           |               缺点               |
| :------: | :----------------: | :----------------------: | :------------------------------: |
|  Always  |      同步写回      | 可靠性高，数据基本不丢失 | 每个写命令都要落盘，性能影响较大 |
| Everysec |      每秒写回      |         性能适中         |      宕机时丢失1秒内的数据       |
|    No    | 操作系统控制的写回 |          性能好          |        宕机时丢失数据较多        |



**开启流程**

myredis.conf

```conf
appendonly yes
```

```conf
appendfsync everysec
```

redis6：rdb文件和aof文件都放在同一个文件夹

redis7：默认 /myredis/appendonlydir/xxx.aof文件

```conf
# The working directory.
#
# The DB will be written inside this directory, with the filename specified
# above using the 'dbfilename' configuration directive.
#
# The Append Only File will also be created inside this directory.
#
# Note that you must specify a directory here, not a file name.
dir /myredis/dumpfiles
```



```conf
# appendonly.aof.1.base.rdb as a base file.
# - appendonly.aof.1.incr.aof, appendonly.aof.2.incr.aof as incremental files.
# - appendonly.aof.manifest as a manifest file.
```

- BASE：表示基础AOF，它一般由子进程通过重写产生，该文件最多只有一个
- INCR：表示增量AOF，它一般会在AOFRW开始执行时被创建，该文件可能存在多个
- HISTORY：表示历史AOF，它由BASE和INCR AOF变化而来，每次AOFRW成功完成时，本次AOFRW之前对应的BASE和INCR AOF都将变为HISTORY，HISTORY类型的AOF会被Redis自动删除。

为了管理这些AOF文件，我们引入了一个manifest（清单）文件来跟踪、管理这些AOF。同时，为了便于AOF备份和靠背，我们将所有的AOF文件和manifest文件放入一个单独的文件目录中，目录名由appenddiranme配置（Redis 7.0新增配置项）决定。



**修复**

```code
redis-check-aof appendonly.aof.1.incr.aof
```

**异常修复**

```
redis-check-aof --fix appendonly.aof.1.incr.aof
```



**优势：**

- Using AOF Redis is much more durable: you can have different fsync policies: no fsync at all, fsync every second, fsync at every query. With the default policy of fsync every second, write performance is still great. fsync is performed using a background thread and the main thread will try hard to perform writes when no fsync is in progress, so you can only lose one second worth of writes.
- The AOF log is an append-only log, so there are no seeks, nor corruption problems if there is a power outage. Even if the log ends with a half-written command for some reason (disk full or other reasons) the redis-check-aof tool is able to fix it easily.
- Redis is able to automatically rewrite the AOF in background when it gets too big. The rewrite is completely safe as while Redis continues appending to the old file, a completely new one is produced with the minimal set of operations needed to create the current data set, and once this second file is ready Redis switches the two and starts appending to the new one.
- AOF contains a log of all the operations one after the other in an easy to understand and parse format. You can even easily export an AOF file. For instance even if you've accidentally flushed everything using the [`FLUSHALL`](https://redis.io/commands/flushall) command, as long as no rewrite of the log was performed in the meantime, you can still save your data set just by stopping the server, removing the latest command, and restarting Redis again.

```markdown
使用AOF Redis更持久：你可以有不同的fsync策略：完全不fsync，每秒fsync，每次查询fsync。使用fsync每秒一次的默认策略，写入性能仍然很好。fsync是使用后台线程执行的，当没有fsync正在进行时，主线程将努力执行写入，因此您只能丢失一秒钟的写入。 
AOF日志是只附加日志，因此没有查找，如果断电也没有损坏问题。即使由于某种原因（磁盘已满或其他原因）日志以半写命令结束，redis-check-aof工具也能够轻松修复它。
当AOF变得太大时，Redis能够在后台自动重写AOF。重写是完全安全的，因为当Redis继续追加到旧文件时，会产生一个全新的文件，其中包含创建当前数据集所需的最少操作集，一旦第二个文件准备就绪，Redis就会切换这两个文件并开始追加到新文件。 
AOF以易于理解和解析的格式依次包含所有操作的日志。您甚至可以轻松导出AOF文件。例如，即使你使用FLUSHALL命令意外地刷新了所有内容，只要在此期间没有重写日志，你仍然可以保存你的数据集，只要停止服务器，删除最新的命令，重新启动Redis。
```

小总结：

1. 更好的保护数据不丢失
2. 性能高
3. 可做紧急恢复



**劣势**

- AOF files are usually bigger than the equivalent RDB files for the same dataset.
- AOF can be slower than RDB depending on the exact fsync policy. In general with fsync set to *every second* performance is still very high, and with fsync disabled it should be exactly as fast as RDB even under high load. Still RDB is able to provide more guarantees about the maximum latency even in the case of a huge write load.

```markdown
AOF文件通常比相同数据集的等效RDB文件大。 
AOF可能比RDB慢，这取决于确切的fsync策略。一般来说，fsync设置为每秒一次的性能仍然非常高，禁用fsync时，即使在高负载下，它也应该与RDB一样快。尽管如此，RDB仍然能够提供更多关于最大延迟的保证，即使在巨大的写入负载的情况下。
```

小总结：

1. 相同数据集的数据而言aof文件远要大于rdb文件，恢复速度慢于rdb
2. aof运行效率要慢于rdb，每秒同步策略效率较好，不同步效率和rdb相同



**AOF的重写机制**

`是什么？`

由于AOF持久化是Redis不断将写命令记录到AOF文件中，随着Redis不断地进行，AOF的文件会越来越大，文件越大。占用服务器内存越大以及AOF恢复要求时间越长。

为了解决这个问题，**Redis新增了重写机制**，当AOF文件的大小超过设定的峰值时，Redis就会**自动**启动AOF文件的内容压缩，只保留可以恢复数据的最小值令集，或者可以**手动使用命令bgrewriteaof来重写**。

- 自动重写

  ```conf
  # 100 是百分比
  # auto-aof-rewrite-percentage 100
  # auto-aof-rewrite-min-size 64mb
  # 1.根据上次重写后的aof大小，判断当前aof大小是不是增长了一倍
  # 2.重写时满足的文件大小
  
  auto-aof-rewrite-percentage 100
  auto-aof-rewrite-min-size 1k
  
  ```

  注意，**同时满足，且的关系**才会触发

  ***启动AOF文件的内容压缩，只保留可以恢复数据的最小指令集***

  ```conf
  # 关闭混合，设置为no
  aof-use-rdb-preamble no
  ```

  

- 手动重写

  - bgrewriteao

小总结

​		**也就是说AOF文件重写并不是对源文件进行重新整理，而是直接读取服务器现有的键值对，然后用一条命令去代替之前记录这个键值对的多条命令，生成一个新的文件后去替换原来的AOF文件。**

​		AOF文件重写触发机制：通过redis.conf配置文件中的auto-aof-rewrite-percentage：默认值为100，以及auto-aof-rewrite-min-size：64mb配置，也就是说默认Redis会记录上一次重写时AOF的大小，**默认配置是当AOF文件大小是上一次rewrite后大小的一倍且文件大于64M时触发**。





**重写原理**

1. 在重写开始前，redis会创建一个“重写子进程”，这个子进程会读取现有的AOF文件，并将其包含的指令进行分析压缩并写入到一个临时文件中。
2. 与此同时，主进程会将新接收到的写指令一边累积到内存缓冲区中，一边继续写入到原有的AOF文件中，这样做是保证原有的AOF文件可用性，避免在重写过程中出现意外。
3. 当“重写子进程”完成重写工作后，它会给父进程发一个信号，父进程接收到信号后就会将内存中缓存的写指令追加到新的AOF文件中。
4. 当追加结束后，Redis就会用新的AOF文件来代替旧的AOF文件，之后再有新的写指令，就都会追加到新的AOF文件中。
5. 重写AOF文件的操作，并没有读取旧的AOF文件，而是将整个内存中的数据库内容用命令的方式重写了一个新的AOF文件，这点和快照有点类似。

| 配置指令                    | 配置含义                   | 配置示例                        |
| --------------------------- | -------------------------- | ------------------------------- |
| appendonly                  | 是否开启aof                | appendonly yes                  |
| appendfilename              | 文件名称                   | appendfilename “appendonly.aof” |
| appendfsync                 | 同步方式                   | appendfsync everysec/always/no  |
| no-appendfsync-no-rewrite   | aof重写期间是否同步        | no-appendfsync-on-rewriter no   |
| auto-aof-rewrite-percentage | 重写触发配置、文件重写策略 | auto-aof-rewrite-percentage 100 |
| auto-aof-rewrite-min-size   | 重写触发配置、文件重写策略 | auto-aof-rewrite-min-size 64mb  |



总结：

- AOF文件是一个只进行追加的日志文件。
- Redis可以在AOF文件体积变得过大时，自动地在后台对AOF进行重写。
- AOF文件有序地保存了对数据库执行的所有写入操作，这些写入操作以Redis协议的格式保存，因此AOF文件的内容非常容易被人读懂，对文件进行分析也很轻松。
- 对于相同的数据集来说，AOF文件的体积通常要大于RDB文件的体积。
- 根据所使用的fsync策略，AOF的速度可能会慢于RDB文件。

------



## 4.3、RDB-AOF混合持久化

> **RDB + AOF**: You can also combine both AOF and RDB in the same instance.
>
> RDB + AOF：您也可以在同一个实例中联合收割机AOF和RDB。

**数据恢复顺序和加载流程**

在同时开启rdb和aof持久化时，重启时只会加载aof文件，不会加载rdb文件



RDB持久化方式能够在指定的时间间隔能对数据进行快照存储

AOF持久化方式记录每次对服务器写的操作，当服务器重启的时候会重新执行这些命令来恢复原始的数据，AOF命令以Redis协议追加保存每次写的操作到文件末尾。



**同时开启两种持久化方式**

在这种情况下，当Redis重启的时候会优先载入AOF文件来恢复原始的数据，因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整。

RDB的数据不实时，同时使用两者时服务器重启也只会找AOF文件。**那要不要只使用AOF？**

**作者建议不要**，因为RDB更适合用于备份数据库（AOF在不断变化不好备份），留着RDB作为一个万一的手段。



**RDB+AOF混合方式**

结合了RDB和AOF的优点，既能快速加载又能避免丢失过多的数据。





## 4.4、纯缓存模式

**同时关闭RDB+AOF**

- save “”
  - 禁用RDB
  - 禁用RDB持久化模式下，我们仍然可以使用命令save，bgsave生成rdb文件
- appendonly no
  - 禁用AOF
  - 禁用AOF持久化模式下，我们仍然可以使用命令bgrewriteaof生成AOF文件

# 5、Redis事务

事务：[官网](https://redis.io/docs/interact/transactions/)

> 可以一次执行多个命令，本质是一组命令的集合。一个事务中的所有命令都会序列化，**按顺序地串行化执行而不会被其他命令插入，不许加塞**。



**Redis事务VS数据库事务**

|                    | 一个队列中，一次性、顺序性、排他性的执行一系列命令           |
| ------------------ | ------------------------------------------------------------ |
| 单独的隔离操作     | Redis的事务仅仅是保证食物里的操作会被连续独占的执行，redis命令执行是单线程架构，在执行完事务内所有指令前是不可能再去同时执行其他客户端的请求的 |
| 没有隔离级别的概念 | 因为事务提交前任何指令都不会被实际执行，也就不存在“事务内的查询要看到事务里的更新，在事务外查询不能看到”这种问题了 |
| 不保证原子性       | Redis的事务**不保证原子性**，也就是不保证所有指令同时成功或失败，只有决定是否开启执行全部指令的能力，没有执行到一半进行回滚的能力 |
| 排它性             | Redis会保证一个事务内的命令依次执行，而不会被其他命令插入    |



**常用命令**

- DISCARD：取消事务，放弃执行事务块内的所有命令
- EXEC：执行所有事务块内的命令
- MULTI：标记一个事务块的开始
- UNWATCH：取消WATCH命令对所有key的监视
- WATCH key[ key... ]：监视一个（或多个）key，如果在事务执行之前这个（或这些）可以被其他命令所改动，那么事务将被打断



**case 1：正常执行**

- MULTI
- EXEC

**case 2：方式事务**

- MULTI
- DISCARD

**case 3：全体连坐**

- 在事务期间，可能会遇到两种类型的命令错误： 
  - 命令可能无法排队，因此在调用EXEC之前可能会出现错误。例如，命令可能语法错误（参数数量错误、命令名称错误等），或者可能存在某些临界条件，如内存不足条件（如果服务器被配置为具有使用maxmemory指令的内存限制）。 
  - 在调用EXEC之后，命令可能会失败，例如，因为我们对具有错误值的键执行了操作（就像对字符串值调用列表操作）。

**case 4：冤头债主**

- 对的执行错的停
- 语法没错

**case 5：watch监控**

- Redis使用watch来提供乐观锁，类似于CAS（Check-and-set）
  - 悲观锁：Pessimistic Lock，顾名思义，就是很悲观，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿到这个数据就会block直到它拿到锁。
  - **乐观锁**：Optimistic Lock，顾名思义，就是很乐观，每次去拿数据的时候都认为别人不会修改，**所以不上锁**，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据。
    - **乐观锁策略：提交版本必须 大于 记录当前版本才能执行更新。**
  - CAS：使用check-and-set的乐观锁定 WATCH用于为Redis事务提供检查和设置（CAS）行为。 监视被监视的键以检测针对它们的更改。如果在执行EXEC命令之前至少修改了一个被监视的键，则整个事务将中止，EXEC将返回一个拒绝应答，通知事务失败。
- watch
- unwatch
- 小结
  - 一旦执行了exec之前加的监控锁都会被取消掉了
  - 当客户端连续丢失的时候（比如退出链接），所有东西都会被取消监视



**总结**

1. 开启：以为MULTI开始一个事务
2. 入队：将多个命令入队到事务中，接到这些命令并不会立即执行，而是放到等待执行的事务队列里面
3. 执行：由EXEC命令出发事务

# 6、Redis管道

官网：[管道](https://redis.io/docs/manual/pipelining/)

> 管道（pipeline）可以一次性发送多条命令给服务端，服务端依次处理完成后，**通过一条响应一次性将结果返回，通过减少客户端与redis的通信次数来实现降低往返延时时间**，pipeline实现的原理是队列，先进先出特性就保证数据的顺序性。

*批处理命令变种优化措施*，类似于Redis的原生批命令（mget和mset）

```black code
cat cmd.txt | redis-cli -a zzq123456 --pipe
```



**总结**

- Pipeline与原生批量命令对比
  - 原生批量命令是原子性（例如：：mset，mget），**Pipeline是非原子性**
  - 原生批量命令一次只能执行一种命令，Pipeline支持批量执行不同命令
  - 原生批量命令是服务端实现，而Pipeline需要服务端与客户端共同完成
- Pipeline与事务对比
  - 事务具有原子性，管道不具有原子性
  - 管道一次性将多条命令发送到服务器，事务是一条一条的发，事务只有在接收到exec命令后才会执行，管道不会
  - 执行事务时，会阻塞其他命令的执行，而执行管道中的命令时不会
- 使用Pipeline注意事项
  - Pipeline缓冲的指令只是会一次执行，不保证原子性，如果执行中指令发生异常，将会继续执行后续的指令
  - 使用Pipeline组装的命令个数不能太多，不然数据量过大客户端阻塞的时间可能过久，同时服务端此时也被迫恢复一个队列答复，占用很多内存

# 7、Redis发布订阅

官网：[Pub/Sub](https://redis.io/docs/interact/pubsub/)

> 是一种消息通信模式：发送者（PUBLISH）发送消息，订阅者（SUBSCRIBE）接收消息，可以实现进程间的消息传递
>
> Redis可以实现消息中间件MQ的功能，通过发布订阅实现消息的引导和分流。



**常用命令**

- PSUBSCRIBE pattem [ pattem ]：订阅一个或多个符合给定模式的频道
- PUBSUB subcommand [ argument [ argument... ] ]：查看订阅与发布系统状态
- PUBLSH channel message：将信息发送到指定的频道
- PUNSUBSCRIBE [ pattem [ pattem..... ] ]：退订所有给定模式的频道
- SUBSCRIBE channel [ channel ...]：订阅给定的一个或多个频道的信息
- UNSUBSCRIBE channel [ channel ...]：退订给定的频道



# 8、Redis复制（replica）

## 8.1、理论简介

官网：[replication](https://redis.io/docs/management/replication/)

> 就是主从复制，master以写为主，slave以读为主
>
> 当master数据变化的时候，自动将新的数据异步同步到其他slave数据库

`作用`

- 读写分离
- 容灾恢复
- 数据备份
- 水平扩容支撑高并发



1. 配从（库）不配（库）
2. 权限细节
   - master如果配置了requirepass参数，需要密码登录
   - 那么slave就要配置masterauth来设置校验密码，否则的话master会拒绝slave的访问请求
3. 基本操作命令
   - info replication：可以查看复制结点的主从关系和配置信息
   - replicaof 主库IP 主库端口：一般写入redis.conf配置文件内
   - slaveof 主库IP 主库端口：
     - 每次与master断开后，都需要重新俩连接，除非你配置近redis.conf文件
     - 在运行期间修改slave节点的信息，如果该数据库已经是某个主数据库的从数据库，那么会停止和原主数据库的同步关系**转而和新的主数据库同步，重新拜码头**。
   - slaveof no one：使当前数据库停止与其他数据库的同步，**转成主数据库，自立为王**。



## 8.2、案例演示

- 架构说明
  1. 一个Master两个Slave   =》  3台虚拟机，每台都安装redis
  2. 配置多个redis.conf文件
     1. redis6379.conf
     2. redis6380.conf
     3. redis6381.conf
  
- 小口诀
  - 三边网络相互ping通且**注意防火墙配置**
  - 三大命令
  
- 修改配置文件细节操作
  - redis6379.conf
    1. 开启daemonize yes
    2. 注释掉bind 127.0.0.1
    3. protected-mode no
    4. 指定端口
    5. 指定当前工作目录，dir
    6. pid文件名，pidfile
    7. log文件名，logfile
    8. requirepass
    9. dump.rdb名字
    10. aof文件，appendfilename
    11. **replicaof 主机号 主机端口，从机访问主机的通行密码masterauth，必须**（从机需要配置，主机不用）
  
- 常用3招

  - 一主二仆
    - 方案1：配置文件固定写死
      1. 配置文件执行  =》  replica of 主库IP 主库端口
      2. 配从不配主：
         - 配置从机6380
         - 配置从机6381
      3. 先master后两台slave依次启动
      4. 主从关系查看
         - 日志
           - 主机日志
           - 备机日志
         - 命令
           - info replication命令查看
    - 主从问题
      1. 从机可以执行写命令吗？                    ==》 从机只可以读不可以写
      2. 从机切入点问题                                  ==》  Y，掉队的slave也是可以拿到之前master写入的数据的
      3. 从机shutdown后，从机会上位吗        ==》  No，等待
      4. 主机shutdown后，重启后主从关系还在吗？从机还能否顺利复制？    ==》 Y
      5. 某台从机down后，master继续，从机继续重启后它能跟上大部队吗？==》 Y
    - 方案2：命令操作手动指定
      1. 从机停机去掉配置文件中的配置项，3台虚拟机目前都是主机状态，各不从属
      2. 3台master
      3. 预设的从机上执行命令
         - **slaveof 主库IP 主库端口**
      4. 用命令使用的话，2台从机重启后，关系还在吗？  ==》  N
    - 配置 VS 命令的区别
      - 配置，持久稳定
      - 命令，当次生效

  

  

  - 薪火相传
    - 上一个slave可以是下一个slave的master，slave同样可以接收其他slaves的连接和同步请求，那么该slave作为链条中下一个的master，可以有效减轻主master的写的压力
    - **中途变更转向：会清除之前的数据，重新建立拷贝最新的**
    - slaveof 新主库IP 新主库端口

  

  

  - 反客为主
    - SLAVEOF no one   ==》  使当前数据库与其他数据库脱离关系



## 8.3、复制原理和工作流程

1. slave启动，同步初请
   - slave启动成功连接到master后会发送一个sync命令
   - slave首次全新连接master，一次完全同步（全量复制）将被自动执行，slave自身原有数据会被master数据覆盖清楚
2. 首次连接，全量复制
   - master结点收到sync命令后会开始在后台保存快照（即RDB持久化，主从复制时会触发RDB），同时手机所有接收到的用于修改数据集命令缓存起来，master结点执行RDB持久化完后，master将RDB快照文件和所有缓存的命令发送到所有slave，以完成一次完全同步
   - 而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中，从而完成复制初始化
3. 心跳持续，保持通信
   - repl-ping-replica-period 10  =》master发出PING包的周期，默认是10秒
4. 进入平稳，增量复制
   - master继续将心的所有收集到的修改命令自动依次传给slave，完成同步
5. 从机下线，重连续传
   - master会检查backlog里面的offset，master和slave都会保存一个复制的offset还有一个masterId，offset是保存在backlog中的。**master只会把已经复制的offset后面的数据复制给slave**，类似断点续传。





## 8.4、复制的缺点

1. 复制延时，信号衰减
   - 由于所有的写操作都是现在master上操作，然后同步更新到slave上，所以从master同步到slave机器上有一定的延迟，当系统很繁忙的时候，延迟问题会更加严重，slave机器数量的增加也会使这个问题更加严重。
2. master挂了如何办？
   -  默认情况下，不会在slave节点中自动重选一个master
   - 每次都要人工干预？  ==》  **无人值守安装变成刚需**



# 9、Redis哨兵（sentinel）

## 9.1、理论简介

官网：[Sentinel](https://redis.io/docs/management/sentinel/)

>   吹哨人巡查监控后台master主机是否故障，如果故障了根据**投票数**自动将某一个从库转换为新主库，继续对外服务。

`作用`

- 监控redis运行状态，包括master和slave
- **当master down机，鞥自动将slave切换成新的master**



## 9.2、哨兵能干嘛？

```markdown
This is the full list of Sentinel capabilities at a macroscopic level (i.e. the big picture):

	Monitoring. Sentinel constantly checks if your master and replica instances are working as expected.
	
	Notification. Sentinel can notify the system administrator, or other computer programs, via an API, that something is wrong with one of the monitored Redis instances.
	
	Automatic failover. If a master is not working as expected, Sentinel can start a failover process where a replica is promoted to master, the other additional replicas are reconfigured to use the new master, and the applications using the Redis server are informed about the new address to use when connecting.
	
	Configuration provider. Sentinel acts as a source of authority for clients service discovery: clients connect to Sentinels in order to ask for the address of the current Redis master responsible for a given service. If a failover occurs, Sentinels will report the new address.
```

1. 主从监控：监控主从redis库运行是否正常
2. 消息通知：哨兵可以将故障转移的结果发送给客户端
3. 故障转移：如果master异常，则会进行主从切换，将其中一个slave作为新master
4. 配置中心：客户端通过连接哨兵来获取当前redis服务的主节点地址



## 9.3、案例演示

1. Redis Sentinel架构

   1. 三个哨兵
      - 主动监控和维护集群，不存放数据，只是吹哨人
   2. 一主二从
      - 用于数据读取和存放

2. 案例步骤

   1. **/myredis目录下新建或者拷贝sentinel.conf文件，名字绝不能错**

   2. 先看看/opt目录下默认的sentinel.conf文件的内容

   3. 重点参数项说明

      1. bind：服务监听地址，用于客户端连接，默认本机地址
      2. daemonize：是否以后台daemon方式运行
      3. protected-mode：安全保护模式
      4. port：端口
      5. logfile：日志文件路径
      6. pidfile：pid文件路径
      7. dir：工作目录
      8. sentinel monitor <master-name><ip><redis-port><quorum>
         1. 设置要监控的master服务器
         2. quorum表示最少有几个哨兵认可客观下线，同意故障迁移的法定票数
         3. quorum：确认客观下线的最少的哨兵数量
      9. sentinel auth-pass <master-name><password>
         1. master设置了密码，连接maste服务的密码

   4. 哨兵sentinel文件通用配置

      1. ```conf
         bind 0.0.0.0
         daemonize yes
         proected-mode no
         port 26379
         logfile "/myredis/sentinel26379.log"
         pidfile /var/run/redis-sentinel26379.pid
         dir /myredis
         sentinel monitor mymaster 192.168.129.132 6379 2
         sentinel auth-pass mymaster zzq12346
         ```

         

      2. sentinel26379.conf

      3. sentinel26380.conf

      4. sentinel26381.conf

      5. master主机配置文件说明

   5. 先启动一主二从3个redis实例，测试正常的主从复制

   6. ==========以下是哨兵内容部分==============

   7. 再启动三个哨兵，完成监控

      1. redis-sentinel sentinel26379.conf --sentinel
      2. redis-sentinel sentinel26380.conf --sentinel
      3. redis-sentinel sentinel26381.conf --sentinel

   8. 启动三个哨兵监控后，再测试一次主从复制

   9. 原有的master挂了

      1. 手动关闭6379服务器，模拟master挂了
      2. 思考问题
         1. 两台**从机**数据是否OK   ==》  YES
         2. 是否会从剩下的2台机器上选出新的master   ==》   YES
         3. 之前down机的master机器重新回来，谁将会是新老大？会不会双master冲突？ ==》选举出来的从机是新老大   ==》  不会，旧的master会变成slave

   10. 对比配置文件

       1. vim sentinel26379.conf
       2. 老master，vim redis6379.conf
       3. 新master，vim redis6381.conf
       4. 结论
          1. 文件的内容，在运行期间会被sentinel动态进行更改
          2. master-slave切换后，master_redis.conf、slave_redis.conf和sentinel.conf的内容都会发生改变，即master_redis.conf中会多一行slaveof的配置，sentinel.conf的监控目标会随之调换

3. 其他备注

   1. 生产都是不用机房不同服务器，很少出现3个哨兵全部挂掉的情况
   2. 可以同时监控多个master，一行一个





## 9.4、哨兵运行流程和选举原理

> 当一个主从配置中的master失效之后，sentinel可以选举出一个新的master用于自动接替原master的工作，主从配置中的其他redis服务器自动指向新的mmaster同步数据。
>
> 一般建议sentinel采取奇数台，防止某一台sentinel无法连接到master导致误切换

**流程切换 | 故障切换**

1. 三个哨兵监控一主二从
2. SDown主观下线（Subjectively Down）
   1. SDown（主观不可用）是一个**单个sentinel自己主观上**检测到的关于master的状态，从sentinel的角度来看，如果发送了PING心跳后，在一定时间内没有收到合法的回复，就达到了SDown的条件。
   2. sentinel配置文件中的down-after-milliseconds设置了判断主观下线的时间长度。
3. ODown客观下线（Objectively Down）
   1. ODown需要一定数量的Sentinel，**多个哨兵达成一致意见**才能认为一个master客观上已经宕掉
4. 选举出领导者哨兵（哨兵中选出兵王）
   1. 当主节点被判断客观下线以后，各个哨兵节点都会进行协商，先选举出一个**领导者哨兵节点（兵王）**并由该领导者节点，也即被选举出来的兵王进行failover（故障迁移）
   2. 哨兵领导者，兵王如何选出来的？
      1. Raft算法
5. 由兵王开始推动故障切换流程并选出一个新的master
   1. 三步骤
      1. 新主登基
         1. **某个Slave被选中成为新master**
         2. 选出新master的规则，剩余slave节点健康前提下
            1. redis.conf文件中，优先级slave-priority或者replica-priority最高的从节点（数字最小优先级越高）
            2. 复制偏移位置offset最大的从节点 ==》 复制量
            3. 最小Run ID的从节点 ==》 字典顺序，ASCII码
      2. 群臣俯首
         1. **一朝天子一朝臣，换个码头重新拜**
         2. 在执行slave no one命令让选出来的从节点成为新的主节点，并通过slaveof命令让其他节点成为其从节点
         3. Sentinel leader会对选举出来的新master执行slave no one操作，将其提升为master节点
         4. Sentinel leader向其他slave发送命令，让剩余的slave成为新的master节点的slave
      3. 旧主拜服
         1. **老master回来也认怂**
         2. 将之前已下线的老master设置为新选出来的新master的从节点，当老master重新上线后，它会成为新master的从节点
         3. Sentinel leader会让原来的master降级为slave并回复正常工作
   2. 小总结
      1. 上述的failover操作均由Sentinel自己独立完成，完全无需人工干预



**说明：**

1. 所谓的**主观下线**（Subjectively Down，简称SDOWN）指的是**单个Sentinel实例**对服务器做出的下线判断，即单个Sentinel认为某个服务下线（有可能是接收不到订阅，之间的网络不同等等原因）。主观下线就是说如果服务器在【  Sentinel down-after-milliseconds】给定的毫秒数之内没有回应PING命令或者返回一个错误信息，那么这个Sentinel会主观的（**单方面的**）认为这个master不可用了。

2. **quorum这个参数是进行客观下线的一个依据**，法定人数/法定票数

   意思是至少有quorum个Sentinel认为这个master有故障才会对这个master进行下线以及故障转移。因为有的时候，某个Sentinel结点可能因为自身的网络原因导致无法连接master，而此时master并没有出现故障，所以这就需要多个sentinel都一致认为该master有问题，才可以进行下一步操作，这就保证了公平性和高可用。

3. **Raft算法**：监视该主节点的所有哨兵都有可能被选为领导者，选举使用的算法是Raft算法；Raft算法的基本思路是**先到先得**。即在一轮选举中，哨兵A向哨兵B发送成为领导者的申请，如果B没有同意过其他哨兵，则会同意A成为领导者。





## 9.5、哨兵使用建议

- 哨兵节点的数量应为多个，哨兵本身应该集群，保证高可用
- 哨兵节点数应该是奇数
- 各个哨兵结点的配置应一致
- 如果哨兵节点部署在Docker等容器里面，尤其要注意端口号的正确映射
- **哨兵集群+主从复制，并不能保证数据零丢失**   ==》  **承上启下引出集群**



# 10、Redis集群（cluster）

官网：[Cluster](https://redis.io/docs/reference/cluster-spec/)

> **由于数据量过大，单个master复制集**难以承担，因此需要对多个复制集进行集群，形成水平扩展每个复制集只负责存储整个数据集的一部门，这就是Redis的集群，其作用是提供在多个Redis节点间共享数据的程序集。

Redis集群是一个提供在多个Redis节点间共享数据的程序集。

**Redis集群可以支持多个master**



## 10.1、集群能干嘛

- Redis集群支持多个master，每个master又可以挂载多个slave
  - 读写分离
  - 支持数据的高可用
  - 支持海量数据的读写存储操作
- 由于cluster自带Sentinel的故障转移机制，内置了高可用的支持，**无需再去使用哨兵功能**
- 客户端与Redis的节点连接，不在需要连接集群中所有的节点，只需要任意连接集群中的一个可用节点即可
- **槽位slot**负责分配到各个物理服务结点，由对应的集群来负责维护结点、插槽和数据之间的关系



## 10.2、集群分片之槽位slot

官网：[slot](https://redis.io/docs/reference/cluster-spec/)

- **redis集群的槽位slot**

  - Redis集群的数据分片

    Redis集群没有使用一致性hash，而是引入了**哈希槽**的概念。

    Redis集群有16384个哈希槽，每个key通过CRC16校验后对1638取模来决定放置哪个槽，集群的每个节点负责一部分hash槽

- **redis集群的分片**

  - 分片是什么？=》 使用Redis集群时我们会将存储的数据分散到多台redis机器上，这称为分片。简而言之，集群中的每个Redis实例都被认为是整个数据的一个分片。
  - 如何找到给定Key的分片？ =》 为了找到给定Key的分片，我们对key进行**CRC16（key）**算法处理并通过对总分片数量取模。然后，**使用确定性哈希函数**，这意味着给定的key**将多次始终映射到同一个分片**，我们可以推断将来读取特定key的位置。

- **它们的优势**

  - 最大优势，方便扩容和数据分派查找

    这种结构很容易添加或删除节点，比如我如果想新添加个节点D。我需要从节点A，B，C中得到部分槽到D上，如果我想要移除节点A，需要将A中的槽移到B和C节点上，然后将没有任何槽点的A节点从集群中移除即可。由于从一个节点将哈希槽移动到另一个节点并不会停止服务，所以无论添加删除或改变某个节点的哈希槽的数量都不会造成集群不可用的状态。

- **slot槽位映射，一般业界有三种解决方案**

  - 哈希取余分区

    - 优点：简单粗暴，直接有效，只需要预估好数据规划好节点，例如3台、8台、10台，就能保证一段时间的数据支撑。使用Hash算法让固定的一部分请求落到同一台服务器上，这样每台服务器固定处理一部分请求（并维护这些请求的信息），起到负载均衡+分而治之的作用。

    - 缺点：原来规划好的接地啊，进行扩容或者缩容就比较麻烦了，不管扩缩，每次数据变动导致节点有变动，映射关系需要重新进行计算，在服务器个数固定不变时没有问题，如果需要弹性扩容或故障停机的情况下，原来的取模公式就会发生变化；**Hash（key）/3会变成Hash（key）/?**。此时地址经过取余运算的结果将发生很大变化，根据公式获取的服务器也会变得不可控。

      某个redis机器宕机了，由于台数数量变化，导致hash取余全部数据重新洗牌。

  - 一致性哈希算法分区

    - 一致性Hash算法背景：一一致性哈希算法在1997年由麻省理工学院中提出的，设计目标是为了解决**分布式缓存数据变动和映射问题，某个机器宕机了，分母数量改变了，自然取余数不OK了。**
    - 能干嘛：提出一致性Hash解决方案。目的是当服务器个数发生变动时，尽量减少影响客户端到服务端的映射关系。
    - **3大步骤**：
      1. 算法构建[一致哈希环](https://www.cnblogs.com/ghx-kevin/p/12950912.html)
         - 一致性哈希算法必然有一个hash函数并按照算法产生hash值，这个算法的所有可能哈希值会构成一个全量集，这个集合可以成为一个hash空间[0,2^32-1]，这个是一个线性空间，但是在算法中，我们通过适当的逻辑控制将它**首尾相连**（0=2^32），这样让它逻辑上形成了一个环形空间。
      2. 服务器IP节点映射
         - 将集群中各个IP节点映射到环上的某一个位置
      3. key落到服务器的落键规则
         - 当我们需要存储一个kv键值对时，首先计算key的hash值，hash（key），将这个key使用相同的函数Hash计算出哈希并确定此数据在环上的位置，**从此位置沿顺时针“行走”**，第一次遇到服务器就是其应该定位到的服务器，并将该键值对存储在该节点上。
    - 优点与缺点
      - 优点：
        - 一致性哈希算法的**容错性**
        - 一致性哈希算法的**扩展性**
      - 缺点
        - 一致性哈希算法的**数据倾斜**问题：一致性Hash算法在服务**节点太少时**，容易因为节点分布不均匀而造成**数据倾斜**（被缓存的对象大部分集中缓存在某一台服务器上）问题 
    - 小总结：
      - 为了在节点数目发生变化改变时尽可能减少的迁移数据
        - 将所有的存储节点排列在首位相接的hash环上，每个key在计算hash后会**顺时针**找到临近的存储节点存放。而当有节点加入或退出时仅影响该节点上Hash环上**顺时针相邻的后续节点**。
      - 优点：加入和删除节点只影响哈希环中顺时针方向的相邻节点，对其他节点无影响。
      - 缺点：数据的分布和节点的位置有关，因为这些节点不是均匀的分布在哈希环上的，所以数据在进行存储时达不到均匀分布的效果。

  - **哈希槽分区**

    - 为什么出现？

      一致性哈希算法的数据倾斜问题。哈希槽实质就是一个数据，数据【0，2^14-1】形成hash slot空间。

    - 能干什么？

      解决均匀分配问题，**在数据和节点之间又加了一层，把这层称为哈希槽（slot），用于管理数据和节点之间的关系**，现在就相当于节点上放的是槽，槽里放的是数据。

      槽解决的是粒度问滴，相当于把粒度变大了，这样便于数据移动。哈希解决的是映射问题，使用key的哈希值来计算所在的槽，便于数据分配

    - 多少个hash槽？

      一个集群只能有16384个槽，编号0-16383（0，2^14-1）。这些槽会分配给集群中的所有主节点，分配策略没有要求。

      集群会记录节点和槽的对应关系，解决了节点和槽的关系后，接下来就需要对key求哈希值，然后对16384取模，余数是几key就落入对应的槽里面。HASH_SLOT=CRC16(key) mod 16384。以槽为单位移动数据，因为槽的数目是固定的，处理起来比较容易，这样数据移动问题就解决了。

    - 哈希槽的计算

      Redis集群中内置了16384个哈希槽，redis会根据节点数量大致均等的哈希槽映射到不同的节点。当需要在Redis集群中防止一个key-value时，redis先对key使用crc16算法算出一个结果然后用结果对16384求余[ CRC16(key) % 16384 ]，这样每个key都会对应一个编号，在0-16383之间的哈希槽，也就是映射到某个节点上。

- 经典面试题 =》 **为什么redis集群的最大槽数值是16384个？**

  - Redis集群并没有使用一致性哈希而是引入了哈希槽的概念，**Redis集群有16384个哈希槽**，每个key通过CRC16校验后对16384取模来决定放置哪个槽，集群的每个节点负责一部分hash槽，但为什么哈希槽的数量是16384（2^14）个呢？

    > CRC16算法产生的hash值有16bit，该算法可以产生2^16=65536个值。
    >
    > 换句话说值是分布在0~65535之间，有更大的65536不用为什么只用16384就够？
    >
    > 作者在做mod运算的时候，为什么不mod65536，而原则mod16384？HASH_SLOT = CRC16(key) mod 65536 为什么没启用
    >
    > 1. 如果槽位为65536，发送心跳信息的信息头达8k，发送的心跳包过于庞大。
    >
    >    在消息头中最占空间的是myslots[ CLUSTER_SLOTS/8 ]。当槽位为65536时，这块的大小是：65536÷8÷1024=8kb
    >
    >    在消息头中最占空间的是myslots[ CLUSTER_SLOTS/8 ]。当槽位为16384时，这块的大小是：65536÷8÷1024=2kb
    >
    >    因为每秒钟，redis节点需要发送一定数量的ping消息作为心跳包，如果槽位为65536，这个ping消息的消息头太大了，浪费带宽。
    >
    > 2. redis的集群主节点数量基本不可能超过1000个。
    >
    >    集群节点越多，心跳包的消息体内携带的数据越多。如果节点超过1000个，也会导致网络拥堵。因此redis作者不建议redis cluster节点数量超过1000个。那么，对于节点数在1000以内的redis cluster集群，16384槽位够用了。没有必要拓展到65536个。

- **Redis集群不保证强一致性，这意味着在特定的条件下，Redis集群可能会丢掉一些被系统收到的写入请求命令**



## 10.3、集群环境案例步骤

### 10.3.1、三主三从redis集群配置

1. 找三台真实虚拟机，各自新建    =》  mkdir -p /myredis/cluster

2. 新建6个独立的redis实例服务

   1. redisCluster.conf

      ```markdown
          bind 0.0.0.0
          daemonize yes
          proected-mode no
          port 26379
          logfile "/myredis/sentinel26379.log"
          pidfile /var/run/redis-sentinel26379.pid
          dir /myredis/cluster
          dbfilename dump6381.rdb
          appendonly yes
          appendfilename "appendonly6381.aof"
          requirepass zzq123456
          masterauth zzq123456
      
          cluster-enabled yes
          cluster-config-file nodes-6381.conf
          cluster-node-timeout 5000
      ```

3. 通过redis-cli命令为6台机器构建集群关系

   - 构建主从关系命令

     - ```conf
       //	注意自己的真实IP地址
       redis-cli -a zzq123456 --cluster create --cluster-replicas 1 192.168.111.175:6381 192.168.111.175:6382 192.168.111.175:6383 192.168.111.175:6384 192.168.111.175:6384 192.168.111.175:6386
       
       redis-cli -a zzq121700 --cluster create --cluster-replicas 1 192.168.129.132:6379 192.168.129.132:6380 192.168.129.133:6381 192.168.129.133:6382 192.168.129.134:6383 192.168.129.134:6384
       
       --cluster-replicas 1表示为每个master创建一个slave节点
       ```

   - 一切OK的话，三主三从搞定

4. 链接近入6381作为切入点，**查看并校验集群状态**

   - ```conf
     CLUSTER NODES
     ```

   

### 10.3.2、三主三从redis集群读写

1. 对6381新增两个key，看看效果如何

   - set k1 v1 不也能写
   - set k2 v2 可以写

2. 为什么报错

   - 一定注意槽位的范围区间，需要**路由到位**

3. 如何解决

   - 防止路由失效，加参数-c新增两个key

     redis-cli -a zzq12346 -p 6381 -c

4. 查看集群信息

5. 查看某个key该属于对应的槽位置CLUSTER KEYSLOT 键名称



### 10.3.3、出从容错切换迁移案例

1. 容错切换迁移
   1. 主6381和从机切换，先停止主机6381
      1. 6381主机停了，对应的真实从机会不会上位？ ==》  YES
      2. 6381作为1号主机分配的从机以实际情况为准，具体是几号机器就是几号
   2. 再次查看集群信息  ==》 6384主，6381从
   3. 停止主机6384，再次查看集群信息
   4. 随后，6381原来的主机回来了，是否上位？ ==》 YES
2. 集群不保证数据一致性100%OK，一定会有数据丢失情况
   1. Redis集群不保证强一致性，这意味着在特定的条件下，Redis集群可能会丢掉一些被系统收到的写入请求命令
3. 手动故障转移 or 节点从属调整该如何处理
   1. 上面1换后6381、6384主从对调了，原始设计的不一样了，该如何
   2. 重新登录6381机器
   3. 常用命令
      1. CLUSTER FAILOVER



### 10.3.4、主从扩容案例

1. 新建6387/6388两个服务实例配置文件+新建后启动

2. 启动87/88两个新的节点实例，此时他们都是master

3. 将新增的6387节点（空槽号）作为master节点加入原集群

   1. ```conf
      将新增的6387作为master节点加入原有集群
      redis-cli -a 密码 --cluster add-node 实际IP:6387 实际IP:6381
      6387就是将要作为master新增节点
      6381就是原来集群节点里面的领路人（集群中的任意节点），相当于6387拜6381的码头从而找到组织加入集群
      ```

4. 检查集群情况第一次

5. **重新分派槽（reshard）**

   1. ```conf
      1、redis-cli -a 密码 --cluster reshard 实际IP:6381
      2、4096   // 16384/4
      3、6387的node节点号
      ```

6. 检查集群情况第二次

   1. 为什么6387是三个新的区间，以前的还是连续？

      重新分配成本太高，所以前三家各自匀出来一部分，从6381/6382/6383三个旧节点分别匀出1364个坑位给新节点6387

7. 为主节点6387陪分配从节点6388

   1. ```conf
      redis-cli -a 密码 --cluster add-node ip:新slave端口 ip:新master端口 --cluster-master-id 新主机节点ID
      ```

8. 检查集群情况第三次



### 10.3.5、主从缩容案例

1. 目的：6387和6388下线

2. 检查集群情况第一次，先获得从节点6388和节点ID

   1. ```conf
      redis-cli -a 密码 --cluster check ip:6388
      ```

3. 从集群中将4号从节点6388删除

   1. ```conf
      redis-cli -a 密码 --cluster del-node ip:从机端口号 从机6388节点ID
      ```

4. 将6387的槽号清空，重新分配，本例将请出来的槽号都给6381

   1. ```conf
      redis-cli -a 密码 --cluster reshard ip:6381
      4096
      (6381的节点ID)
      (6387的节点ID)
      done
      yes
      ```

5. 检查集群情况第二次

6. 将6387删除

   1. ```conf
      redis-cli -a 密码 --cluster del-node ip:从机端口号 从机6387节点ID
      ```

7. 检查集群情况第三次，6387/6388被彻底去除



## 10.4集群常用操作命令和CRC16算法

1. 不在同一个slot槽位下的多键操作支持不好，通识占位符登场

   1. 不在用一个slot槽位下的键值无法使用mset，mget等多键操作
   2. 可以通过{}来定义同一个组的概念，使key中{}内相同内容的键值对放在一个slot槽位去

2. Redis集群有16384个哈希槽，每个key通过CRC16校验后对16384取模来决定放置哪个槽。

   集群的每一个节点负责一部分hash槽

   1. CRC16源码浅谈

3. 常用命令

   1. 集群是否完整才能对外提供服务：cluster-require-full-coverage
      - 默认是YES，现在集群框架是3主3从的redis cluster由3个master平分16384个slot，每个master的小集群负责1/3的slot，对应一部分数据。
      - cluster-require-full-coverage：默认值是Yes，既需要集群完整性，方可对外服务。通常情况下，如果这三个小集群中，任何一个（一主一从）挂了，这个集群对外可提供的数据只有2/3了，整个集群是不完整的，redis默认在这种情况下，是不会对外提供服务的。
      - 如果你的诉求是。**集群不完整的话也需要对外提供服务**需要将该参数设置为no，这样的话，挂了额那个小集群是不行了，但是其他的小集群仍然可以对外提供服务。
   2. CLUSTER COUNTKEYSINSLOT 槽位数据编号
      1. 该槽位被占用       => 1
      2. 该槽位没被占用   => 0
   3. CLUSTER KEYSLOT 键名称
      1. 该键在哪个槽位上

# 11、SpringBoot集成Redis

## 11.1、总体概述

Jedis - Lettuce - RedisTemplate三者的联系





## 11.2、本地Java连接Redis常见问题

1. bind配置注释掉
2. 保护模式设置为no
3. Linux系统的防火墙设置
4. Redis服务器的IP地址和密码是否正确
5. 访问Redis的服务端口号和auth密码



## 11.3、集成Jedis

### 11.3.1、 Jedis是什么？

Jedis Client是Redis官网推荐的一个面向Java客户端，库文件实现了对各类API进行封装调用 

在Java中，Jedis是一个流行的开源Java库，用于与Redis数据库进行通信。Redis是一个高性能的键值存储系统，广泛用于缓存、会话管理、消息队列等用途。Jedis库提供了与Redis服务器进行交互的API，使Java开发人员能够轻松地在他们的应用程序中使用Redis功能。通过Jedis，你可以执行各种Redis操作，如设置和获取键值对、执行列表、集合和有序集合操作等。



### 11.3.2、使用Jedis步骤

1. 建Module

2. 改POM

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <groupId>com</groupId>
       <artifactId>Redis01_Jedis</artifactId>
       <version>0.0.1-SNAPSHOT</version>
   
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.6.10</version>
       </parent>
       <properties>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <maven.compiler.target>11</maven.compiler.target>
           <maven.compiler.source>11</maven.compiler.source>
           <junit.version>4.12</junit.version>
           <log4j.version>1.2.17</log4j.version>
           <lombok.version>1.16.18</lombok.version>
       </properties>
   
       <dependencies>
           <!--Spring Boot通用依赖模块-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
           <!--Jedis-->
           <dependency>
               <groupId>redis.clients</groupId>
               <artifactId>jedis</artifactId>
               <version>4.3.1</version>
           </dependency>
           <!--通用基础配置-->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>${junit.version}</version>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
           <dependency>
               <groupId>log4j</groupId>
               <artifactId>log4j</artifactId>
               <version>${log4j.version}</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>${lombok.version}</version>
           </dependency>
       </dependencies>
   </project>
   
   ```

3. 写YML

   ```YML
   server:
     port: 8080
   
   spring:
     application:
       name: redis01_jedis
   ```

4. 主启动

   ```java
   package com.redis01_jedis.demo;
   
   import redis.clients.jedis.Jedis;
   
   public class JedisDemo {
   
       public static void main(String[] args) {
           //  1、connection获得，通过指定ip和端口号
           Jedis jedis = new Jedis("192.168.129.132", 6379);
   
           //  2、指定访问服务器的密码
           jedis.auth("zzq121700");
   
           //  3、获得了jedis客户端，可以像jdbc一样，访问redis
           System.out.println(jedis.ping());
       }
   }
   ```

5. 业务类

   1. 入门案例

   2. 5+1

      1. 一个key

         ```java
         //  keys
         Set<String> keys = jedis.keys("*");
         System.out.println(keys);
         ```

      2. 常用五大数据类型

         ```java
                 //String
                 jedis.set("k3", "v3");
                 System.out.println(jedis.get("k3"));
                 System.out.println(jedis.ttl("k3"));
                 jedis.expire("k3",20L);
         
                 //list
                 jedis.lpush("list", "k1", "k2", "k3");
                 List<String> list = jedis.lrange("list", 0, -1);
                 for (String element : list) {
                     System.out.println(element);
                 }
         
                 //hash
                 Map<String,String> map1 = new HashMap<>();
                 Map<String,String> map2 = new HashMap<>();
                 Map<String,String> map3 = new HashMap<>();
                 map1.put("蛋蛋","Disney");
                 jedis.hset("map",map1);
                 System.out.println(jedis.hgetAll("map"));
         
         
                 //set
                 jedis.sadd("set","k1","k2");
                 System.out.println(jedis.smembers("set"));
         
                 //zSet
                 jedis.zadd("zSet1",80,"k1");
                 jedis.zadd("zSet1",60,"k2");
                 jedis.zadd("zSet1",90,"k3");
                 System.out.println(jedis.zcount("zSet1", 70, 90));
                 System.out.println(jedis.zcard("zSet1"));
         ```





## 11.4、集成Lettuce

### 11.4.1、Lettuce是什么？

Lettuce是一个Redis的Java驱动包，Lettuce翻译为生菜

Lettuce是一个流行的开源Java Redis客户端库，用于与Redis服务器进行通信。它提供了异步、同步和反应式的API，允许Java开发人员以多种方式与Redis进行交互。

Lettuce具有高性能和可扩展性，支持Redis的所有主要功能，包括字符串、列表、哈希、集合、有序集合等数据类型的操作。它还提供了连接池、集群支持、SSL连接、命令延迟和哨兵模式等特性，使得在Java应用程序中使用Redis变得更加方便和灵活。





### 11.4.2、Lettuce VS Jedis

Lettuce和Jedis都是用于在Java应用程序中与Redis服务器进行通信的流行客户端库，但它们在实现方式和特性上有一些不同：

1. **实现方式**：
   - Jedis：Jedis是一个基于阻塞I/O的Redis客户端库，它使用传统的同步方式进行通信。这意味着当执行Redis操作时，当前线程会被阻塞，直到操作完成或者超时。
   - Lettuce：Lettuce是一个基于Netty的非阻塞I/O的Redis客户端库，它使用异步方式进行通信。这意味着它可以在单个连接上同时处理多个请求，并且不会阻塞当前线程，从而提高了并发性能和吞吐量。
2. **线程安全性**：
   - Jedis：Jedis实例不是线程安全的，如果在多个线程中共享同一个Jedis实例，需要使用同步机制来保证线程安全性。
   - Lettuce：Lettuce实例是线程安全的，可以在多个线程中共享同一个Lettuce实例而无需担心线程安全性问题。
3. **连接池**：
   - Jedis：Jedis提供了自带的连接池来管理连接，但默认情况下连接池是单例的，即所有的Jedis实例共享同一个连接池。
   - Lettuce：Lettuce也提供了连接池的支持，但与Jedis不同的是，Lettuce的连接池是可配置的，可以根据需要创建多个连接池，每个连接池可以设置不同的参数。
4. **功能特性**：
   - Jedis和Lettuce都支持Redis的所有主要功能，包括字符串、列表、哈希、集合、有序集合等数据类型的操作。但Lettuce提供了更多的高级特性，如SSL连接、哨兵模式、集群模式、命令延迟等。



### 11.4.3、案例

1. 改POM

   ```xml
           <!--Lettuce-->
           <dependency>
               <groupId>io.lettuce</groupId>
               <artifactId>lettuce-core</artifactId>
               <version>6.2.1.RELEASE</version>
           </dependency>
   ```

2. 业务类

   ```java
   package com.redis01_jedis.demo;
   
   import io.lettuce.core.RedisClient;
   import io.lettuce.core.RedisURI;
   import io.lettuce.core.api.StatefulRedisConnection;
   import io.lettuce.core.api.sync.RedisCommands;
   
   import java.util.List;
   
   public class LettuceDemo {
   
       public static void main(String[] args) {
   
           //1、使用构建起链式编程来builder RedisURI
           RedisURI redis = RedisURI.builder()
                   .redis("192.168.129.132")
                   .withPort(6379)
                   .withAuthentication("default", "zzq121700")
                   .build();
   
   
           //2、创建连接客服端
           RedisClient client = RedisClient.create(redis);
           StatefulRedisConnection connect = client.connect();
   
   
           //3、创建操作的command，通过connect
           RedisCommands commands = connect.sync();
           //  keys
           List keys = commands.keys("*");
           System.out.println(keys);
   
           //  string
           commands.set("k1","v1");
           System.out.println("==========》"+commands.get("k1"));
   
   
           //4、各种关闭释放资源
           try {
               connect.close();
               client.shutdown();
           } catch (Exception e) {
               System.out.println(e);
           }
   
       }
   }
   ```









## 11.5、集成RedisTemplate -- 推荐使用

### 11.5.1、连接单机

**导入依赖：**

```xml
<!--RedisTemplate-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
<!--swagger2-->
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
```

**application.yml**

```yml
server:
  port: 8080

spring:
  application:
    name: redis01_jedis
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
  redis:
    database: 0
    host: 192.168.129.132
    port: 6379
    password: zzq121700
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

  swagger2:
    enable: true

#  logging
logging:
  level:
    root: info
    com.redis01_jedis: info
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [thread] %-5level %logger- %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [thread] %-5level %logger- %msg%n"


  file:
    name: K:/GitHub/notes/Redis/Redis_CODE/myLogs2024/redis7.log

##swagger
#spring:
#  swagger2:
#    enabled:true
```

**Swagger2Config**

```java
package com.redis01_jedis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${spring.swagger2.enable}")
    private Boolean enable;

    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.redis01_jedis"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SpringBoot利用Swagger2构建api接口文档" + "\t"+
                                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())
                        )
                .description("SpringBoot+Redis整合")
                .version("1.0")
                .termsOfServiceUrl("disney")
                .build();
    }


}
```

**Service**

```java
package com.redis01_jedis.service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class OrderServer {

    public static final String ORDER_KEY = "ord";

    @Resource
    private RedisTemplate redisTemplate;

    public void addOrder() {
        int keyId = ThreadLocalRandom.current().nextInt(1000) + 1;
        String serialNo = UUID.randomUUID().toString();

        String key = ORDER_KEY + keyId;
        String value = "京东订单" + serialNo;

        redisTemplate.opsForValue().set(key, value);

        System.out.println("---key:{" + key + "}");
        System.out.println("---value:{" + value + "}");

    }

    public String getOrderById(Integer keyId) {
        return (String) redisTemplate.opsForValue().get(ORDER_KEY + keyId);
    }

}
```

**Controller**

```java
package com.redis01_jedis.controller;

import com.redis01_jedis.service.OrderServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@Api(tags = "订单接口")
public class OrderController {

    @Resource
    private OrderServer orderServer;

    @ApiOperation("新增订单")
    @RequestMapping(value = "/order/add",method = RequestMethod.POST)
    public void addOrder(){
        orderServer.addOrder();
    }

    @ApiOperation("按照keyId查询订单")
    @RequestMapping(value = "/order/{keyId}",method = RequestMethod.GET)
    public void getOrderById(@PathVariable Integer keyId){
        orderServer.getOrderById(keyId);
    }

}
```



**测试**

http://localhost:8080/swagger-ui.html

传回去的值被序列化了

key：\xac\xed\x00\x05t\x00\x06ord441

value：xac\xed\x00\x05t\x000\xe4\xba\xac\xe4\xb8\x9c\xe8\xae\xa2\xe5\x8d\x95ffb35f37-140d-4d7a-af13-1df8573ca5eb"

**解决方案**

第一种：用StringRedisTemplate替换掉RedisTemplate

默认使用：

```java
public void afterPropertiesSet() {
    super.afterPropertiesSet();
    boolean defaultUsed = false;
    if (this.defaultSerializer == null) {
        this.defaultSerializer = new JdkSerializationRedisSerializer(this.classLoader != null ? this.classLoader : this.getClass().getClassLoader());
    }
```

redis上会不支持中文的显示，所以我们进入redis的时候需要：

redis-cli -a zzq123456 -p 6399 --raw

```java
// @Resource private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
```

第二种：定义自己的序列化方式

```java
package com.redis01_jedis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        //设置key序列化方式String
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认的
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
```





**RedisConfig**

> 键（key）和值（value）都是通过Spring提供的Serializer序列化到数据库的。
>
> RedisTemplate默认使用的是JdkSerializationRedisSerializer，
>
> StringRedisTemplate默认使用的是StringRedisSerializer。
>
> Key被序列化成这样，线上通过Key去查询对应的Value非常不方便。









### 11.5.3、连接集群




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
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
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

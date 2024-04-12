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

package com.hand.rebbitmq.four;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/3 14:29
 * 发布确认模式
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {
    /**
     * 批量发消息个数
     */
    public static final int MESSAGE_COUNT=1000;
    public static void main(String[] args) throws Exception {

        //1.单个确认   发布1000个单独确认消息，耗时:15794ms
//        publishMessgaeOne();
        //2.批量确认  发布1000个批量确认消息，耗时:556ms
//        publishMessageBatch();
        //3.异步批量确认   发布1000个异步确认消息，耗时:38ms
        publishMessageAsync();
    }

    public static void publishMessgaeOne() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message = "消息:"+i;
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功。。。。");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时:"+(end-begin)+"ms");
    }
    //批量发布确认
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;

        //批量发送消息，批量发布确认
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message = "消息:"+i;
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));

            //判断达到100条消息的时候 批量确认一次
            if(i%batchSize == 0){
                //发布确认
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认消息，耗时:"+(end-begin)+"ms");
    }

    //异步发布确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表  适用于高并发的情况下
         *1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发(多线程)
         */
        ConcurrentSkipListMap<Long,String>  outStandingConfirms = new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();

        //消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple) ->{
            if(multiple){
                //2.删除掉已经确认的消息 剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else{
                outStandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认的消息:"+deliveryTag);
        };
        //消息确认失败回调函数
        /**
         * deliveryTag 消息的标记
         * multiple 是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag,multiple) ->{
            String message = outStandingConfirms.get(deliveryTag);
            //3.打印未确认的消息有哪些
            System.out.println("未确认的消息是:"+message+"::::::未确认的消息tag:"+deliveryTag);
        };
        //准备消息的监听器 监听哪些消息成功了 哪些消息失败了
        /**
         * 异步通知
         * 1.监听哪些消息成功
         * 2.监听哪些消息失败
         */
        channel.addConfirmListener(ackCallback,nackCallback);

        for(int i=0;i<MESSAGE_COUNT;i++) {
            String message = "消息:" + i;
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));

            //1.记录下所有要发送的消息  消息的总和
            outStandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步确认消息，耗时:"+(end-begin)+"ms");
    }
}

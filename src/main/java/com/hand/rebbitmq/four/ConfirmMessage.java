package com.hand.rebbitmq.four;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

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

        //1.单个确认
//        publishMessgaeOne();
        //2.批量确认
        publishMessageBatch();
        //3.异步批量确认
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
}

package com.hand.rebbitmq.three;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/3 9:52
 * 消息在手动应答时是不丢失的，放回队列重新消费
 */
public class Task2 {
    /**
     * //队列名称
     */
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //声明队列
        //队列持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String message = sc.next();
            //消息持久化  MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息:"+message);
        }
    }
}

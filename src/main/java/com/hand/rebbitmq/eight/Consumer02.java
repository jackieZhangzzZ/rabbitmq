package com.hand.rebbitmq.eight;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 14:44
 *
 * 死信队列
 *
 * 消费者2
 */
public class Consumer02 {
    /**
     * 死信队列名称
     */
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        System.out.println("等待接收消息。。。。。。");
        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("Consumer02收到的消息:"+new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("Consumer02消息消费被中断。。。。");
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
    }
}

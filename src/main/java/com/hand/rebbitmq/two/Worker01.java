package com.hand.rebbitmq.two;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/2 14:42
 * 这是一个工作线程(相当于消费者)
 */
public class Worker01 {
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("收到的消息:"+new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println(consumerTag+"消息消费被中断。。。。");
        };
        System.out.println("C2等待接收消息。。。");
        //消息接收
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}

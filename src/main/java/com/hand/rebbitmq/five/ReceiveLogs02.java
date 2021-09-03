package com.hand.rebbitmq.five;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/3 16:42
 */
public class ReceiveLogs02 {
    //  交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        //声明一个交换机  fanout扇出  -->广播
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列  队列的名称是随机的 当消费者断开与队列连接的时候 队列自动删除
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("ReceiveLogs02等待接收消息");

        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("ReceiveLogs02收到的消息:"+new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("ReceiveLogs02消息消费被中断。。。。");
        };

        channel.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}

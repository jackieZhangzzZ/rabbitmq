package com.hand.rebbitmq.seven;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 10:45
 * 声明主题交换机 及相关队列
 *
 * 消费者C2
 */
public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接收消息");

        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("ReceiveLogsTopic02收到的消息:"+new String(message.getBody()));
            System.out.println("接收队列:"+queueName+" 绑定键:"+message.getEnvelope().getRoutingKey());
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("ReceiveLogsTopic02消息消费被中断。。。。");
        };
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);

    }
}

package com.hand.rebbitmq.six;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 9:38
 */
public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
        //绑定交换机
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("ReceiveLogsDirect02收到的消息:"+new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("ReceiveLogsDirect02消息消费被中断。。。。");
        };
        channel.basicConsume("disk",true,deliverCallback,cancelCallback);
    }
}

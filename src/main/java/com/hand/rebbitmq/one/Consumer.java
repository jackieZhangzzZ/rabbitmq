package com.hand.rebbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/8/31 20:07
 * 消费者  -->接受消息
 */
public class Consumer {
    /**
     *    队列名称
     */
    public static final String QUEUE_NAME="hello";
    //接收消息
    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接Rabbit的队列
        factory.setHost("49.234.211.99");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("admin");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("收到的消息:"+new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息消费被中断。。。。");
        };
        /**
         * 消费者消费消息
         * 1.消费的哪个队列
         * 2.消费成功之后是否要自动应答 true代表自动应答 false代表手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者
         */
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}

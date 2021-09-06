package com.hand.rebbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/8/31 15:53
 * 生产者  -->发送消息
 */

public class Producer {
    /**
     *    队列名称
     */
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
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
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里的消息是否持久化
         * 3.队列是否只供一个消费者进行消费 是否进行消息共享，true可以多个消费者消费
         * 4.是否自动删除   最后一个消费者断开连接以后队列是否自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String message = "hello world";
        /**
         * 发送一个消费
         * 1.发送到哪个交换机
         * 2.路由的key值
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕！");
    }
}

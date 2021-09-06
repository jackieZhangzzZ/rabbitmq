package com.hand.rebbitmq.two;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/2 15:03
 * 生产者 工作队列
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息完成："+message);
        }
    }
}

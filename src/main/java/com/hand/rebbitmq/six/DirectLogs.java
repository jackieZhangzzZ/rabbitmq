package com.hand.rebbitmq.six;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 9:48
 */
public class DirectLogs {
    //  交换机的名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            //不同的rutingKey实现给不同的队列发送消息
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息:"+message);
        }
    }
}

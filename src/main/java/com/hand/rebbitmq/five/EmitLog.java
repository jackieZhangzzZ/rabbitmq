package com.hand.rebbitmq.five;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/3 16:52
 * 发送消息给交换机
 */
public class EmitLog {
    //  交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        //声明一个交换机  fanout扇出  -->广播
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息:"+message);
        }
    }
}

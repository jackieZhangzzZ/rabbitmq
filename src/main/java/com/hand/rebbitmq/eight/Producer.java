package com.hand.rebbitmq.eight;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 15:07
 *
 * 死信队列生产者
 *
 * 产生死信队列的条件：
 * 1.消息被拒绝
 * 2.消息TTL过期
 * 3.队列达到最大长度
 */
public class Producer {
    /**
     * 普通交换机名称
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        //死信消息 设置TTL时间  time to live
        /*AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().expiration("10000").build();*/

        for (int i = 0; i < 11; i++) {
            String message = "message-info"+i;
//            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("已发送的消息"+message);
        }
    }
}

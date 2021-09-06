package com.hand.rebbitmq.seven;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 9:48
 */
public class EmitLogTopic {
    //  交换机的名称
    public static final String EXCHANGE_NAME = "topic_logs";

    /**
     * Q1-->绑定的是
     * 中间带 orange 带 3 个单词的字符串(*.orange.*)
     * Q2-->绑定的是
     * 最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
     * 第一个单词是 lazy 的多个单词(lazy.#)
     *
     */
    /**
     * quick.orange.rabbit 被队列 Q1Q2 接收到
     * lazy.orange.elephant 被队列 Q1Q2 接收到
     * quick.orange.fox 被队列 Q1 接收到
     * lazy.brown.fox 被队列 Q2 接收到
     * lazy.pink.rabbit 虽然满足两个绑定但只被队列 Q2 接收一次
     * quick.brown.fox 不匹配任何绑定不会被任何队列接收到会被丢弃
     * quick.orange.male.rabbit 是四个单词不匹配任何绑定会被丢弃
     * lazy.orange.male.rabbit 是四个单词但匹配 Q2
     */
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        Map<String,String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        for (Map.Entry<String,String> bindingKey:bindingKeyMap.entrySet()) {
            String routingKey = bindingKey.getKey();
            String message = bindingKey.getValue();
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:"+message);
        }
    }
}

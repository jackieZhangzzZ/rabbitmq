package com.hand.rebbitmq.three;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.hand.rebbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/3 9:58
 * 消息在手动应答时是不丢失的，放回队列重新消费
 */
public class Work04 {
    /**
     * 队列名称
     */
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        System.out.println("C2等待接收消息处理时间较长....");
        //接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            //休眠30 秒
            SleepUtils.sleep(30);
            System.out.println("C2收到的消息:"+new String(message.getBody(),"UTF-8"));
            /**
             * 手动应答
             * 1.消息的标记
             * 2.是否批量应答 false:不批量应答信道中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息消费被中断。。。。");
        };
        //设置不公平分发  默认是轮询 0
//        int prefetchCount = 1;
        //预取值  即接收5条消息
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}

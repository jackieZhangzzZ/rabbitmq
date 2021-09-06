package com.hand.rebbitmq.eight;

import com.hand.rebbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/6 14:44
 *
 * 死信队列
 *
 * 消费者1
 * 产生死信队列的条件：
 * 1.消息被拒绝
 * 2.消息TTL过期
 * 3.队列达到最大长度
 */
public class Consumer01 {
    /**
     * 普通交换机名称
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    /**
     * 死信交换机名称
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";
    /**
     * 普通队列名称
     */
    public static final String NORMAL_QUEUE = "normal_queue";
    /**
     * 死信队列名称
     */
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        //声明死信和普通交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        //过期时间 10s   也可以在生产消息的时候设置过期时间
//        arguments.put("x-message-ttl",10000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置正常队列的长度限制  最大长度为6   队列达到最大长度成为死信(队出来的部分)
//        arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定普通的交换机与普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信的交换机与死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收消息。。。。。。");
        //接收消息的回调
        /*DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("Consumer01收到的消息:"+new String(message.getBody()));
        };*/
        //消息被拒绝
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody());
            if(msg.equals("message-info5")){
                System.out.println("Consumer01收到的消息:"+msg+",但是该消息被拒绝消费");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("Consumer01收到的消息:"+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        //取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("Consumer01消息消费被中断。。。。");
        };
//        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,cancelCallback);
        //拒绝接收消息  开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
    }
}

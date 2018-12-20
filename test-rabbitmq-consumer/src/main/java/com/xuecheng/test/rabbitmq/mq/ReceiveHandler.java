package com.xuecheng.test.rabbitmq.mq;

import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 作者: lin
 * 描述:
 * 日期: 2018/12/18 14:16
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send(String msg, Message message){

        System.out.println("获取的消息是"+msg);
        //System.out.println("获取的通道是"+channel);
        System.out.println("获取的消息类型"+message);

    }
}

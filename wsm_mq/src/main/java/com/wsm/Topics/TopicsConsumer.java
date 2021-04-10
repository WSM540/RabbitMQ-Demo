package com.wsm.Topics;


import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicsConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("tpc.exchange", BuiltinExchangeType.TOPIC);       //指定交换机类型TOPIC

        //使用:自动生成的队列名;
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "tpc.exchange", "wsm.#");                 //通配符Routingkey wsm.# 所有的key!

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body, "UTF-8");
                System.out.println(str);
            }
        };
        //使用默认 指定要监测的队列...
        channel.basicConsume(queueName,true,defaultConsumer);
    }
}

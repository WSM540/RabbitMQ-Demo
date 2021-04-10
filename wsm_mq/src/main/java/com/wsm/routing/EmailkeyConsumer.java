package com.wsm.routing;

import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmailkeyConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("rout.exchange", BuiltinExchangeType.DIRECT);

        //使用:自动生成的队列名;
        String queueName = channel.queueDeclare().getQueue();

        //读取数据时候,只要根据绑定 routing key 交换机就会往对应队列上发送数据;
        channel.queueBind(queueName, "rout.exchange", "email.key");
//        //一个队列可以绑定多个Routingkey, 从而实现多种类型值...
//        channel.queueBind(queueName, "rout.exchange", "sms.key");   //这里可以松解注释多次进行测试...

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

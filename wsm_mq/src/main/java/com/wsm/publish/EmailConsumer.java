package com.wsm.publish;

import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmailConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("publish.exchange", BuiltinExchangeType.FANOUT);

        String queuename = channel.queueDeclare().getQueue();

        channel.queueBind(queuename, "publish.exchange", "");
        //消息回调
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body, "UTF-8");
                System.out.println(str);
            }
        };
        //设置...
        channel.basicConsume(queuename,true,defaultConsumer);
    }
}

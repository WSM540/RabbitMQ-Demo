package com.wsm.confirm;

import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;

public class ConfimConsumer {
    public static void main(String[] args) throws Exception {
        //连接
        Connection conn = ConnectionUtil.getConnection();
        Channel channel = conn.createChannel();
        //声明交换机
        channel.exchangeDeclare("confim.exchage", BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare("confim.queue", true, false, false, null);
        //绑定数据
        channel.queueBind("confim.queue",  "confim.exchage", "confim.#");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
            }
        };
        //监听消息, 传入回调函数...
        //开启ack 自动认证...
        channel.basicConsume("confim.queue", true, consumer);
    }
}

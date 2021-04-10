package com.wsm.returns;


import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
public class ReturnConsumer {
    public static void main(String[] args) throws Exception {
        //连接
        Connection conn = ConnectionUtil.getConnection();
        Channel channel = conn.createChannel();

        //声明: 交换机 队列 绑定！
        channel.exchangeDeclare("return.exchage", BuiltinExchangeType.TOPIC);
        channel.queueDeclare( "return.queue", true, false, false, null);
        channel.queueBind( "return.queue", "return.exchage", "return.#");

        //接收数据 回调！
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
            }
        };
        channel.basicConsume( "return.queue", true, consumer);

    }
}

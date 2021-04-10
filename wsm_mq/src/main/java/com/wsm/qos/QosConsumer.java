package com.wsm.qos;

import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;

import java.io.IOException;

public class QosConsumer {
    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtil.getConnection();
        final Channel channel = conn.createChannel();
        channel.exchangeDeclare("qos.exchage", BuiltinExchangeType.TOPIC);
        channel.queueDeclare("qos.queue", true, false, false, null);
        channel.queueBind("qos.queue", "qos.exchage", "qos.#");

        /**限流 Void BasicQos(uint prefetchSize, ushort prefetchCount, bool global);
         *prefetchSize：0	不限制消息大小
         *prefetchSize 会告诉RabbitMQ不要同时给一个消费者推送多于N个消息,即一旦有N个消息还没有ack,则该Consumer将block（阻塞）掉,直到有消息ack
         *Global：true\false是否将上面设置应用于Channel；简单来说，就是上面限制是Channel级别的还是Consumer级别
         */
        channel.basicQos(0, 3, false);  //每次通过三条数据！

        //发送消息,没有返回ack 的回调方法！
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("===========服务端发送的信息===============");
                System.out.println(new String(body, "UTF-8"));
                //代码手动回复ack，告诉服务端,以处理完当前的消息,你可以继续发下一条消息了!
                //测试程序演示,注释掉改代码,不然它每一次都通过代码手动回复了...就看不到效果了...
//                channel.basicAck(envelope.getDeliveryTag(),false);        // false 处理批量数据
            }
        };

        //发送数据,因为要查看限流的效果,默认的自动回复设置为 false 不自动回复！
//        channel.basicConsume("qos.queue", true, consumer);
        channel.basicConsume("qos.queue", false, consumer);
    }
}

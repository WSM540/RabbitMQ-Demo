package com.wsm.hello;

import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;

//消息接收者:
public class HelloConsumer {
    public void revice() throws Exception {
        //创建连接:
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello.queue", true, false, false, null);
        //收到消息后用来处理消息的回调对象
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            /**
             * 当接收到消息后此方法将被调用
             * @param consumerTag  消费者标签，用来标识消费者的，在监听队列时设置channel.basicConsume
             * @param envelope 信封，通过envelope: 可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * @param properties 消息属性
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //将返回的字节数组转换成 String 打印输出！
                String str = new String(body, "UTF-8");
                System.out.println(str);
            }
        };
        // 监听队列，第二个参数：是否自动进行消息确认。
        //参数：String queue, boolean autoAck, Consumer callback
        /**
         * 参数明细：
         * 1、queue 队列名称
         * 2、autoAck 自动回复,当消费者接收到消息后要告诉mq消息已接收,如果将此参数设置为tru表示会自动回复mq,如果设置为false要通过编程实现回复验证,这就是Unacked 为返回ack的数据
         * 3、callback，消费方法，当消费者接收到消息要执行的方法
         */
        channel.basicConsume("hello.queue", true, defaultConsumer);
    }

    //main运行输出！
    public static void main(String[] args)throws Exception {
        HelloConsumer sc =new HelloConsumer();
        sc.revice();
    }
}

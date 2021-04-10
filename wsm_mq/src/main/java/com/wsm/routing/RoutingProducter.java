package com.wsm.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class RoutingProducter {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //指定交换机 及 交换机的类型
        channel.exchangeDeclare("rout.exchange", BuiltinExchangeType.DIRECT);
//        定义队列.....发送者可以不需要！
//        channel.queueDeclare("rout.queue", true, false, false, null);

        //要发送的信息！
        String mess = "一条消息:" + new SimpleDateFormat("yyyy-MM-dd hh:mm-ss").format(new Date());

        //开始发送... 发送的交换机  发送的key队列  发送的特殊类型数据   发送的方式字节数组！
        //发送数据时候指定 routingkey  取数据时候,根据指定的routingkey 而获得对应的routingkey 匹配的值！
        channel.basicPublish("rout.exchange", "email.key", null, (mess+"email").getBytes()); //emial.key
        channel.basicPublish("rout.exchange", "sms.key", null, (mess+"sms").getBytes());     //sma.key
        //关闭资源！
        connection.close();
    }
}

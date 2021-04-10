package com.wsm.Topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class TopicsProducter {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //指定交换机 及 交换机的类型
        channel.exchangeDeclare("tpc.exchange", BuiltinExchangeType.TOPIC);
        //要发送的信息！
        String mess = "一条消息:" + new SimpleDateFormat("yyyy-MM-dd hh:mm-ss").format(new Date());

        channel.basicPublish("tpc.exchange", "wsm.email.key", null, (mess+"email").getBytes()); //emial.key
        channel.basicPublish("tpc.exchange", "wsm.sms.key", null, (mess+"sms").getBytes());     //sma.key
        //关闭资源！
        connection.close();
    }
}

package com.wsm.qos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsm.util.ConnectionUtil;

public class QosProducter {
    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtil.getConnection();
        Channel channel = conn.createChannel();
        String exchangeName = "qos.exchage";
        String routingKey = "qos.key";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, true, null, ("qos一条消息" + i).getBytes());
        }
    }
}

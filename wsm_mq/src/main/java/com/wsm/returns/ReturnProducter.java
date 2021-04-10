package com.wsm.returns;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;

public class ReturnProducter {
    public static void main(String[] args)throws Exception {
        //连接！
        Connection conn = ConnectionUtil.getConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "return.exchage";     //设置交换机
        String routingKey = "return.key";          //不存在的Routingkey： return1.key

        //设置Return Listener监听.... 当然发送消息,交换机/队列错误..没有发送成功就会尽然这个方法！
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println("replyCode:"+replyCode);
                System.out.println("replyText:"+replyText);
                System.out.println("交换机exchange:"+exchange);
                System.out.println("交换机exchange数据:"+new String(bytes,"UTF-8"));
            }
        });
        //发送一条数据... 第三个参数设置 true 丢失数据进行监听处理！
        channel.basicPublish(exchangeName, routingKey,true, null, "return一条消息".getBytes());
    }
}

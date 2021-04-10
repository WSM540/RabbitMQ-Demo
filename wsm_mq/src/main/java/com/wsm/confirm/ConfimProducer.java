package com.wsm.confirm;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.wsm.util.ConnectionUtil;

import java.io.IOException;

public class ConfimProducer {
    public static void main(String[] args) throws Exception {
        //连接
        Connection conn = ConnectionUtil.getConnection();
        Channel channel = conn.createChannel();
        //声明交换机 类型无所谓...
        channel.exchangeDeclare("confim.exchage", BuiltinExchangeType.TOPIC);
        //开启确认机制;
        channel.confirmSelect();
        //发送消息
        channel.basicPublish("confim.exchage", "confim.key", null, "一条消息".getBytes());
        //开启 confrim 消息认证机制...
        channel.addConfirmListener(new ConfirmListener() {
            /**发送成功！执行方法..
             * long: 返回消息的,序列号
             * boolean: 是否允许消息的批量发送！
             */
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println(l);
                System.out.println(b);
                System.out.println("ack");  //已经发送ack确认
            }
            //发送失败！执行... 一般发送这种事情的处理方案...
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println(l);
                System.out.println(b);
                System.out.println("nack"); //nack没有确认！
            }
        });
    }
}

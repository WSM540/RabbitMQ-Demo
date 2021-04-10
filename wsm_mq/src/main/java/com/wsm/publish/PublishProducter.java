package com.wsm.publish;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsm.util.ConnectionUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublishProducter {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        /**创建交换机
         * 交换机名
         * 交换机类型 FANOUT
         * */
        channel.exchangeDeclare("publish.exchange", BuiltinExchangeType.FANOUT);
        /**设置队列/
         channel.queueDeclare("email.queue", true, false, false, null);
         /**交换机绑定队列！
         *指定的队列,与指定的交换机关联起来
         *指定交换机
         *第三个参数时 routingKey, 由于是fanout交换机, 这里忽略 routingKey; 但它是必须参数所以必须要加...而且无论加不加都不会产生影响！
         */
//        channel.queueDeclare("email.queue", true, false, false, null);
//        channel.queueDeclare("sms.queue", true, false, false, null);

        //对于 fanout 类型的交换机,routingKey会被忽略,但不允许null值,允许 ""
//        channel.queueBind("email.queue", "publish.exchange", "");
//        channel.queueBind("sms.queue", "publish.exchange", "");

        //发送的消息
        String mess = "email和sms共同的一条消息:" + new SimpleDateFormat("yyyy-MM-dd hh:mm-ss").format(new Date());

        //发送消息: 到交换机上,交换机根据 FANOUT类型给,每一个队列发送消息...
        channel.basicPublish("publish.exchange", "", null, mess.getBytes());
        connection.close();
    }
}

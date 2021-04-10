package com.wsm.head;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wsm.util.ConnectionUtil;
import java.util.HashMap;
import java.util.Map;

//发送者
public class HederProducter {
    public static void main(String[] args) throws Exception{

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("head.exchange", BuiltinExchangeType.HEADERS);

        //发送的内容
        String str ="header的内容lalala~~";
        //发送的head map, 需要与队列的map 规范匹配才可以发送成功！ 不然发送失败!
        Map<String ,Object> param = new HashMap<String, Object>();
        param.put("id","2");

        //设置Map 匹配参数！
        AMQP.BasicProperties.Builder builder=new AMQP.BasicProperties.Builder();
        builder.headers(param);
        //发送参数 两次！
        channel.basicPublish("head.exchange","",builder.build(),str.getBytes());
        //关闭资源！
        connection.close();
    }
}

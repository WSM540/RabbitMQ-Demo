package com.wsm.head;

import com.rabbitmq.client.*;
import com.wsm.util.ConnectionUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//消费者
public class HeaderConsumer {
    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare("head.exchange", BuiltinExchangeType.HEADERS);    //交换机类型 heades

        //设置队列上的 map 参数,用于匹配请求时候的参数！
        //特殊参数 x-match 值 all 或 any
        //all  在发布消息时携带的map 必须和绑定在队列上的所有map 完全匹配
        //any  只要在发布消息时携带的有一对键值map 满足队列定义的多个参数map的其中一个就能匹配上
        //注意: 这里是键值对的完全匹配，只匹配到键了，值却不一样是不行的；
        Map<String ,Object> param = new HashMap<String, Object>();
        param.put("x-match","any");
        param.put("id","1");
        param.put("name","wsm");


        //声明队列 传入
        channel.queueDeclare("headqueue", false, false, false, null);
        //绑定
        // 队列绑定时需要指定参数,注意虽然不需要路由键但仍旧不能写成null，需要写成空字符串""
        channel.queueBind("headqueue", "head.exchange", "",param);    //map参数,规范！

        //----------------------------------------以上是声明 交换机/队列 绑定规范...
        //以下是,消费者对消息的监听,获取!----------------------------------------...

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                Map<String, Object> headers = properties.getHeaders();
                String str = new String(body, "UTF-8");
                System.out.println("内容:"+str);
                System.out.println("Map传入参数数据！"+headers);
            }
        };

        channel.basicConsume("headqueue", true, defaultConsumer);
    }
}

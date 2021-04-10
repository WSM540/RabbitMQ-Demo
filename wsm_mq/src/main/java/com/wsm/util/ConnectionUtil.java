package com.wsm.util;

//注意MQ 的连接对象类型...
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

//RabbitMQ 连接配置类:
public class ConnectionUtil {
    //静态方法,需要连接对象直接调用即可！
    public static Connection getConnection() throws IOException, TimeoutException {
        //获取一个链接工程
        ConnectionFactory factory = new ConnectionFactory();
        //设置属性
        factory.setHost("127.0.0.1");   //ip
        factory.setPort(5672);          //端口
        factory.setVirtualHost("/");    //虚拟主机地址 虚拟机相当于一个独立的mq服务器
        factory.setUsername("guest");   //用户密码
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        return connection;
    }
}

package com.wsm.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.wsm.util.ConnectionUtil;
//发送者
public class HellowProducter {
    //发送消息
    public void send() throws Exception {
        //创建连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道！Exchange的通道,每个连接可以创建多个通道,每个通道代表一个会话任务
        Channel channel = connection.createChannel();
        /**设置消息队列的属性！
         *  queue :队列名称
         *  durable :是否持久化 如果持久化,mq重启后队列数据还在! (队列是在虚拟路径上的...)
         *  exclusive :队列是否独占此连接,队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
         *  autoDelete :队列不再使用时是否自动删除此队列,队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
         *  arguments :队列参数 null,可以设置一个队列的扩展参数,需要时候使用！比如：可设置存活时间
         * */
        channel.queueDeclare("hello.queue", true, false, false, null);

        //要发送的消息
        String mess = "一条消息:" + new SimpleDateFormat("yyyy-MM-dd hh:mm-ss").format(new Date());

        /**发送消息,参数:
         * exchange :指定的交换机,不指定就会有默认的....
         * routingKey :路由key,交换机根据路由key来将消息转发到指定的队列,如果使用默认交换机routingKey设置为队列的名称
         * props :消息包含的属性: 后面介绍,可以是一个一个对象...
         * body :发送的消息,AMQP以字节方式传输...
         * */
        channel.basicPublish("", "hello.queue", null, mess.getBytes());

        //关闭通道和连接(资源关闭最好用try-catch-finally语句处理
        channel.close();
        connection.close();
    }
    //main 启动运行...
    public static void main(String[] args) throws Exception {
        HellowProducter sp = new HellowProducter();
        sp.send();
    }
}

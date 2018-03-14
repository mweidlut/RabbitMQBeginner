package com.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 如果你想创建一个只有自己可见的队列，即不允许其它用户访问，RabbitMQ允许你将一个Queue声明成为排他性的（Exclusive Queue）
 * <p>
 * 该队列的特点是：
 * 只对首次声明它的连接（Connection）可见
 * 会在其连接断开的时候自动删除。
 * <p>
 * User: weimeng
 * Date: 2018/3/14 10:50
 */
public class ExclusiveQueue {

    private final static String QUEUE_NAME = "UserLogin2";
    private final static String EXCHANGE_NAME = "user.login";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        try {

            //1.创建一个排他的queue
            Connection connection = factory.newConnection();
            Channel channel1 =connection.createChannel();
            AMQP.Queue.DeclareOk declareOk = channel1.queueDeclare(QUEUE_NAME, true, true, false, null);

            channel1.basicPublish("", QUEUE_NAME, null, "Hello".getBytes());

            //close the channel, check if the queue is deleted
            System.out.println("Try to close channel");
            channel1.close();
            System.out.println("Channel closed");



            //2.共用同一个connection的queue
            System.out.println("Create a new channel");
            Channel channel2 =connection.createChannel();
            AMQP.Queue.DeclareOk declareOk2 = channel2.queueDeclarePassive(QUEUE_NAME);

            //we can access the exclusive queue from another channel
            System.out.println(declareOk2.getQueue()); //will output "UserLogin2"
            channel2.basicPublish("", QUEUE_NAME, null, "Hello2".getBytes());
            System.out.println("Message published through the new channel");


            //关闭connection
            /*System.out.println("Try to close Connection");
            connection.close();
            System.out.println("Connection closed");*/


            //3. 接收消息
            //Connection connection2 = factory.newConnection();
            //Channel channel3 = connection2.createChannel(); //error: RESOURCE_LOCKED - cannot obtain exclusive access to locked queue 'UserLogin2' in vhost '/'
            Channel channel3 = connection.createChannel();
            channel3.queueDeclarePassive(QUEUE_NAME);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel3) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };

            channel3.basicConsume(QUEUE_NAME, true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class ReceiveErrorLogsDirect1 {

    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String QUEUE_NAME = "direct_logs_queue";


    public static void main(String[] argv) throws Exception {
        Channel channel = ChannelHelper.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, PublishErrorLogDirect.EXCHANGE_TYPE);
        //String queueName = channel.queueDeclare().getQueue();
        String queueName = channel.queueDeclare(QUEUE_NAME, true, false, false, null).getQueue();

        System.out.println("Error-queue-name:" + queueName);

        /*if (argv.length < 1) {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }*/

        channel.queueBind(queueName, EXCHANGE_NAME, "error");
        //channel.basicQos(1);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

                //channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
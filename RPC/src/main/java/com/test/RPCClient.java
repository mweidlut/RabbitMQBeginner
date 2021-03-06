package com.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * User: weimeng
 * Date: 2018/3/20 11:36
 */
public class RPCClient {

    private static String requestQueueName = "rpc_queue";
    private Channel channel;
    private String replyQueueName;

    public RPCClient() throws Exception {

        channel = ChannelHelper.getChannel();

        replyQueueName = channel.queueDeclare().getQueue();
    }

    public static void main(String[] argv) throws Exception {
        RPCClient fibonacciRpc = new RPCClient();
        try {
            Integer targetValue = 12;
            System.out.println(" [x] Requesting fib("+targetValue.toString()+")");

            String response = fibonacciRpc.call(targetValue.toString());

            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            ChannelHelper.closeChannel(fibonacciRpc.getChannel());
        }
    }

    //call
    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                    /*try {
                        response.offer(new String(body, "UTF-8"), 1, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        response.offer("unknown");
                    }*/
                }
            }
        });

        return response.take();
    }

    public Channel getChannel() {
        return channel;
    }
}
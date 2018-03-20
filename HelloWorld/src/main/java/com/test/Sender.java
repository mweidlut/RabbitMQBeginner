package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: ROOT
 * Date: 2018/3/13 20:35
 */
public class Sender {
    private static Logger logger = LoggerFactory.getLogger(Sender.class);

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        Channel channel = ChannelHelper.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));

        System.out.println(" [x] Sent '" + message + "'");

        ChannelHelper.closeChannel(channel);
    }

}

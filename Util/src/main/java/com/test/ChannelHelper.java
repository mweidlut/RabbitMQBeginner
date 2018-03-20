package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * User: weimeng
 * Date: 2018/3/20 10:45
 */
public class ChannelHelper {

    public static Channel getChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        return channel;
    }

    public static void closeChannel(Channel channel) {
        if (isNull(channel)) {
            return;
        }

        try {
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Connection connection = channel.getConnection();
        if (nonNull(connection)) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

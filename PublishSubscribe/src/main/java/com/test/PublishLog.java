package com.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * User: weimeng
 * Date: 2018/3/20 9:35
 */
public class PublishLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = ChannelHelper.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        for(int i=1; i<=2; i++){
            String message = "Hello World-"+i;

            channel.basicPublish(EXCHANGE_NAME, "diff-a", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }

        ChannelHelper.closeChannel(channel);
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1)
            return "info: Hello World!";
        return StringUtil.joinStrings(strings, " ");
    }

}
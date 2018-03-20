package com.test;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import static com.test.StringUtil.joinStrings;

/**
 * User: weimeng
 * Date: 2018/3/20 11:15
 */
public class PublishInfoLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {

        Channel channel = ChannelHelper.getChannel();

        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String routingKey = "quick.info.rabbit";
            String message = "info...message...";

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

        } finally {
            ChannelHelper.closeChannel(channel);
        }
    }

    private static String getRouting(String[] strings) {
        if (strings.length < 1)
            return "anonymous.info";
        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }


}
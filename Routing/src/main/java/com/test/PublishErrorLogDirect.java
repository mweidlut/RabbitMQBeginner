package com.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class PublishErrorLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";
    public static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.DIRECT;

    public static void main(String[] argv) throws Exception {

        Channel channel = ChannelHelper.getChannel();

        try {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            String routingKey = "error";

            for (int i = 0; i < 10; i++) {
                String message = "error message...something..." + i;
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ChannelHelper.closeChannel(channel);
        }
    }

    private static String getSeverity(String[] strings) {
        if (strings.length < 1)
            return "info";
        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 2)
            return "Hello World!";
        return StringUtil.joinStrings(strings, " ", 1);
    }

}
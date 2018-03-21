package com.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * User: weimeng
 * Date: 2018/3/15 11:38
 */
public class Producer1 {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {

        Channel channel = ChannelHelper.getChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        for (int i=0; i<10;i++){
            String message = getMessage(new String[]{"Task", ""+i, "."});
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        ChannelHelper.closeChannel(channel);
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1)
            return "Hello World!";
        return StringUtil.joinStrings(strings, "-");
    }



}

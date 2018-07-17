package org.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class TestRabbitListener {
    private static Logger logger = LoggerFactory.getLogger(TestRabbitListener.class);

    @RabbitHandler
    @RabbitListener(queues = LocalMQDefinition.local_queue_name, containerFactory = "localRabbitListenerContainerFactory")
    public void localHandleMessage(String message) throws Exception {
        logger.info(">>>receive local msg: {}", message);
    }

    @RabbitHandler
    @RabbitListener(queues = DevMQDefinition.dev_queue_name, containerFactory = "devRabbitListenerContainerFactory")
    public void devHandleMessage(String message) throws Exception {
        logger.info(">>>receive dev msg: {}", message);
    }

}

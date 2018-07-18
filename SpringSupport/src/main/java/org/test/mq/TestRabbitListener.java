package org.test.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Service
public class TestRabbitListener {
    private static Logger logger = LoggerFactory.getLogger(TestRabbitListener.class);

    @RabbitHandler
    @RabbitListener(queues = LocalMQDefinition.local_queue_name, containerFactory = "localRabbitListenerContainerFactory")
    public void localHandleMessage(@Payload String message) throws Exception {
        logger.info(">>>receive local msg: {}", message);
    }

    @RabbitHandler
    @RabbitListener(queues = LocalMQDefinition.local_queue_name, containerFactory = "localRabbitListenerContainerFactory")
    public void localHandleMessage(@Payload Object message) throws Exception {
        logger.info(">>>receive local msg2: {}", message);
    }

    @RabbitHandler
    @RabbitListener(queues = DevMQDefinition.dev_queue_name, containerFactory = "devRabbitListenerContainerFactory")
    public void devHandleMessage(@Payload String message) throws Exception {
        logger.info(">>>receive dev msg: {}", message);
    }

    @RabbitHandler
    @RabbitListener(queues = DevMQDefinition.dev_queue_name, containerFactory = "devRabbitListenerContainerFactory")
    public void devHandleMessage(@Payload Object message) throws Exception {
        logger.info(">>>receive dev msg2: {}", message);
    }
}

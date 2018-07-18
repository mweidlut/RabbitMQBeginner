package org.test.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.test.mq.DevMQDefinition;
import org.test.mq.LocalMQDefinition;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试用
 * User: weimeng
 * Date: 2018/7/17 16:37
 */
@RestController
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    private AtomicInteger atomicInteger = new AtomicInteger(1);

    @Inject
    private RabbitTemplate localRabbitTemplate;
    @Inject
    private RabbitTemplate devRabbitTemplate;


    @RequestMapping(value = "/send/local", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String sendLocalMsg() {

        localRabbitTemplate.convertAndSend(LocalMQDefinition.local_exchange_name, "", "from local..." + atomicInteger.getAndIncrement());
        logger.info("sendLocalMsg...");

        return "local done";
    }

    @RequestMapping(value = "/send/dev", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String sendDevMsg() {

        devRabbitTemplate.convertAndSend(DevMQDefinition.dev_exchange_name, "", "from dev..." + atomicInteger.getAndIncrement());
        logger.info("sendDevMsg...");

        return "dev done";
    }
}

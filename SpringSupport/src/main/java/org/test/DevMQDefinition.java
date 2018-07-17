package org.test;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Configuration
public class DevMQDefinition {

    public static final String dev_queue_name = "dev_queue_test";
    private static final String dev_exchange_name = "dev_exchange_test";

    @Inject
    private RabbitMQConfig rabbitMQConfig;

    @Bean(name = "devConnectionFactory")
    public ConnectionFactory devConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMQConfig.getDev().getAddresses());
        connectionFactory.setUsername(rabbitMQConfig.getDev().getUsername());
        connectionFactory.setPassword(rabbitMQConfig.getDev().getPassword());
        connectionFactory.setVirtualHost(rabbitMQConfig.getDev().getVhost());
        return connectionFactory;
    }

    @Bean(name = "devRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory devRabbitListenerContainerFactory(@Autowired @Qualifier("devConnectionFactory") ConnectionFactory devConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(devConnectionFactory);
        return factory;
    }

    @Bean
    public RabbitTemplate devRabbitTemplate(@Autowired @Qualifier("devConnectionFactory") ConnectionFactory devConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(devConnectionFactory);
        //template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public AmqpAdmin devAmqpAdmin(@Autowired @Qualifier("devConnectionFactory") ConnectionFactory devConnectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(devConnectionFactory);
        admin.setAutoStartup(true);

        Queue queue = new Queue(dev_queue_name, false, false, true);
        TopicExchange topicExchange = new TopicExchange(dev_exchange_name);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("#");//all topic

        admin.declareQueue(queue);
        admin.declareExchange(topicExchange);
        admin.declareBinding(binding);

        return admin;
    }


    /*
    //如果使用下面的声明方式，queue和exchange会被注册到两个mq源上
    @Bean
    public Queue devQueue(@Autowired AmqpAdmin devAmqpAdmin) {
        //Declares a non-exclusive, autodelete, non-durable queue.
        Queue queue = new Queue(dev_queue_name, false, false, true);
        //devAmqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public TopicExchange devTopicExchange(@Autowired AmqpAdmin devAmqpAdmin) {
        TopicExchange topicExchange = new TopicExchange(dev_exchange_name);
        //devAmqpAdmin.declareExchange(topicExchange);
        return topicExchange;
    }

    @Bean
    public Binding binding(@Autowired Queue devQueue, @Autowired TopicExchange devTopicExchange) {
        return BindingBuilder.bind(devQueue).to(devTopicExchange).with("#"); //all topic
    }*/
}

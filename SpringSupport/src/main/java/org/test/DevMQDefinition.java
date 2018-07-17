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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Configuration
public class DevMQDefinition {

    public static final String dev_queue_name = "dev_queue_test";
    private static final String dev_exchange_name = "dev_exchange_test";

    @Inject
    private RabbitMQConfig rabbitMQConfig;

//    @Primary
    @Bean(name = "devConnectFactory")
    public ConnectionFactory bConnectFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMQConfig.getDev().getAddresses());
        connectionFactory.setUsername(rabbitMQConfig.getDev().getUsername());
        connectionFactory.setPassword(rabbitMQConfig.getDev().getPassword());
        connectionFactory.setVirtualHost(rabbitMQConfig.getDev().getVhost());
        return connectionFactory;
    }

    @Bean(name = "devRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory brabbitListenerContainerFactory(@Qualifier("devConnectFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean(name = "devAmqpAdmin")
    public AmqpAdmin devAmqpAdmin(@Qualifier("devConnectFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

//    @Primary
    @Bean
    public RabbitTemplate devRabbitTemplate(@Autowired @Qualifier("devConnectFactory") ConnectionFactory devConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(devConnectionFactory);
        //template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public Queue devQueue() {
        //Declares a non-exclusive, autodelete, non-durable queue.
        Queue queue = new Queue(dev_queue_name, false, false, true);
        return queue;
    }

    @Bean
    public TopicExchange devTopicExchange() {
        return new TopicExchange(dev_exchange_name);
    }

    @Bean
    public Binding binding(@Autowired Queue devQueue, @Autowired TopicExchange devTopicExchange) {
        return BindingBuilder.bind(devQueue).to(devTopicExchange).with("#"); //all topic
    }
}

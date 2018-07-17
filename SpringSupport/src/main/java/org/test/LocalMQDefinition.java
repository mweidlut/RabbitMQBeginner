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
public class LocalMQDefinition {

    public static final String local_queue_name = "local_queue_test";
    private static final String local_exchange_name = "local_exchange_test";

    @Inject
    private RabbitMQConfig rabbitMQConfig;

    @Primary
    @Bean
    public ConnectionFactory localConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMQConfig.getLocal().getAddresses());
        connectionFactory.setUsername(rabbitMQConfig.getLocal().getUsername());
        connectionFactory.setPassword(rabbitMQConfig.getLocal().getPassword());
        connectionFactory.setVirtualHost(rabbitMQConfig.getLocal().getVhost());
        return connectionFactory;
    }

    @Bean(name = "localRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory localRabbitListenerContainerFactory(@Autowired ConnectionFactory localConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(localConnectionFactory);
        return factory;
    }

    @Bean
    public AmqpAdmin localAmqpAdmin(@Autowired ConnectionFactory localConnectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(localConnectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Primary
    @Bean
    public RabbitTemplate localRabbitTemplate(@Autowired ConnectionFactory localConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(localConnectionFactory);
        //template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public Queue localQueue() {
        //Declares a non-exclusive, autodelete, non-durable queue.
        Queue queue = new Queue(local_queue_name, false, false, true);
        return queue;
    }

    @Bean
    public TopicExchange localTopicExchange() {
        return new TopicExchange(local_exchange_name);
    }

    @Bean
    public Binding binding(@Autowired Queue localQueue, @Autowired TopicExchange localTopicExchange) {
        return BindingBuilder.bind(localQueue).to(localTopicExchange).with("#"); //all topic
    }
}

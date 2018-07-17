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
    @Bean(name = "localConnectionFactory")
    public ConnectionFactory localConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMQConfig.getLocal().getAddresses());
        connectionFactory.setUsername(rabbitMQConfig.getLocal().getUsername());
        connectionFactory.setPassword(rabbitMQConfig.getLocal().getPassword());
        connectionFactory.setVirtualHost(rabbitMQConfig.getLocal().getVhost());
        return connectionFactory;
    }

    @Bean(name = "localRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory localRabbitListenerContainerFactory(@Autowired @Qualifier("localConnectionFactory") ConnectionFactory localConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(localConnectionFactory);
        return factory;
    }

    @Primary
    @Bean
    public RabbitTemplate localRabbitTemplate(@Autowired @Qualifier("localConnectionFactory") ConnectionFactory localConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(localConnectionFactory);
        //template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Primary
    @Bean
    public AmqpAdmin localAmqpAdmin(@Autowired @Qualifier("localConnectionFactory") ConnectionFactory localConnectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(localConnectionFactory);
        admin.setAutoStartup(true);

        Queue queue = new Queue(local_queue_name, false, false, true);
        TopicExchange topicExchange = new TopicExchange(local_exchange_name);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("#");//all topic

        admin.declareQueue(queue);
        admin.declareExchange(topicExchange);
        admin.declareBinding(binding);

        return admin;
    }


    /*
    //如果使用下面的声明方式，queue和exchange会被注册到两个mq源上
    @Bean
    public Queue localQueue(@Autowired AmqpAdmin localAmqpAdmin) {
        //Declares a non-exclusive, autodelete, non-durable queue.
        Queue queue = new Queue(local_queue_name, false, false, true);
        //localAmqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public TopicExchange localTopicExchange(@Autowired AmqpAdmin localAmqpAdmin) {
        TopicExchange topicExchange = new TopicExchange(local_exchange_name);
        //localAmqpAdmin.declareExchange(topicExchange);
        return topicExchange;
    }

    @Bean
    public Binding binding(@Autowired Queue localQueue, @Autowired TopicExchange localTopicExchange) {
        return BindingBuilder.bind(localQueue).to(localTopicExchange).with("#"); //all topic
    }*/

}

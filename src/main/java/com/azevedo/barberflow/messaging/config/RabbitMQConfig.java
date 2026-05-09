package com.azevedo.barberflow.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "appointment.exchange";

    public static final String CREATED_QUEUE = "appointment.created.queue";

    public static final String CREATED_ROUTING_KEY = "appointment.created";

    public static final String CANCELED_QUEUE = "appointment.canceled.queue";

    public static final String CANCELED_ROUTING_KEY = "appointment.canceled";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(CREATED_QUEUE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue canceledQueue() {
        return new Queue(CANCELED_QUEUE);
    }

    @Bean
    public Binding canceledBinding() {
        return BindingBuilder
                .bind(canceledQueue())
                .to(exchange())
                .with(CANCELED_ROUTING_KEY);
    }
}
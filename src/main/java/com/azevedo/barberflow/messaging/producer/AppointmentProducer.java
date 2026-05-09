package com.azevedo.barberflow.messaging.producer;

import com.azevedo.barberflow.messaging.config.RabbitMQConfig;
import com.azevedo.barberflow.messaging.event.AppointmentCanceledEvent;
import com.azevedo.barberflow.messaging.event.AppointmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendAppointmentCreated(AppointmentCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CREATED_ROUTING_KEY,
                event
        );
    }

    public void sendAppointmentCanceled(AppointmentCanceledEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CANCELED_ROUTING_KEY,
                event
        );
    }

}

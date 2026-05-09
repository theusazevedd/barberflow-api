package com.azevedo.barberflow.messaging.consumer;

import com.azevedo.barberflow.messaging.event.AppointmentCanceledEvent;
import com.azevedo.barberflow.messaging.event.AppointmentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppointmentConsumer {

    @RabbitListener(queues = "appointment.created.queue")
    public void consume(AppointmentCreatedEvent event) {

        log.info("""

                EVENT RECEIVED

                appointmentId={}
                barberId={}
                clientId={}
                dateTime={}

                """,
                event.appointmentId(),
                event.barberId(),
                event.clientId(),
                event.dateTime()
        );
    }

    @RabbitListener(queues = "appointment.canceled.queue")
    public void consumeCanceled(AppointmentCanceledEvent event) {

        log.info("""

            APPOINTMENT CANCELED

            appointmentId={}
            barberId={}
            clientId={}

            """,
                event.appointmentId(),
                event.barberId(),
                event.clientId()
        );
    }

}

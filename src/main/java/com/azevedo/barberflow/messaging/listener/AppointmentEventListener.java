package com.azevedo.barberflow.messaging.listener;

import com.azevedo.barberflow.messaging.event.AppointmentCanceledEvent;
import com.azevedo.barberflow.messaging.event.AppointmentCreatedEvent;
import com.azevedo.barberflow.messaging.producer.AppointmentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AppointmentEventListener {

    private final AppointmentProducer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAppointmentCreated(AppointmentCreatedEvent event) {
        producer.sendAppointmentCreated(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAppointmentCanceled(AppointmentCanceledEvent event) {
        producer.sendAppointmentCanceled(event);
    }
}

package com.azevedo.barberflow.messaging.event;

import java.time.LocalDateTime;

public record AppointmentCreatedEvent(
        Long appointmentId,
        Long barberId,
        Long clientId,
        LocalDateTime dateTime

) {}

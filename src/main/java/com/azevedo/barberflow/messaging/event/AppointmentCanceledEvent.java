package com.azevedo.barberflow.messaging.event;

public record AppointmentCanceledEvent(
        Long appointmentId,
        Long barberId,
        Long clientId
) {}

package com.azevedo.barberflow.dto.response;

import com.azevedo.barberflow.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,
        String barberName,
        String clientName,
        LocalDateTime dateTime,
        AppointmentStatus status
) {}

package com.azevedo.barberflow.dto.response;

import com.azevedo.barberflow.domain.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,
        String barberName,
        String clientName,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dateTime,
        AppointmentStatus status
) {}

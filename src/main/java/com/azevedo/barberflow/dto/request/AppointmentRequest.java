package com.azevedo.barberflow.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(

        @NotNull(message = "barberId is required")
        Long barberId,

        @NotNull(message = "dateTime is required")
        @Future(message = "dateTime must be in the future")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dateTime
) {}

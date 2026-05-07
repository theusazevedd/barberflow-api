package com.azevedo.barberflow.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record BarberScheduleRequest(

        @NotNull(message = "startTime is required")
        LocalTime startTime,

        @NotNull(message = "endTime is required")
        LocalTime endTime,

        @Min(value = 5, message = "durationMinutes must be at least 5")
        @Max(value = 240, message = "durationMinutes must be at most 240")
        int durationMinutes
) {}

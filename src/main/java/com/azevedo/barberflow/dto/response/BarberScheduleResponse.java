package com.azevedo.barberflow.dto.response;

import java.time.LocalTime;

public record BarberScheduleResponse(

        Long id,
        LocalTime startTime,
        LocalTime endTime,
        int durationMinutes
) {}

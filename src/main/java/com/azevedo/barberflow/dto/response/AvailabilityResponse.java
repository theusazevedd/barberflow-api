package com.azevedo.barberflow.dto.response;

import java.time.LocalDateTime;

public record AvailabilityResponse(

        Long id,
        LocalDateTime dateTime,
        boolean available
) {}

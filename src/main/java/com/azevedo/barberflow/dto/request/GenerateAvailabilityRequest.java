package com.azevedo.barberflow.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GenerateAvailabilityRequest(

        @NotNull(message = "date is required")
        @FutureOrPresent(message = "date must be today or in the future")
        LocalDate date
) {}

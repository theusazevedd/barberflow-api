package com.azevedo.barberflow.mapper;

import com.azevedo.barberflow.domain.model.BarberSchedule;
import com.azevedo.barberflow.dto.response.BarberScheduleResponse;
import org.springframework.stereotype.Component;

@Component
public class BarberScheduleMapper {

    // entity → DTO
    public BarberScheduleResponse toResponse(BarberSchedule schedule) {
        return new BarberScheduleResponse(
                schedule.getId(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDurationMinutes()
        );
    }
}

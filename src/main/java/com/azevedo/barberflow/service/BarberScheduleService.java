package com.azevedo.barberflow.service;

import com.azevedo.barberflow.domain.enums.Role;
import com.azevedo.barberflow.domain.model.BarberSchedule;
import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.BarberScheduleRequest;
import com.azevedo.barberflow.dto.response.BarberScheduleResponse;
import com.azevedo.barberflow.exception.BusinessException;
import com.azevedo.barberflow.exception.ResourceNotFoundException;
import com.azevedo.barberflow.mapper.BarberScheduleMapper;
import com.azevedo.barberflow.repository.BarberScheduleRepository;
import com.azevedo.barberflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BarberScheduleService {

    private final UserRepository userRepository;
    private final BarberScheduleRepository barberScheduleRepository;
    private final BarberScheduleMapper barberScheduleMapper;

    @Transactional
    public BarberScheduleResponse saveOrUpdate(Long barberId, BarberScheduleRequest request) {

        validateWindow(request);

        User barber = userRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!Role.BARBER.equals(barber.getRole())) {
            throw new BusinessException("Only barbers can define a schedule");
        }

        BarberSchedule schedule = barberScheduleRepository.findByBarber(barber)
                .orElseGet(() -> {
                    BarberSchedule s = new BarberSchedule();
                    s.setBarber(barber);
                    return s;
                });

        schedule.setStartTime(request.startTime());
        schedule.setEndTime(request.endTime());
        schedule.setDurationMinutes(request.durationMinutes());

        BarberSchedule saved = barberScheduleRepository.save(schedule);
        return barberScheduleMapper.toResponse(saved);
    }

    private static void validateWindow(BarberScheduleRequest request) {

        if (request.durationMinutes() <= 0) {
            throw new BusinessException("durationMinutes must be positive");
        }

        if (!request.endTime().isAfter(request.startTime())) {
            throw new BusinessException("endTime must be after startTime");
        }
    }

    @Transactional(readOnly = true)
    public BarberScheduleResponse getMySchedule(Long barberId) {

        User barber = userRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BarberSchedule schedule = barberScheduleRepository.findByBarber(barber)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        return barberScheduleMapper.toResponse(schedule);

    }

}

package com.azevedo.barberflow.service;

import com.azevedo.barberflow.domain.enums.Role;
import com.azevedo.barberflow.domain.model.Availability;
import com.azevedo.barberflow.domain.model.BarberSchedule;
import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.GenerateAvailabilityRequest;
import com.azevedo.barberflow.dto.response.AvailabilityResponse;
import com.azevedo.barberflow.exception.BusinessException;
import com.azevedo.barberflow.exception.ResourceNotFoundException;
import com.azevedo.barberflow.repository.AvailabilityRepository;
import com.azevedo.barberflow.repository.BarberScheduleRepository;
import com.azevedo.barberflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final BarberScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<AvailabilityResponse> generate(
            Long barberId,
            GenerateAvailabilityRequest request
    ) {

        LocalDate date = request.date();

        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException("Cannot generate availability in the past");
        }

        User barber = userRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!Role.BARBER.equals(barber.getRole())) {
            throw new BusinessException("Only barbers can generate availability");
        }

        BarberSchedule schedule = scheduleRepository.findByBarber(barber)
                .orElseThrow(() -> new BusinessException("Schedule not defined"));

        LocalTime start = schedule.getStartTime();
        LocalTime end = schedule.getEndTime();
        int duration = schedule.getDurationMinutes();

        List<Availability> slots = new ArrayList<>();

        LocalTime current = start;

        while (current.isBefore(end)) {

            if (date.equals(LocalDate.now()) && current.isBefore(LocalTime.now())) {
                current = current.plusMinutes(duration);
                continue;
            }

            LocalDateTime dateTime = LocalDateTime.of(date, current);

            boolean exists = availabilityRepository
                    .findByBarberIdAndDateTime(barber.getId(), dateTime)
                    .isPresent();

            if (!exists) {
                Availability availability = new Availability();
                availability.setBarber(barber);
                availability.setDateTime(dateTime);
                availability.setAvailable(true);

                slots.add(availability);
            }

            current = current.plusMinutes(duration);
        }

        availabilityRepository.saveAll(slots);

        log.info("Generated {} slots for barber {} on {}",
                slots.size(), barberId, date);

        return slots.stream()
                .map(a -> new AvailabilityResponse(
                        a.getId(),
                        a.getDateTime(),
                        a.isAvailable()
                ))
                .toList();
    }

    public List<AvailabilityResponse> listAvailable(Long barberId) {

        return availabilityRepository.findByBarberIdAndAvailableTrue(barberId)
                .stream()
                .filter(a -> !a.getDateTime().isBefore(LocalDateTime.now()))
                .map(a -> new AvailabilityResponse(
                        a.getId(),
                        a.getDateTime(),
                        a.isAvailable()
                ))
                .toList();
    }

}

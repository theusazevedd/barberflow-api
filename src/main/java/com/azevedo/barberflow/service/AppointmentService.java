package com.azevedo.barberflow.service;

import com.azevedo.barberflow.domain.enums.AppointmentStatus;
import com.azevedo.barberflow.domain.enums.Role;
import com.azevedo.barberflow.domain.model.Appointment;
import com.azevedo.barberflow.domain.model.Availability;
import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.AppointmentRequest;
import com.azevedo.barberflow.dto.response.AppointmentResponse;
import com.azevedo.barberflow.exception.BusinessException;
import com.azevedo.barberflow.exception.ResourceNotFoundException;
import com.azevedo.barberflow.repository.AppointmentRepository;
import com.azevedo.barberflow.repository.AvailabilityRepository;
import com.azevedo.barberflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    @Transactional
    public AppointmentResponse create(Long clientId, AppointmentRequest request) {

        if (request.dateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot schedule in the past");
        }

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Availability availability = availabilityRepository
                .findByBarberIdAndDateTime(request.barberId(), request.dateTime())
                .orElseThrow(() -> new BusinessException("Time slot not found"));

        if (!availability.isAvailable()) {
            throw new BusinessException("Time slot already booked");
        }

        if (!availability.isAvailable()) {
            throw new BusinessException("Time slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setBarber(availability.getBarber());
        appointment.setClient(client);
        appointment.setDateTime(request.dateTime());
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        availability.setAvailable(false);
        appointmentRepository.save(appointment);

        log.info("Appointment created | client={} barber={} time={}",
                clientId,
                availability.getBarber().getId(),
                request.dateTime());

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getBarber().getName(),
                appointment.getClient().getName(),
                appointment.getDateTime(),
                appointment.getStatus()
        );

    }

    @Transactional
    public void cancel(Long appointmentId, Long userId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        boolean isOwner = appointment.getClient().getId().equals(userId)
                || appointment.getBarber().getId().equals(userId);

        if (!isOwner) {
            throw new BusinessException("You cannot cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);

        Availability availability = availabilityRepository
                .findByBarberIdAndDateTime(
                        appointment.getBarber().getId(),
                        appointment.getDateTime()
                )
                .orElseThrow(() -> new BusinessException("Availability not found"));

        availability.setAvailable(true);

        log.info("Appointment canceled | id={}", appointmentId);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyAppointments(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Appointment> appointments;

        if (Role.BARBER.equals(user.getRole())) {
            appointments = appointmentRepository.findByBarberId(userId);
        } else {
            appointments = appointmentRepository.findByClientId(userId);
        }

        return appointments.stream()
                .map(a -> new AppointmentResponse(
                        a.getId(),
                        a.getBarber().getName(),
                        a.getClient().getName(),
                        a.getDateTime(),
                        a.getStatus()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id, Long userId) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        boolean isOwner = appointment.getClient().getId().equals(userId)
                || appointment.getBarber().getId().equals(userId);

        if (!isOwner) {
            throw new BusinessException("You cannot access this appointment");
        }

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getBarber().getName(),
                appointment.getClient().getName(),
                appointment.getDateTime(),
                appointment.getStatus()
        );
    }






}

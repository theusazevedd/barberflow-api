package com.azevedo.barberflow.repository;

import com.azevedo.barberflow.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByClientId(Long clientId);

    List<Appointment> findByBarberId(Long barberId);

    boolean existsByBarberIdAndDateTime(Long barberId, LocalDateTime dateTime);

}

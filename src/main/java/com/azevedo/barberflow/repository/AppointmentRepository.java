package com.azevedo.barberflow.repository;

import com.azevedo.barberflow.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}

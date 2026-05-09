package com.azevedo.barberflow.repository;

import com.azevedo.barberflow.domain.model.BarberSchedule;
import com.azevedo.barberflow.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarberScheduleRepository extends JpaRepository<BarberSchedule, Long> {

    Optional<BarberSchedule> findByBarber(User barber);

}

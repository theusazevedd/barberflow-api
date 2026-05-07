package com.azevedo.barberflow.repository;

import com.azevedo.barberflow.domain.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    Optional<Availability> findByBarberIdAndDateTime(Long barberId, LocalDateTime dateTime);

    List<Availability> findByBarberIdAndAvailableTrue(Long barberId);
}

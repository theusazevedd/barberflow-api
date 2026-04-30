package com.azevedo.barberflow.repository;

import com.azevedo.barberflow.domain.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

}

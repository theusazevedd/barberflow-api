package com.azevedo.barberflow.repository;

import com.azevedo.barberflow.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

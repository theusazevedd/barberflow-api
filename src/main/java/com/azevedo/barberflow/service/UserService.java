package com.azevedo.barberflow.service;

import com.azevedo.barberflow.domain.enums.Role;
import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.UpdateUserRequest;
import com.azevedo.barberflow.dto.response.BarberResponse;
import com.azevedo.barberflow.dto.response.UserResponse;
import com.azevedo.barberflow.exception.BusinessException;
import com.azevedo.barberflow.exception.ResourceNotFoundException;
import com.azevedo.barberflow.mapper.UserMapper;
import com.azevedo.barberflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.email() != null && !request.email().isBlank()
                && !request.email().equals(user.getEmail())) {
            authService.checkEmailAvailability(request.email());
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return userMapper.toResponse(userRepository.save(user));

    }

    @Transactional
    public UserResponse becomeBarber(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (Role.BARBER.equals(user.getRole())) {
            throw new BusinessException("User is already a barber");
        }

        user.setRole(Role.BARBER);

        return userMapper.toResponse(user);
    }

    public List<BarberResponse> getAllBarbers() {
        return userRepository.findByRole(Role.BARBER)
                .stream()
                .map(user -> new BarberResponse(
                        user.getId(),
                        user.getName()
                ))
                .toList();
    }


}

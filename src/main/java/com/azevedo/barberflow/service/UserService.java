package com.azevedo.barberflow.service;

import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.UpdateUserRequest;
import com.azevedo.barberflow.dto.response.UserResponse;
import com.azevedo.barberflow.exception.ResourceNotFoundException;
import com.azevedo.barberflow.mapper.UserMapper;
import com.azevedo.barberflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {

        Long userId = currentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteCurrentUser() {

        Long userId = currentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Transactional
    public UserResponse update(UpdateUserRequest request) {

        Long userId = currentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            authService.checkEmailAvailability(request.email());
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return userMapper.toResponse(userRepository.save(user));

    }

    private Long currentUserId() {
        String id = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return Long.parseLong(id);
    }

}

package com.azevedo.barberflow.service;

import com.azevedo.barberflow.domain.enums.Role;
import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.LoginRequest;
import com.azevedo.barberflow.dto.request.RegisterRequest;
import com.azevedo.barberflow.dto.response.AuthResponse;
import com.azevedo.barberflow.dto.response.UserResponse;
import com.azevedo.barberflow.exception.BadCredentialsException;
import com.azevedo.barberflow.exception.ConflictException;
import com.azevedo.barberflow.exception.ResourceNotFoundException;
import com.azevedo.barberflow.mapper.UserMapper;
import com.azevedo.barberflow.repository.UserRepository;
import com.azevedo.barberflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponse registerUser(RegisterRequest request) {
        checkEmailAvailability(request.email());
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CLIENT);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already registered " + email);

        }
    }

    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer");

    }


}

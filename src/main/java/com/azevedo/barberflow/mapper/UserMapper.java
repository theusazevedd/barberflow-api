package com.azevedo.barberflow.mapper;

import com.azevedo.barberflow.domain.model.User;
import com.azevedo.barberflow.dto.request.RegisterRequest;
import com.azevedo.barberflow.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }


}

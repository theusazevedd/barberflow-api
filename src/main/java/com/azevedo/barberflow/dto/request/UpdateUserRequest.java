package com.azevedo.barberflow.dto.request;

public record UpdateUserRequest(

        String name,
        String email,
        String password

) {}

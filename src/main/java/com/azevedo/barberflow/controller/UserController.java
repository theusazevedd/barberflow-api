package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.UpdateUserRequest;
import com.azevedo.barberflow.dto.response.BarberResponse;
import com.azevedo.barberflow.dto.response.UserResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getById(@AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(service.getById(user.getId()));

    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal CustomUserDetails user) {
        service.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal CustomUserDetails user,
                                               @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(service.update(user.getId(), request));
    }

    @PatchMapping("/me/become-barber")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<UserResponse> becomeBarber(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                service.becomeBarber(user.getId())
        );
    }

    @GetMapping("/barbers")
    public ResponseEntity<List<BarberResponse>> getBarbers() {
        return ResponseEntity.ok(service.getAllBarbers());
    }

}

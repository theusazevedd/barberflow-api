package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.AppointmentRequest;
import com.azevedo.barberflow.dto.response.AppointmentResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AppointmentResponse> create(@AuthenticationPrincipal CustomUserDetails user,
                                                      @Valid @RequestBody AppointmentRequest request) {

        return ResponseEntity.ok(service.create(user.getId(), request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.cancel(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getMyAppointments(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getById(id, user.getId()));
    }


}

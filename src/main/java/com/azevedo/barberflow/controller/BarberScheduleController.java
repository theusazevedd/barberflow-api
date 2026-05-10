package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.BarberScheduleRequest;
import com.azevedo.barberflow.dto.response.BarberScheduleResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.BarberScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barbers/me/schedule")
@RequiredArgsConstructor
@Tag(name = "Barber schedule")
public class BarberScheduleController {

    private final BarberScheduleService service;

    @PutMapping
    @PreAuthorize("hasRole('BARBER')")
    public ResponseEntity<BarberScheduleResponse> saveOrUpdate(@AuthenticationPrincipal CustomUserDetails user,
                                                               @Valid @RequestBody BarberScheduleRequest request) {
        return ResponseEntity.ok(service.saveOrUpdate(user.getId(), request));
    }

    @GetMapping
    @PreAuthorize("hasRole('BARBER')")
    public ResponseEntity<BarberScheduleResponse> getMySchedule(@AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(service.getMySchedule(user.getId()));
    }



}

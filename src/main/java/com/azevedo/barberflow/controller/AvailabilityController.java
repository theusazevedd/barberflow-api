package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.GenerateAvailabilityRequest;
import com.azevedo.barberflow.dto.response.AvailabilityResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService service;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('BARBER')")
    public ResponseEntity<List<AvailabilityResponse>> generate(@AuthenticationPrincipal CustomUserDetails user,
                                                               @Valid @RequestBody GenerateAvailabilityRequest request) {
        return ResponseEntity.ok(service.generate(user.getId(), request));
    }

    @GetMapping("/{barberId}")
    public ResponseEntity<List<AvailabilityResponse>> listAvailable(@PathVariable Long barberId) {
        return ResponseEntity.ok(service.listAvailable(barberId));
    }

}

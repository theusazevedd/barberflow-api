package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.AppointmentRequest;
import com.azevedo.barberflow.dto.response.AppointmentResponse;
import com.azevedo.barberflow.dto.response.ErrorResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments")
public class AppointmentController {

    private final AppointmentService service;

    @Operation(
            summary = "Book a time slot",
            description = "CLIENT only. Marks the availability slot as taken and publishes a domain event."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment confirmed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AppointmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid time, slot unavailable, or business rule",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Not a CLIENT"),
            @ApiResponse(responseCode = "409", description = "Slot already booked (race)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AppointmentResponse> create(@AuthenticationPrincipal CustomUserDetails user,
                                                      @Valid @RequestBody AppointmentRequest request) {

        return ResponseEntity.ok(service.create(user.getId(), request));
    }

    @Operation(summary = "Cancel appointment", description = "Client or barber who owns the appointment.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canceled"),
            @ApiResponse(responseCode = "400", description = "Not allowed to cancel",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.cancel(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List my appointments")
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getMyAppointments(user.getId()));
    }

    @Operation(summary = "Get appointment by id", description = "Only client or barber involved.")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getById(id, user.getId()));
    }


}

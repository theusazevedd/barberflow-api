package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.GenerateAvailabilityRequest;
import com.azevedo.barberflow.dto.response.AvailabilityResponse;
import com.azevedo.barberflow.dto.response.ErrorResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("/availability")
@RequiredArgsConstructor
@Tag(name = "Availability")
public class AvailabilityController {

    private final AvailabilityService service;

    @Operation(
            summary = "Generate availability slots",
            description = "BARBER only. Creates bookable slots for a date range (implementation-specific rules)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slots created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AvailabilityResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid range or overlaps",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Not a BARBER")
    })
    @PostMapping("/generate")
    @PreAuthorize("hasRole('BARBER')")
    public ResponseEntity<List<AvailabilityResponse>> generate(@AuthenticationPrincipal CustomUserDetails user,
                                                               @Valid @RequestBody GenerateAvailabilityRequest request) {
        return ResponseEntity.ok(service.generate(user.getId(), request));
    }

    @Operation(summary = "List open slots for a barber")
    @GetMapping("/{barberId}")
    public ResponseEntity<List<AvailabilityResponse>> listAvailable(
            @Parameter(description = "Barber user id") @PathVariable Long barberId) {
        return ResponseEntity.ok(service.listAvailable(barberId));
    }

}

package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.UpdateUserRequest;
import com.azevedo.barberflow.dto.response.BarberResponse;
import com.azevedo.barberflow.dto.response.ErrorResponse;
import com.azevedo.barberflow.dto.response.UserResponse;
import com.azevedo.barberflow.security.CustomUserDetails;
import com.azevedo.barberflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users")
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

    @Operation(
            summary = "Promote account to barber",
            description = """
                    CLIENT only. One-way role change for this API: after promotion the user can maintain `/barbers/me/schedule` \
                    and call `POST /availability/generate`. Idempotent only in the sense that a second call returns **400** \
                    if the user is already a BARBER.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role is now BARBER",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Already a barber",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not CLIENT"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
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

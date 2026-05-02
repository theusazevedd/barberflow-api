package com.azevedo.barberflow.controller;

import com.azevedo.barberflow.dto.request.UpdateUserRequest;
import com.azevedo.barberflow.dto.response.UserResponse;
import com.azevedo.barberflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> update(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }

}

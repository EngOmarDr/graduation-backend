package com.graduationProject._thYear.Auth.controllers;

import com.graduationProject._thYear.Auth.dtos.request.AuthenticationRequest;
import com.graduationProject._thYear.Auth.dtos.request.ChangePasswordRequest;
import com.graduationProject._thYear.Auth.dtos.request.UpdateUserRequest;
import com.graduationProject._thYear.Auth.dtos.response.AuthenticationResponse;
import com.graduationProject._thYear.Auth.dtos.request.RegisterRequest;
import com.graduationProject._thYear.Auth.dtos.response.UserResponse;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Auth.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
//@Autowired
//    public AuthenticationController(AuthenticationService service) {
//        this.service = service;
//    }

    //  Register Only ADMIN
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthenticationResponse> register(
         @Valid @RequestBody RegisterRequest request,
         @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(service.register(request, currentUser));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid   @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}

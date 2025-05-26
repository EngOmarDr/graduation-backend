package com.graduationProject._thYear.Auth.controllers;

import com.graduationProject._thYear.Auth.dtos.request.AuthenticationRequest;
import com.graduationProject._thYear.Auth.dtos.response.AuthenticationResponse;
import com.graduationProject._thYear.Auth.dtos.request.RegisterRequest;
import com.graduationProject._thYear.Auth.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
//@Autowired
//    public AuthenticationController(AuthenticationService service) {
//        this.service = service;
//    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
         @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid   @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}

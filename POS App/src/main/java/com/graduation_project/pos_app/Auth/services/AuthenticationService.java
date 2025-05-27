package com.graduation_project.pos_app.Auth.services;

import com.graduation_project.pos_app.Auth.config.JWTService;
import com.graduation_project.pos_app.Auth.dtos.request.AuthenticationRequest;
import com.graduation_project.pos_app.Auth.dtos.response.AuthenticationResponse;
import com.graduation_project.pos_app.Auth.dtos.request.RegisterRequest;
import com.graduation_project.pos_app.exceptionHandler.LoginUnauthorizedException;
import com.graduation_project.pos_app.Auth.models.Role;
import com.graduation_project.pos_app.Auth.models.User;
import com.graduation_project.pos_app.Auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
  //  private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LoginUnauthorizedException("User not found with email: " + request.getEmail()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new LoginUnauthorizedException("Incorrect password");
        }

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }



//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = this.repository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}

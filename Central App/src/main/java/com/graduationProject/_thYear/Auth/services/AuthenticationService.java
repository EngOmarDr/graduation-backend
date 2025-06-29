package com.graduationProject._thYear.Auth.services;

import com.graduationProject._thYear.Auth.config.JWTService;
import com.graduationProject._thYear.Auth.dtos.request.AuthenticationRequest;
import com.graduationProject._thYear.Auth.dtos.request.ChangePasswordRequest;
import com.graduationProject._thYear.Auth.dtos.request.UpdateUserRequest;
import com.graduationProject._thYear.Auth.dtos.response.AuthenticationResponse;
import com.graduationProject._thYear.Auth.dtos.request.RegisterRequest;
import com.graduationProject._thYear.Auth.dtos.response.UserResponse;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.exceptionHandler.LoginUnauthorizedException;
import com.graduationProject._thYear.Auth.models.Role;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
  //  private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private final BranchRepository branchRepository;


    public AuthenticationResponse register(RegisterRequest request, User currentUser) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }

        Role roleEnum;
        try {
            roleEnum = Role.valueOf(String.valueOf(request.getRole()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        Branch branch = null;

        if (roleEnum != Role.ADMIN) {
            if (request.getBranchId() == null) {
                throw new IllegalArgumentException("Branch ID is required for non-admin users.");
            }
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleEnum)
                .branch(branch)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new LoginUnauthorizedException("User not found with email: " + request.getUsername()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new LoginUnauthorizedException("Incorrect password");
        }

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getUsername())
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
//        final String userUsername;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userUsername = jwtService.extractUsername(refreshToken);
//        if (userUsername != null) {
//            var user = this.repository.findByUsername(userUsername)
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

    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public UserResponse getUserById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Integer id, UpdateUserRequest request, User currentUser) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Non-admin users can't change their role
        if (currentUser.getRole() != Role.ADMIN && request.getRole() != null) {
            throw new RuntimeException("Only admins can change roles");
        }

        // Update fields
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getRole() != null) user.setRole(request.getRole());

        // Handle branch (admin can be branchless)
        if (request.getBranchId() != null) {
            if (user.getRole() == Role.ADMIN) {
                user.setBranch(null);
            } else {
                Branch branch = branchRepository.findById(request.getBranchId())
                        .orElseThrow(() -> new RuntimeException("Branch not found"));
                user.setBranch(branch);
            }
        }

        repository.save(user);
        return mapToUserResponse(user);
    }

    public void deleteUser(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repository.deleteById(id);
    }

    public void changePassword(Integer id, ChangePasswordRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .role(user.getRole())
                .branchId(user.getBranch() != null ? user.getBranch().getId() : null)
                .build();
    }
}

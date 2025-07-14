package com.graduationProject._thYear.Auth.seeder;

import com.graduationProject._thYear.Auth.models.Role;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .username("admin")
                    .password(passwordEncoder.encode("12345678"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println(" Admin user seeded");
        }
    }
}

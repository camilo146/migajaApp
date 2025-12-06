package com.migaja.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.migaja.model.User;
import com.migaja.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            String adminUsername = "admin";
            log.info("Checking for admin user...");

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                log.info("Creating admin user...");
                User admin = User.builder()
                        .username(adminUsername)
                        .email("admin@migaja.com")
                        .password(passwordEncoder.encode("Yz125-:*"))
                        .fullName("Administrador")
                        .role(User.Role.ADMIN)
                        .active(true)
                        .build();
                userRepository.save(admin);
                log.info("Admin user created successfully with password: Yz125-:*");
            } else {
                log.info("Admin user already exists. Updating password to ensure access...");
                User admin = userRepository.findByUsername(adminUsername).get();
                admin.setPassword(passwordEncoder.encode("Yz125-:*"));
                admin.setRole(User.Role.ADMIN); // Ensure role is correct
                admin.setActive(true); // Ensure active
                userRepository.save(admin);
                log.info("Admin user updated successfully with password: Yz125-:*");
            }
        };
    }
}

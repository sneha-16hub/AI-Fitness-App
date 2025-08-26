package com.fitness.userService.repository;

import com.fitness.userService.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    User findByEmail(@NotBlank(message = "Email is blank") @Email(message = "invalid Mail format") String email);

    Boolean existsByKeycloakId(String userId);
}


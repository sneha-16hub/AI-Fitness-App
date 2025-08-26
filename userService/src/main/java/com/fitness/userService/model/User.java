package com.fitness.userService.model;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable=false, unique=true)
    private String email;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private UserRole userRole=UserRole.USER;

}

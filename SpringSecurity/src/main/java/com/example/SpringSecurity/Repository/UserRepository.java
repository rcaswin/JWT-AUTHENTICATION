package com.example.SpringSecurity.Repository;

import com.example.SpringSecurity.Entity.AuthEntity;
import com.example.SpringSecurity.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByEmail(String email);

    long countByRole(Role role);
}

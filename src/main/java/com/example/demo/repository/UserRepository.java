package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    long countByRole(Role role);

    long countByBlocked(boolean blocked);

    // =========================
    // Admin User Management
    // =========================

    List<User> findByRole(Role role);

    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
}

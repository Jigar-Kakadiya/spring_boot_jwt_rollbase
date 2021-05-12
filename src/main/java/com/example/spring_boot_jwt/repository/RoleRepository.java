package com.example.spring_boot_jwt.repository;

import com.example.spring_boot_jwt.model.ERole;
import com.example.spring_boot_jwt.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}


package com.example.spring_boot_jwt.repository;

import com.example.spring_boot_jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String userName);

    Boolean existsUserByEmail(String s);

    User findUsersByIdIs(Long id);
}

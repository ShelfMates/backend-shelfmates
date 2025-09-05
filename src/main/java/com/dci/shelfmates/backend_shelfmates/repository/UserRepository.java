package com.dci.shelfmates.backend_shelfmates.repository;

import com.dci.shelfmates.backend_shelfmates.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email); // check if the mail already exists
}

package com.dci.shelfmates.backend_shelfmates.repository;

import com.dci.shelfmates.backend_shelfmates.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // for finding existing roles
}

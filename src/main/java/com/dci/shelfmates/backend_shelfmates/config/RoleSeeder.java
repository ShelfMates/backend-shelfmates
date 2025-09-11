package com.dci.shelfmates.backend_shelfmates.config;

import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoleSeeder.class);

    // this will have a list of roles and then iterate over that list to see if that role already exists
    // if not the role will be added to the database
    @Override
    public void run(String... args) {
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        for(String roleName : roles) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                return roleRepository.save(role);
            });
        }
        logger.info("--> Default Roles seeded: {}", roles);
    }
}

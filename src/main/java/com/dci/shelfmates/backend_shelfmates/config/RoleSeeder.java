package com.dci.shelfmates.backend_shelfmates.config;

import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    // this will have a list of roles and then iterate over that list to see if that role already exists
    // if not the role will be added to the database
    @Override
    public void run(String... args) {
        List<String> roles = List.of("USER", "ADMIN");

        for(String roleName : roles) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                return roleRepository.save(role);
            });
        }
        // replace this with logging
        System.out.println("Roles seeded: " + roles);
    }
}

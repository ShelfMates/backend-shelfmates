package com.dci.shelfmates.backend_shelfmates.integration;


import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        //empty the database before the test
        userRepository.deleteAll();

        //get roles seed from import.sql
        Role userRole = roleRepository.findByName("ROLE_USER")
                                      .orElseThrow(() -> new RuntimeException("Default role not found"));

        //set up test user
        User newUser = User.builder()
                           .email("test@example.com")
                           .password(passwordEncoder.encode("password123"))
                           .displayName("Test User")
                           .roles(Set.of(userRole))
                           .build();

        userRepository.save(newUser);
    }

    @Test
    void login_withValidCredentials_returns200andCookie() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                    {
                        "email": "test@example.com",
                        "password": "password123"
                    }
                """
                                        ))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));


    }
}

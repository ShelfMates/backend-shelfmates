package com.dci.shelfmates.backend_shelfmates.integration;

import com.dci.shelfmates.backend_shelfmates.exception.UserNotFoundException;
import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import com.dci.shelfmates.backend_shelfmates.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity())
                                 .build();

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
    void meEndpoint_withValidCredentials_returns200() throws Exception {

        // get the user ID for further use
        User user = userRepository.findByEmail("test@example.com")
                                  .orElseThrow(() -> new UserNotFoundException("test@example.com"));
        Long userId = user.getId();

        mockMvc.perform(get("/api/user/me")
                                     .with(SecurityMockMvcRequestPostProcessors.user(
                                             new CustomUserDetails(userId, "test@example.com", "password123",
                                                                   Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                                                                                    )))
                    .andExpect(status().isOk());
    }

    @Test
    void meEndpoint_withBadCredentials_returns401() throws Exception {
        mockMvc.perform(get("/api/user/me"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void getUser_withValidCredentials_returns200() throws Exception {

        User user = userRepository.findByEmail("test@example.com")
                                  .orElseThrow(() -> new UserNotFoundException("test@example.com"));
        Long userId = user.getId();

        mockMvc.perform(get("/api/user/{id}", userId)
                                     .with(SecurityMockMvcRequestPostProcessors.user(
                                             new CustomUserDetails(userId, "test@example.com", "password123",
                                                                   Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                                                                                    )))
                    .andExpect(status().isOk());
    }

    @Test
    void getUser_withBadCredentials_returns401() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 1L))
               .andExpect(status().isUnauthorized());
    }

}

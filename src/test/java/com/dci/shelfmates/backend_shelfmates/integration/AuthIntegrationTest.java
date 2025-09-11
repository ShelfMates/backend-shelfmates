package com.dci.shelfmates.backend_shelfmates.integration;


import com.dci.shelfmates.backend_shelfmates.dto.LoginUserRequest;
import com.dci.shelfmates.backend_shelfmates.dto.RegisterUserRequest;
import com.dci.shelfmates.backend_shelfmates.dto.UpdateUserRequest;
import com.dci.shelfmates.backend_shelfmates.exception.UserNotFoundException;
import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import com.dci.shelfmates.backend_shelfmates.security.CustomUserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @BeforeEach
    void setUp(WebApplicationContext context) {

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
    void login_withValidCredentials_returns200andCookie() throws Exception {

        // uses the dto to emulate a request from the frontend and converts it into json
        LoginUserRequest request = new LoginUserRequest("test@example.com", "password123");
        String json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));


    }

    @Test
    void login_withBadCredentials_returns401() throws Exception {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "badpassword");
        String json = new ObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnauthorized())
                .andReturn();

        // convert the response and turn it into json
        String response = result.getResponse().getContentAsString();

        JsonNode jsonNode = new ObjectMapper().readTree(response);

        // we need to use asText() here and not toString() or otherwise we will get back the quotation marks :D
        assertEquals("Bad credentials", jsonNode.get("error").asText());
    }

    @Test
    void registerNewUser_returns200andCookie() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest("TestRegisterUser",
                                                              "register@test.com",
                                                              "password123");

        String json = new ObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();

        User testUser = userRepository.findByEmail("register@test.com")
                .orElseThrow(() -> new UsernameNotFoundException("register@test.com"));

        assertEquals("TestRegisterUser", testUser.getDisplayName());
        assertEquals("register@test.com", testUser.getEmail());
        // to test the encoded passwords we need to use matches from the encoder
        assertTrue(passwordEncoder.matches("password123", testUser.getPassword()));
        assertNotNull(testUser.getCreatedAt());
        assertNotNull(testUser.getUpdatedAt());
        assertNotNull(testUser.getId());
    }

    @Test
    void updateUser_returns200() throws Exception {

        String updatedName = "ChangedUser";
        String updatedPassword = "NewPassword";

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(updatedPassword, updatedName, "test@example.com");

        String updateJson = new ObjectMapper().writeValueAsString(updateUserRequest);

        // change user details
        this.mockMvc.perform(put("/api/auth/{id}", 1L)
                                     .with(SecurityMockMvcRequestPostProcessors.user(
                                             new CustomUserDetails(1L, "test@example.com", "password123",
                                                                   Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                                                                                    ))
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(updateJson))
                    .andExpect(status().isOk());

        // test login with changed password
        LoginUserRequest request = new LoginUserRequest("test@example.com", updatedPassword);
        String loginJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/auth/login")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(loginJson))
                                       .andExpect(status().isOk())
                                       .andExpect(header().exists("Set-Cookie"));

        // check if the username was changed
        User user = userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException(1L));
        assertEquals(updatedName, user.getDisplayName());

    }

}

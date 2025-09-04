package com.dci.shelfmates.backend_shelfmates.service;

import com.dci.shelfmates.backend_shelfmates.dto.RegisterUserRequest;
import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String register(RegisterUserRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken!");
        }

        Role userRole = roleRepository.findByName("USER")
                                      .orElseThrow(() -> new RuntimeException("Default role not found"));

        User newUser = User.builder()
                .email(request.getEmail())
                .password(encoder.encode((request.getPassword())))
                .displayName(request.getDisplayName())
                .roles(Set.of(userRole))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        // for auto login return JWT
        return jwtService.generateToken(newUser);
    }
}

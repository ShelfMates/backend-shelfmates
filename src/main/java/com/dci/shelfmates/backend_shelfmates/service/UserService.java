package com.dci.shelfmates.backend_shelfmates.service;

import com.dci.shelfmates.backend_shelfmates.dto.LoginUserRequest;
import com.dci.shelfmates.backend_shelfmates.dto.RegisterUserRequest;
import com.dci.shelfmates.backend_shelfmates.model.Role;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import com.dci.shelfmates.backend_shelfmates.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public String register(RegisterUserRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken!");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                                      .orElseThrow(() -> new RuntimeException("Default role not found"));
        // this is just for testing
//        Role admin = roleRepository.findByName("ROLE_ADMIN")
//                                   .orElseThrow(() -> new RuntimeException("Default role not found"));

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

    public String login(LoginUserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),
                                                        request.password()));
        // Sets the authentication in the context - so spring security knows the user
        // this might not be necessary with JWT
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate a token for the authenticated user
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User: " + request.email() + " not found!"));
        return jwtService.generateToken(user);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found!"));
        userRepository.delete(user);
    }
}

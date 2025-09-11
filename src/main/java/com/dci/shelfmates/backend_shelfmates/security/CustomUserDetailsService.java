package com.dci.shelfmates.backend_shelfmates.security;

import com.dci.shelfmates.backend_shelfmates.exception.UserNotFoundException;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // there has to be a better way instead of using mail - this is not desciptive enough
    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email));

        // map the user roles to granted authorities (these are for spring securities internal system)
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }
}

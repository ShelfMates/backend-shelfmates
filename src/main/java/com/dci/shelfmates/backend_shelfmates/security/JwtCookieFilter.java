package com.dci.shelfmates.backend_shelfmates.security;

import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        // this will get the cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
//                    String email = jwtService.extractSubject(token);
//                    User user = userRepository.findById(Long.valueOf(email)).orElse(null);
                    Long userId = Long.valueOf(jwtService.extractSubject(token));
                    User user = userRepository.findById(userId).orElse(null);

                    // this converts a user from the db to something spring security can actually understand :D
                    if (user != null) {
                        // this makes use of the custom user details and fixes a bug where ids were converted to strings
                        CustomUserDetails userDetails = new CustomUserDetails(
                                user.getId(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                                    .collect(Collectors.toSet())
                        );


                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                      userDetails, null, userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

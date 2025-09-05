package com.dci.shelfmates.backend_shelfmates.config;

import com.dci.shelfmates.backend_shelfmates.security.JwtCookieFilter;
import com.dci.shelfmates.backend_shelfmates.security.OAuth2LoginSuccessHandler;
import com.dci.shelfmates.backend_shelfmates.security.Oauth2UserService;
import com.dci.shelfmates.backend_shelfmates.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler successHandler;
    private final JwtCookieFilter jwtCookieFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Oauth2UserService oauth2UserService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/**", "/api/public").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.userInfoEndpoint(
                        userInfo -> userInfo.userService(oauth2UserService)
                        ).successHandler(successHandler))
                // this is for exception handling for bad auths
                .exceptionHandling(exceptions -> exceptions
                                           .authenticationEntryPoint((request, response, authException) -> {
                                               response.setContentType("application/json");
                                               response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                               response.getWriter().write("{\"error\": \"" + authException.getMessage() + "\"}");
                                           })
                                  )
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);  // still works, even if deprecated
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}

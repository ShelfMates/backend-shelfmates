package com.dci.shelfmates.backend_shelfmates.config;

import com.dci.shelfmates.backend_shelfmates.service.Oauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Oauth2UserService oauth2UserService) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/public").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.userInfoEndpoint(
                        userInfo -> userInfo.userService(oauth2UserService)
                        ));

        return http.build();
    }
}

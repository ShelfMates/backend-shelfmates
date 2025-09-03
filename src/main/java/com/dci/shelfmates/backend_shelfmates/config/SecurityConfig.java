package com.dci.shelfmates.backend_shelfmates.config;

import com.dci.shelfmates.backend_shelfmates.security.JwtCookieFilter;
import com.dci.shelfmates.backend_shelfmates.security.OAuth2LoginSuccessHandler;
import com.dci.shelfmates.backend_shelfmates.security.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler successHandler;
    private final JwtCookieFilter jwtCookieFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Oauth2UserService oauth2UserService) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/public").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.userInfoEndpoint(
                        userInfo -> userInfo.userService(oauth2UserService)
                        ).successHandler(successHandler))
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}

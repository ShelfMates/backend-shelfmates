package com.dci.shelfmates.backend_shelfmates.security;

import com.dci.shelfmates.backend_shelfmates.exception.UserNotFoundException;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // this may need to be customized to allow for different auth providers
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // extract email from the user
        String email = oAuth2User.getAttribute("email");

        // load user entity from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // build a token
        String token = jwtService.generateToken(user);

        // this will set a secure cookie that can only be seen by the backend for security reasons
        Cookie cookie = new Cookie("jwt", token);

        cookie.setHttpOnly(true); // this is for JS access
        cookie.setSecure(true); // sends over https
        cookie.setPath("/"); // makes it available for the whole domain
        cookie.setMaxAge(24 * 60 * 60); // Age of 1 day

        //cookie.setAttribute("SameSite", "Strict"); // Spring Boot 3+ supports this
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000/");
    }
}

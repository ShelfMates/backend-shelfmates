package com.dci.shelfmates.backend_shelfmates.controller;

import com.dci.shelfmates.backend_shelfmates.dto.RegisterUserRequest;
import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // generates a constructor for all required arguments
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
        String token = userService.register(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("User " + request.getDisplayName() + " registered successfully!");
    }

    // this is just a test for a "me" endpoint that will later be used in the frontend
    @GetMapping("/meme")
    @ResponseBody
    public String getMe(Authentication authentication) {
        if(authentication == null) {
            return "Not authenticated!";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            return "Not authenticated";
        }

        return "Hello " + user.getDisplayName() +"! " + "Your email is: " + user.getEmail();
    }



}

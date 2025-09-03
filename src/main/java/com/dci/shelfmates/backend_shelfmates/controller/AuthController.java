package com.dci.shelfmates.backend_shelfmates.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor // generates a constructor for all required arguments
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
}

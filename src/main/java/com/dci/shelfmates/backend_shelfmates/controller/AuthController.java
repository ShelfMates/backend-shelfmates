package com.dci.shelfmates.backend_shelfmates.controller;

import com.dci.shelfmates.backend_shelfmates.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // generates a constructor for all required arguments
public class AuthController {

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

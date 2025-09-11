package com.dci.shelfmates.backend_shelfmates.controller;

import com.dci.shelfmates.backend_shelfmates.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // this is just a test for a "me" endpoint that will later be used in the frontend
    @GetMapping("/me")
    @ResponseBody
    public String getMe(Authentication authentication) {

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails userDetails)) {
            System.out.println("Principal: " + principal.toString());
            return "Say what?";
        }



        return "Hello " + userDetails.getUsername() +"! " + "Your email is: " + userDetails.getUsername();
    }
}

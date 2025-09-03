package com.dci.shelfmates.backend_shelfmates.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/public")
    public String publicApi(){ return "Welcome to the public API";}

//    @GetMapping("/private")
//    public String privateApi(OAuth2AuthenticationToken token) {
//        System.out.println(token.getPrincipal().getAttributes());
//        return "You are authenticated! " + token.getPrincipal().getAttribute("email");
//    }

}

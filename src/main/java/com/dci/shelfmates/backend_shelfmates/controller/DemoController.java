package com.dci.shelfmates.backend_shelfmates.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/public")
    public String publicApi(){ return "Welcome to the public API";}

    @GetMapping("/private")
    public String privateApi() {return "You are authenticated!";}

}

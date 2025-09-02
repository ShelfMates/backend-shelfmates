package com.dci.shelfmates.backend_shelfmates;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class BackendShelfmatesApplication {


    public static void main(String[] args) {
//        // Start Spring context once
//        ApplicationContext context = SpringApplication.run(BackendShelfmatesApplication.class, args);
//
//        // Access environment
//        Environment env = context.getEnvironment();
//        String googleClientSecret = env.getProperty("spring.security.oauth2.client.registration.google.client-secret");
//
//        System.out.println("Google Client Secret: " + googleClientSecret); // Only for local debugging

		SpringApplication.run(BackendShelfmatesApplication.class, args);
	}

}

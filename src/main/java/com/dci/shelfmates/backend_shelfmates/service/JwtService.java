package com.dci.shelfmates.backend_shelfmates.service;

import com.dci.shelfmates.backend_shelfmates.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    // this needs to be an env variable
    private String secretKey = "superverysecretkey";

    public JwtService() {

        // this will generate a secret key
        // also: generating a key in the constructor will invalidate old tokens after a restart
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



    public String generateToken(User user) {
        // to add more claims, just put them in the hashmap
        Map<String, Object> claims = new HashMap<>();
        // like this 
        claims.put("roles", user.getRoles().stream()
                .map(role -> role.getName()).toList());

        return Jwts.builder().claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 30))
                .and().signWith(getKey()).compact();
    }

    private SecretKey getKey() {
        byte[] keyBites = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBites);
    }
}

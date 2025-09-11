package com.dci.shelfmates.backend_shelfmates.security;

import com.dci.shelfmates.backend_shelfmates.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // this needs to be an env variable
    @Value("${jwt.secret}")
    private String secretKey;


    public String generateToken(User user) {
        // to add more claims, just put them in the hashmap
        Map<String, Object> claims = new HashMap<>();
        // like this with roles
        claims.put("roles", user.getRoles().stream()
                .map(role -> role.getName()).toList());
        // or the email
        claims.put("email", user.getEmail());

        return Jwts.builder().claims().add(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 30))
                .and().signWith(getKey()).compact();
    }

    private SecretKey getKey() {
        byte[] keyBites = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBites);
    }

    public String extractSubject(String token) {
        // extract the id from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        // this will fetch an extract the claims
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }
}

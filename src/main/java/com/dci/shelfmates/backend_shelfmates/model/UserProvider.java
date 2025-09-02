package com.dci.shelfmates.backend_shelfmates.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_providers")
@Builder
public class UserProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provider; // for providers like github or google

    @Column(nullable = false)
    private String providerUserId; // id from the provider

    private String providerEmail;
    private String accessToken;
    private String refreshToken;

    // many to one - this means that many providers like google or github can relate to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

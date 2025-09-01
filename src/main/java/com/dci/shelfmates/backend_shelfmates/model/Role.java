package com.dci.shelfmates.backend_shelfmates.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // this is for the roles like ADMIN etc.

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}

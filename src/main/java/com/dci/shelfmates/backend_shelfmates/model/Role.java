package com.dci.shelfmates.backend_shelfmates.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // this needs to be enabled for lazy loading
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // this is for the roles like ADMIN etc.

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}

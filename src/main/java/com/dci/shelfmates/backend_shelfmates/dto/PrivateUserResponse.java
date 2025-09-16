package com.dci.shelfmates.backend_shelfmates.dto;

import com.dci.shelfmates.backend_shelfmates.model.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record PrivateUserResponse(
    Long id,
    String displayName,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Set<String> roles
) {}

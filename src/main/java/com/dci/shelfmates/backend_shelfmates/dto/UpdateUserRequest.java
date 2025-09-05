package com.dci.shelfmates.backend_shelfmates.dto;

public record UpdateUserRequest(
        String password,
        String displayName,
        String email
) {}

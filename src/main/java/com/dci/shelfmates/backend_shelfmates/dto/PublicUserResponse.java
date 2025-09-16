package com.dci.shelfmates.backend_shelfmates.dto;

import java.time.LocalDateTime;

// this will be extended in the future
public record PublicUserResponse(
        String displayName,
        LocalDateTime createdAt
) {}

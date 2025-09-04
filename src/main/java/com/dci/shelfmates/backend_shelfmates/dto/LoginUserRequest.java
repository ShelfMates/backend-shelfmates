package com.dci.shelfmates.backend_shelfmates.dto;

public record LoginUserRequest(
  String email,
  String password,
  String displayName
){}
package com.dci.shelfmates.backend_shelfmates.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequest {
    @NotBlank(message = "Name required")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    //this is how you would integrate regex matchers to the validation :D
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String displayName;
    @NotBlank(message = "E-Mail required")
    @Email(message = "E-Mail must be valid")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;
}
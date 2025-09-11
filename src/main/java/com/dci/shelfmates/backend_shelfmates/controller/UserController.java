package com.dci.shelfmates.backend_shelfmates.controller;

import com.dci.shelfmates.backend_shelfmates.dto.PrivateUserResponse;
import com.dci.shelfmates.backend_shelfmates.dto.PublicUserResponse;
import com.dci.shelfmates.backend_shelfmates.security.CustomUserDetails;
import com.dci.shelfmates.backend_shelfmates.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @ResponseBody
    public ResponseEntity<PrivateUserResponse> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        PrivateUserResponse response = userService.getMe(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PublicUserResponse> getMe(@PathVariable Long id) {
        PublicUserResponse response = userService.getPublicUser(id);

        return ResponseEntity.ok(response);
    }
}

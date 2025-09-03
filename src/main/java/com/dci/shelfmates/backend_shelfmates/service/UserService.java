package com.dci.shelfmates.backend_shelfmates.service;

import com.dci.shelfmates.backend_shelfmates.repository.RoleRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
}

package com.dci.shelfmates.backend_shelfmates.repository;

import com.dci.shelfmates.backend_shelfmates.model.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {
    Optional<UserProvider> findByProviderAndProviderUserId(String provider, String providerUserId);
    Optional<UserProvider> findByProviderEmail(String providerEmail);
}

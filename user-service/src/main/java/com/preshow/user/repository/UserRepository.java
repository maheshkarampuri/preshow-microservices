package com.preshow.user.repository;

import com.preshow.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, String> {

    Optional<UserProfile> findByEmail(String email);

    boolean existsByEmail(String email);
}

package com.graduationProject._thYear.Auth.repositories;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByGlobalId(UUID globalId);

}

package com.exadel.pedrolima.userservice.repository;


import com.exadel.pedrolima.userservice.entity.User;
import com.exadel.pedrolima.userservice.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long id);

    List<User> findByRole(UserRole role);


}

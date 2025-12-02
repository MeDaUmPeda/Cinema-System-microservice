package com.exadel.pedrolima.sessionservice.repository;

import com.exadel.pedrolima.sessionservice.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Override
    Optional<Session> findById(Long id);

    List<Session> findByDateTime(LocalDateTime dateTime);
}

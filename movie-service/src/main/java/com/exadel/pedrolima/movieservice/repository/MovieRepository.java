package com.exadel.pedrolima.movieservice.repository;

import com.exadel.pedrolima.movieservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Override
    Optional<Movie> findById(Long id);

    Optional<Movie> findByTitle(String title);

    List<Movie> findAllByGenre(String genre);

    List<Movie> findByDurationGreaterThan(Integer duration);
}

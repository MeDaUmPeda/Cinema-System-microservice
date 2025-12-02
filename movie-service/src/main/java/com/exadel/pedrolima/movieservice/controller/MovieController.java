package com.exadel.pedrolima.movieservice.controller;

import com.exadel.pedrolima.movieservice.dto.CreateMovieRequest;
import com.exadel.pedrolima.movieservice.dto.MovieResponse;
import com.exadel.pedrolima.movieservice.entity.Movie;
import com.exadel.pedrolima.movieservice.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@RequestBody CreateMovieRequest request) {
        MovieResponse created = movieService.createMovie(request);
        return ResponseEntity.created(URI.create("api/movies/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @RequestBody CreateMovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}

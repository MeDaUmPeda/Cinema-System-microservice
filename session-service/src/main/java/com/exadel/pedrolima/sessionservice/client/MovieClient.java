package com.exadel.pedrolima.sessionservice.client;

import com.exadel.pedrolima.sessionservice.dto.MovieResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-service", url = "http://movie-service:8084")
public interface MovieClient {

    @GetMapping("/api/movies/{id}")
    MovieResponse getMovieById(@PathVariable("id") Long id);
}

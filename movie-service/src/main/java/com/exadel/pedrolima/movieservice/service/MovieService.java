package com.exadel.pedrolima.movieservice.service;

import com.exadel.pedrolima.movieservice.dto.CreateMovieRequest;
import com.exadel.pedrolima.movieservice.dto.MovieResponse;
import com.exadel.pedrolima.movieservice.entity.Movie;
import com.exadel.pedrolima.movieservice.exception.BadRequestException;
import com.exadel.pedrolima.movieservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.movieservice.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private MovieResponse convertToDto(Movie movie){
        return new MovieResponse(movie.getId(), movie.getTitle(), movie.getDuration(), movie.getGenre());
    }

    public List<MovieResponse> getAllMovies(){
        return movieRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MovieResponse getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + id + " not found."));
        return convertToDto(movie);
    }

    public MovieResponse createMovie(CreateMovieRequest request){
        movieRepository.findByTitle(request.getTitle())
                .ifPresent(m -> {throw new BadRequestException("Movie with title " + request.getTitle() + " already exists.");});

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .duration(request.getDuration())
                .genre(request.getGenre())
                .build();

        return convertToDto(movieRepository.save(movie));
    }

    public MovieResponse updateMovie(Long id, CreateMovieRequest request){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + id + " not found."));

        movie.setTitle(request.getTitle());
        movie.setDuration(request.getDuration());
        movie.setGenre(request.getGenre());

        return convertToDto(movieRepository.save(movie));
    }

    public void deleteMovie(Long id){
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie with id " + id + " not found.");
        }
        movieRepository.deleteById(id);
    }
}

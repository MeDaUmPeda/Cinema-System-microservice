package com.exadel.pedrolima.movieservice.service;

import com.exadel.pedrolima.movieservice.dto.CreateMovieRequest;
import com.exadel.pedrolima.movieservice.dto.MovieResponse;
import com.exadel.pedrolima.movieservice.entity.Movie;
import com.exadel.pedrolima.movieservice.exception.BadRequestException;
import com.exadel.pedrolima.movieservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.movieservice.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMovies() {
        Movie movie = new Movie(1L, "Matrix", 136, "Sci-fi");
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieResponse> movies = movieService.getAllMovies();

        assertEquals(1, movies.size());
        assertEquals("Matrix", movies.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById_Success() {
        Movie movie = new Movie(1L, "Matrix", 136, "Sci-fi");
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieResponse response = movieService.getMovieById(1L);

        assertNotNull(response);
        assertEquals("Matrix", response.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieById(1L));
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateMovie_Success() {
        CreateMovieRequest request = new CreateMovieRequest("Matrix", 136, "Sci-fi");
        Movie movie = new Movie(1L, "Matrix", 136, "Sci-fi");

        when(movieRepository.findByTitle("Matrix")).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieResponse response = movieService.createMovie(request);

        assertNotNull(response);
        assertEquals("Matrix", response.getTitle());
        verify(movieRepository, times(1)).findByTitle("Matrix");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testCreateMovie_AlreadyExists() {
        CreateMovieRequest request = new CreateMovieRequest("Matrix", 136, "Sci-fi");
        when(movieRepository.findByTitle("Matrix")).thenReturn(Optional.of(new Movie()));

        assertThrows(BadRequestException.class, () -> movieService.createMovie(request));
        verify(movieRepository, times(1)).findByTitle("Matrix");
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testUpdateMovie_Success() {
        CreateMovieRequest request = new CreateMovieRequest("Matrix Reloaded", 138, "Sci-fi");
        Movie existingMovie = new Movie(1L, "Matrix", 136, "Sci-fi");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(existingMovie);

        MovieResponse response = movieService.updateMovie(1L, request);

        assertNotNull(response);
        assertEquals("Matrix Reloaded", response.getTitle());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void testUpdateMovie_NotFound() {
        CreateMovieRequest request = new CreateMovieRequest("Matrix Reloaded", 138, "Sci-fi");
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.updateMovie(1L, request));
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testDeleteMovie_Success() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(1L);

        assertDoesNotThrow(() -> movieService.deleteMovie(1L));
        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMovie_NotFound() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> movieService.deleteMovie(1L));
        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, never()).deleteById(1L);
    }
}

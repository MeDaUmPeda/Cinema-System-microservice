package com.exadel.pedrolima.movieservice.controller;

import com.exadel.pedrolima.movieservice.dto.CreateMovieRequest;
import com.exadel.pedrolima.movieservice.dto.MovieResponse;
import com.exadel.pedrolima.movieservice.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllMovies() throws Exception {
        when(movieService.getAllMovies())
                .thenReturn(List.of(new MovieResponse(1L, "Matrix", 136, "Sci-fi")));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Matrix"))
                .andExpect(jsonPath("$[0].genre").value("Sci-fi"));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void testGetMovieById() throws Exception {
        when(movieService.getMovieById(1L))
                .thenReturn(new MovieResponse(1L, "Matrix", 136, "Sci-fi"));

        mockMvc.perform(get("/api/movies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Matrix"))
                .andExpect(jsonPath("$.duration").value(136))
                .andExpect(jsonPath("$.genre").value("Sci-fi"));

        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void testCreateMovie() throws Exception {
        CreateMovieRequest request = new CreateMovieRequest("Matrix", 136, "Sci-fi");
        MovieResponse movieResponse = new MovieResponse(1L, "Matrix", 136, "Sci-fi");

        when(movieService.createMovie(any(CreateMovieRequest.class))).thenReturn(movieResponse);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Matrix"))
                .andExpect(jsonPath("$.genre").value("Sci-fi"));

        verify(movieService, times(1)).createMovie(any(CreateMovieRequest.class));
    }

    @Test
    void testUpdateMovie() throws Exception {
        CreateMovieRequest request = new CreateMovieRequest("Matrix Reloaded", 138, "Sci-fi");
        MovieResponse response = new MovieResponse(1L, "Matrix Reloaded", 138, "Sci-fi");

        when(movieService.updateMovie(eq(1L), any(CreateMovieRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/movies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Matrix Reloaded"))
                .andExpect(jsonPath("$.duration").value(138));

        verify(movieService, times(1)).updateMovie(eq(1L), any(CreateMovieRequest.class));
    }

    @Test
    void testDeleteMovie() throws Exception {
        doNothing().when(movieService).deleteMovie(1L);

        mockMvc.perform(delete("/api/movies/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(movieService, times(1)).deleteMovie(1L);
    }
}

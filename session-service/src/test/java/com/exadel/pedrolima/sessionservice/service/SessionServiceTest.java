package com.exadel.pedrolima.sessionservice.service;

import com.exadel.pedrolima.sessionservice.client.MovieClient;
import com.exadel.pedrolima.sessionservice.dto.CreateSessionRequest;
import com.exadel.pedrolima.sessionservice.dto.MovieResponse;
import com.exadel.pedrolima.sessionservice.dto.SessionResponse;
import com.exadel.pedrolima.sessionservice.entity.Session;
import com.exadel.pedrolima.sessionservice.exception.BadRequestException;
import com.exadel.pedrolima.sessionservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.sessionservice.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private MovieClient movieClient;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private LocalDateTime now;
    private MovieResponse movieResponse;

    @BeforeEach
    void setup() {
        now = LocalDateTime.now();

        session = new Session();
        session.setId(1L);
        session.setMovieId(1L);
        session.setAvailableSeats(30);

        movieResponse = new MovieResponse(1L, "Matrix", "Sci-Fi", 120);
    }

    @Test
    void getAllSessionsShouldReturnList() {
        when(sessionRepository.findAll()).thenReturn(List.of(session));
        when(movieClient.getMovieById(1L)).thenReturn(movieResponse);

        List<SessionResponse> result = sessionService.getAllSessions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matrix", result.get(0).getMovieTitle());

        verify(sessionRepository, times(1)).findAll();
        verify(movieClient, times(1)).getMovieById(1L);
    }

    @Test
    void getSessionByIdShouldReturnSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(movieClient.getMovieById(1L)).thenReturn(movieResponse);

        SessionResponse result = sessionService.getSessionById(1L);

        assertNotNull(result);
        assertEquals("Matrix", result.getMovieTitle());

        verify(sessionRepository, times(1)).findById(1L);
        verify(movieClient, times(1)).getMovieById(1L);
    }

    @Test
    void getSessionByIdShouldThrowWhenNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sessionService.getSessionById(99L));

        verify(sessionRepository, times(1)).findById(99L);
        verify(movieClient, never()).getMovieById(any());
    }

    @Test
    void createSessionShouldCreateSuccessfully() {
        CreateSessionRequest request = new CreateSessionRequest(now, 30, 1L);

        when(movieClient.getMovieById(1L)).thenReturn(movieResponse);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        SessionResponse result = sessionService.createSession(request);

        assertNotNull(result);
        assertEquals("Matrix", result.getMovieTitle());
        assertEquals(30, result.getAvailableSeats());

        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(movieClient, times(1)).getMovieById(1L);
    }

    @Test
    void createSessionShouldThrowIfSeatsInvalid() {
        CreateSessionRequest request = new CreateSessionRequest(now, 0, 1L);

        when(movieClient.getMovieById(1L)).thenReturn(movieResponse);

        assertThrows(BadRequestException.class, () -> sessionService.createSession(request));

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void updateSessionShouldUpdateSuccessfully() {
        CreateSessionRequest request = new CreateSessionRequest(now.plusDays(1), 50, 1L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(movieClient.getMovieById(1L)).thenReturn(movieResponse);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        SessionResponse result = sessionService.updateSession(1L, request);

        assertNotNull(result);
        assertEquals("Matrix", result.getMovieTitle());
        assertEquals(50, result.getAvailableSeats());

        verify(sessionRepository, times(1)).save(any(Session.class));
    }
}

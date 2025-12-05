package com.exadel.pedrolima.sessionservice.service;

import com.exadel.pedrolima.sessionservice.client.MovieClient;
import com.exadel.pedrolima.sessionservice.dto.CreateSessionRequest;
import com.exadel.pedrolima.sessionservice.dto.MovieResponse;
import com.exadel.pedrolima.sessionservice.dto.SessionResponse;
import com.exadel.pedrolima.sessionservice.entity.Session;
import com.exadel.pedrolima.sessionservice.exception.BadRequestException;
import com.exadel.pedrolima.sessionservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.sessionservice.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MovieClient movieClient;

    public SessionService(SessionRepository sessionRepository, MovieClient movieClient) {
        this.sessionRepository = sessionRepository;
        this.movieClient = movieClient;
    }

    private SessionResponse convertToDto(Session session, String movieTitle) {
        return new SessionResponse(
                session.getId(),
                session.getDateTime(),
                session.getAvailableSeats(),
                movieTitle
        );
    }

    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll()
                .stream()
                .map(session -> {
                    MovieResponse movie = movieClient.getMovieById(session.getMovieId());
                    return convertToDto(session, movie != null ? movie.getTitle() : null);
                })
                .collect(Collectors.toList());
    }

    public SessionResponse getSessionById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session with id " + id + " not found"));
        MovieResponse movie = movieClient.getMovieById(session.getMovieId());
        return convertToDto(session, movie != null ? movie.getTitle() : null);
    }

    public SessionResponse createSession(CreateSessionRequest request) {
        if (request.getAvailableSeats() <= 0) {
            throw new BadRequestException("Available seats must be greater than zero");
        }

        MovieResponse movie = movieClient.getMovieById(request.getMovieId());
        if (movie == null) {
            throw new ResourceNotFoundException("Movie not found with id " + request.getMovieId());
        }

        Session session = Session.builder()
                .dateTime(request.getDateTime())
                .availableSeats(request.getAvailableSeats())
                .movieId(request.getMovieId())
                .build();

        Session saved = sessionRepository.save(session);
        return convertToDto(saved, movie.getTitle());
    }

    public SessionResponse updateSession(Long id, CreateSessionRequest request) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session with id " + id + " not found"));

        MovieResponse movie = movieClient.getMovieById(request.getMovieId());
        if (movie == null) {
            throw new ResourceNotFoundException("Movie not found with id " + request.getMovieId());
        }

        session.setDateTime(request.getDateTime());
        session.setAvailableSeats(request.getAvailableSeats());
        session.setMovieId(request.getMovieId());

        Session updated = sessionRepository.save(session);
        return convertToDto(updated, movie.getTitle());
    }

    public SessionResponse decrementIfAvailable(Long id) {

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id " + id));

        if (session.getAvailableSeats() <= 0) {
            throw new BadRequestException("Available seats must be greater than zero for session id " + id);
        }

        session.setAvailableSeats(session.getAvailableSeats() - 1);

        Session updated = sessionRepository.save(session);

        MovieResponse movie = movieClient.getMovieById(updated.getMovieId());

        String movieTitle = movie != null ? movie.getTitle() : null;

        return convertToDto(updated, movieTitle);
    }

    public void deleteSessionById(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Session with id " + id + " not found");
        }
        sessionRepository.deleteById(id);
    }
}

package com.exadel.pedrolima.sessionservice.controller;

import com.exadel.pedrolima.sessionservice.dto.CreateSessionRequest;
import com.exadel.pedrolima.sessionservice.dto.SessionResponse;
import com.exadel.pedrolima.sessionservice.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@RequestBody CreateSessionRequest request) {
        SessionResponse created = sessionService.createSession(request);
        return ResponseEntity.created(URI.create("/api/sessions/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}/decrement-seat")
    public ResponseEntity<SessionResponse> decrementSeatCount(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.decrementIfAvailable(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SessionResponse> updateSession(@PathVariable Long id, @RequestBody CreateSessionRequest request) {
        return ResponseEntity.ok(sessionService.updateSession(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSessionById(id);
        return ResponseEntity.noContent().build();
    }
}

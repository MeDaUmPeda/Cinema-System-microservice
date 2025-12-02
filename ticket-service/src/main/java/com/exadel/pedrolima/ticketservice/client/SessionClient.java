package com.exadel.pedrolima.ticketservice.client;

import com.exadel.pedrolima.ticketservice.dto.SessionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "session-service", url = "http://session-service:8083")
public interface SessionClient {

    @GetMapping("/api/sessions/{id}")
    SessionResponse getSessionById(@PathVariable("id") Long id);


    @PutMapping("/api/sessions/{id}/decrement-seat")
    SessionResponse decrementSeatCount(@PathVariable("id") Long id);
}

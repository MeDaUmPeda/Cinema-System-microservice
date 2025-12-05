package com.exadel.pedrolima.userservice.controller;

import com.exadel.pedrolima.userservice.dto.CreateUserRequest;
import com.exadel.pedrolima.userservice.dto.UserResponse;
import com.exadel.pedrolima.userservice.entity.enums.UserRole;
import com.exadel.pedrolima.userservice.event.TicketReservationEvent;
import com.exadel.pedrolima.userservice.kafka.TicketReservationProducer;
import com.exadel.pedrolima.userservice.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TicketReservationProducer ticketReservationProducer;

    public UserController(UserService userService,
                          TicketReservationProducer ticketReservationProducer) {
        this.userService = userService;
        this.ticketReservationProducer = ticketReservationProducer;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable UserRole role) {
        return ResponseEntity.ok(userService.getUserByRole(role));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        UserResponse createdUser = userService.createUser(request);

        URI location = URI.create("/api/users/" + createdUser.getId());

        return ResponseEntity.created(location).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{userId}/reserve")
    public ResponseEntity<String> reserveTicket(
            @PathVariable Long userId,
            @RequestParam Long sessionId,
            @RequestParam String seatNumber) {

        TicketReservationEvent event =
                new TicketReservationEvent(userId, sessionId, seatNumber);

        ticketReservationProducer.sendTicketReservation(event);

        return ResponseEntity.ok("Reservation request sent for processing");
    }
}

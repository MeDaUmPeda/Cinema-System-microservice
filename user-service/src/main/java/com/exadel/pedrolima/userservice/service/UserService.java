package com.exadel.pedrolima.userservice.service;

import com.exadel.pedrolima.userservice.client.TicketClient;
import com.exadel.pedrolima.userservice.dto.CreateUserRequest;
import com.exadel.pedrolima.userservice.dto.TicketResponse;
import com.exadel.pedrolima.userservice.dto.UserResponse;
import com.exadel.pedrolima.userservice.dto.UserWithTicketsResponse;
import com.exadel.pedrolima.userservice.entity.User;
import com.exadel.pedrolima.userservice.entity.enums.UserRole;
import com.exadel.pedrolima.userservice.exception.BadRequestException;
import com.exadel.pedrolima.userservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.userservice.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TicketClient ticketClient;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       TicketClient ticketClient,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ticketClient = ticketClient;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponse convertToDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException("We can't find an user with id: " + id)
                );
    }

    public List<UserResponse> getUserByRole(UserRole role) {
        List<User> users = userRepository.findByRole(role);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("We can't find any user with role: " + role);
        }

        return users.stream().map(this::convertToDto).toList();
    }

    public UserResponse createUser(CreateUserRequest request) {


        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("Name is required");
        }
        if (request.getUserRole() == null) {
            throw new BadRequestException("The role of the user is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getUserRole());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ONLY password encrypted

        return convertToDto(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, CreateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("We can't find user with id: " + id));

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("Name is required");
        }
        if (request.getUserRole() == null) {
            throw new BadRequestException("The role of the user is required");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getUserRole());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return convertToDto(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("We can't find an user with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserWithTicketsResponse getUserWithTickets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("We can't find user with id: " + userId));

        List<TicketResponse> tickets = ticketClient.getTicketByUser(userId);

        return new UserWithTicketsResponse(user, tickets);
    }
}

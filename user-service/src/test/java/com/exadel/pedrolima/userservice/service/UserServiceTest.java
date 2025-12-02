package com.exadel.pedrolima.userservice.service;

import com.exadel.pedrolima.userservice.dto.CreateUserRequest;
import com.exadel.pedrolima.userservice.dto.UserResponse;
import com.exadel.pedrolima.userservice.entity.User;
import com.exadel.pedrolima.userservice.entity.enums.UserRole;
import com.exadel.pedrolima.userservice.exception.BadRequestException;
import com.exadel.pedrolima.userservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.userservice.repository.UserRepository;
import com.exadel.pedrolima.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "user", "admin@email.com", "123456", UserRole.ADMIN);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertEquals("Admin", response.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void testGetUserByRoleSuccess() {
        when(userRepository.findByRole(UserRole.ADMIN)).thenReturn(List.of(user));

        List<UserResponse> result = userService.getUserByRole(UserRole.ADMIN);

        assertEquals(1, result.size());
        verify(userRepository).findByRole(UserRole.ADMIN);
    }

    @Test
    void testGetUserByRoleEmpty() {
        when(userRepository.findByRole(UserRole.CUSTOMER)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByRole(UserRole.CUSTOMER));
    }

    @Test
    void testCreateUserSuccess() {
        CreateUserRequest request = new CreateUserRequest("Admin", "admin@email.com", UserRole.ADMIN, "123456");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertEquals("Admin", response.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserMissingEmail() {
        CreateUserRequest request = new CreateUserRequest("User", "", UserRole.CUSTOMER, "123456");
        assertThrows(BadRequestException.class, () -> userService.createUser(request));
    }

    @Test
    void testUpdateUserSuccess() {
        CreateUserRequest request = new CreateUserRequest("Updated", "new@email.com", UserRole.ADMIN, "1233456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, request);

        assertEquals("Updated", response.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        CreateUserRequest request = new CreateUserRequest("User", "user@email.com", UserRole.ADMIN, "123456");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, request));
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(99L));
    }
}
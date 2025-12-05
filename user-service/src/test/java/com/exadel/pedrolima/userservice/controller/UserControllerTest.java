package com.exadel.pedrolima.userservice.controller;

import com.exadel.pedrolima.userservice.dto.CreateUserRequest;
import com.exadel.pedrolima.userservice.dto.UserResponse;
import com.exadel.pedrolima.userservice.entity.enums.UserRole;
import com.exadel.pedrolima.userservice.exception.ResourceNotFoundException;
import com.exadel.pedrolima.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(
                List.of(new UserResponse(1L, "Admin", "admin@email.com", UserRole.ADMIN))
        );

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("admin"));

    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L))
                .thenReturn(new UserResponse(1L, "Admin", "admin@email.com", UserRole.ADMIN));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("admin"));
    }

    @Test
    void testGetUserByRole() throws Exception {
        when(userService.getUserByRole(UserRole.ADMIN))
                .thenReturn(List.of(new UserResponse(1L, "Admin", "admin@email.com", UserRole.ADMIN)));

        mockMvc.perform(get("/api/users/role/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("New User", "new@email.com", UserRole.CUSTOMER, "123456");
        UserResponse response = new UserResponse(1L, "New User", "new@email.com", UserRole.CUSTOMER);

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void testUpdateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Updated", "updated@email.com", UserRole.CUSTOMER, "123456");
        UserResponse response = new UserResponse(1L, "Updated", "updates@email.com", UserRole.CUSTOMER);

        when(userService.updateUser(any(Long.class), any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated"));

    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetUsersByIdNotFound() throws Exception {
        when(userService.getUserById(99L))
                .thenThrow(new ResourceNotFoundException("We can't find an user with id: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUserBadRequest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "invalid@email.com", UserRole.CUSTOMER, "123456");

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new BadRequestException("Name is required"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        CreateUserRequest request = new CreateUserRequest("user", "user@email.com", UserRole.CUSTOMER, "123456");

        when(userService.updateUser(any(Long.class), any(CreateUserRequest.class)))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(put("/api/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("We can't find an user with id: 10"))
                .when(userService).deleteUserById(10L);

        mockMvc.perform(delete("/api/users/10"))
                .andExpect(status().isNotFound());
    }
}
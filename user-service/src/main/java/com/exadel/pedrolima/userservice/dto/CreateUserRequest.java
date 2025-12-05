package com.exadel.pedrolima.userservice.dto;

import com.exadel.pedrolima.userservice.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String name;
    private String email;
    private UserRole userRole;
    private String password;
}

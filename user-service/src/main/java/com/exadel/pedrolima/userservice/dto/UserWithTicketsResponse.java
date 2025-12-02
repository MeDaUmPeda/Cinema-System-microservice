package com.exadel.pedrolima.userservice.dto;

import com.exadel.pedrolima.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithTicketsResponse {

    private User user;
    private List<TicketResponse> tickets;

}

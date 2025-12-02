package com.exadel.pedrolima.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private String seatNumber;
    private String status;

}

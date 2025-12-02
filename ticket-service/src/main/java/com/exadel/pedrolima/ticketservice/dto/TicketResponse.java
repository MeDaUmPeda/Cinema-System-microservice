package com.exadel.pedrolima.ticketservice.dto;

import com.exadel.pedrolima.ticketservice.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {

    private Long id;
    private String seatNumber;
    private TicketStatus status;
    private Long userId;
    private Long sessionId;

}

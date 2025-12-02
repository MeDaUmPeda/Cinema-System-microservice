package com.exadel.pedrolima.ticketservice.dto;

import com.exadel.pedrolima.ticketservice.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequest {
    private String seatNumber;
    private Long userId;
    private Long sessionId;
    private TicketStatus  status;

}

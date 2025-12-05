package com.exadel.pedrolima.ticketservice.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketConfirmationEvent {

    private Long userId;
    private Long sessionId;
    private String seatNumber;
    private boolean confirmed;
    private String message;
}

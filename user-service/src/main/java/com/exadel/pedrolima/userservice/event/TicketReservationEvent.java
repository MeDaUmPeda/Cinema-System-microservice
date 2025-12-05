package com.exadel.pedrolima.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReservationEvent implements Serializable {
    private Long userId;
    private Long sessionId;
    private String seatNumber;
}

package com.exadel.pedrolima.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketConfirmationEvent implements Serializable {

    private Long userId;
    private Long sessionId;
    private String seatNumber;
    private boolean confirmed;
    private String message;
}

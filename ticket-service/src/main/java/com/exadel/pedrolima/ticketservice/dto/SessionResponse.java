package com.exadel.pedrolima.ticketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {

    private Long id;
    private LocalDateTime dateTime;
    private Integer availableSeats;
    private Long movieId;
    private String movieTitle;
}

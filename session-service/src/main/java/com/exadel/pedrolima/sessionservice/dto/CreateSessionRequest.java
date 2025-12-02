package com.exadel.pedrolima.sessionservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionRequest {
    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private Integer availableSeats;

    @NotNull
    private Long movieId;

}

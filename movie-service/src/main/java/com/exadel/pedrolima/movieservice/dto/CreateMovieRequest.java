package com.exadel.pedrolima.movieservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovieRequest {

    @NotBlank
    private String title;

    @NotNull
    @Min(1)
    private Integer duration;

    @NotBlank
    private String genre;

}

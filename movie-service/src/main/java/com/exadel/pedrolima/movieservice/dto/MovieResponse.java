package com.exadel.pedrolima.movieservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

    private Long id;
    private String title;
    private Integer duration;
    private String genre;

}

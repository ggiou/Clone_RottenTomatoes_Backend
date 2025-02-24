package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.component.dto.ResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieInfoDto {
    private MovieDto movieDto;
    private MovieDetailDto movieDetailDto;
    private List<MovieTrailerDto> movieTrailerDtoList = new ArrayList<>();
}

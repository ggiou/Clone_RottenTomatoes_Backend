package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.constant.MovieFindType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieFindResponse {
    private String findValue;   // 검색 값
    private List<MovieDto> movieDtos;   // 영화 정보

    public static MovieFindResponse of(String findValue, List<MovieDto> movieDtos){
        return new MovieFindResponse(findValue, movieDtos);
    }
}

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
    private int totalCnt = 0;   // 전체 데이터 개수
    private List<MovieDto> movieDtos;   // 영화 정보

    public static MovieFindResponse of(String findValue, int totalCnt, List<MovieDto> movieDtos){
        return new MovieFindResponse(findValue,totalCnt, movieDtos);
    }
}

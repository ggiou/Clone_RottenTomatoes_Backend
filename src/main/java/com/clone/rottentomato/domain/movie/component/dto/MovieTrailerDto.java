package com.clone.rottentomato.domain.movie.component.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieTrailerDto {
    private Long id;    // 영화 예고편 id
    private int displayOrder;   // 예고편 노출 순서
    private String playUrl; // 영화 예고편 url
    private Long movieId;    // 영화 pk
}

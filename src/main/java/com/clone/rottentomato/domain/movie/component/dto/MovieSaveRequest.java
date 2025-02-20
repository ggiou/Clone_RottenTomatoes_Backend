package com.clone.rottentomato.domain.movie.component.dto;

import lombok.Getter;

@Getter
public class MovieSaveRequest {

    private String name;    // 영화 이름
    private int releaseYear;    // 개봉 년도

    private String searchUrl;   // 검색 url
}

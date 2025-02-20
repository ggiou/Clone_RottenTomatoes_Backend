package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.common.fomatter.DefaultDateFormat;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MovieDto {
    private long id;    // 영화 id
    private String name;    // 영화명
    private BigDecimal rating;  // 영화 평점 (리뷰 별점 기준 -> 0.0 ~ 5.0)
    private String posterUrl;   // 영화 포스터 url
    private String releaseDate;  // 개봉일


}

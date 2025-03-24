package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.util.UtilDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendMovieDto{
    private MovieDto movie;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private int recommendRank;  // 추천 등수
    @JsonIgnore
    private Long score;  // 추천 등수를 판단할 점수


    //TODO 해당 방식 JPA MovieDto가 생성된다면 위도 동일방식으로 변경
    public RecommendMovieDto(Movie movie, int recommendRank){
       this.movie = MovieDto.fromEntity(movie);
       this.recommendRank = recommendRank;
    }

    public RecommendMovieDto(Movie movie, Long score){
        this.movie = MovieDto.fromEntity(movie);
        this.score = score;
    }

    /** 영화 추천 반환을 위한 객체 생성 */
    public static RecommendMovieDto forRecommend(Movie entity, int recommendRank){
        if(Objects.isNull(entity)) return null;
        return new RecommendMovieDto(entity, recommendRank);
    }
}

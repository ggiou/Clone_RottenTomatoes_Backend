package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.domain.movie.component.entity.id.RecommendMovieId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 추천 영화 정보 */
@Entity
@Getter
@NoArgsConstructor
@IdClass(RecommendMovieId.class) // 복합 키 클래스 지정 (영화/카테고리 정보)
public class RecommendMovie {
    @Id
    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;    // 영화 id

    @Id
    @ManyToOne
    @JoinColumn(name = "RECOMMEND_MOVIE_ID")
    private Movie recommendMovie;    // 추천 영화 id

    private RecommendMovie(Movie movie, Movie recommendMovie){
        this.movie = movie;
        this.recommendMovie = recommendMovie;
    }

    public static RecommendMovie of(Movie movie, Movie recommendMovie){
        return new RecommendMovie(movie, recommendMovie);
    }
}

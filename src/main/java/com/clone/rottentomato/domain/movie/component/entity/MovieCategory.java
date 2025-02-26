package com.clone.rottentomato.domain.movie.component.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 각 영화별 카테고리 정보 (영화-카테고리 연결 정보) */
@Entity
@Getter
@NoArgsConstructor
@IdClass(MovieCategoryId.class) // 복합 키 클래스 지정 (영화/카테고리 정보)
public class MovieCategory {
    @Id
    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;    // 영화 id

    @Id
    @ManyToOne
    @JoinColumn(name = "CATEGORY_INFO_ID")
    private CategoryInfo categoryInfo;    // 영화 카테고리 정보 id

    private MovieCategory(Movie movie, CategoryInfo categoryInfo){
        this.movie = movie;
        this.categoryInfo = categoryInfo;
    }

    public static MovieCategory of(Movie movie, CategoryInfo categoryInfo){
        return new MovieCategory(movie, categoryInfo);
    }
}

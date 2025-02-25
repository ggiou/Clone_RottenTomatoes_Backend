package com.clone.rottentomato.domain.movie.component.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 각 영화별 카테고리 정보 (영화-카테고리 연결 정보) */
@Entity
@Getter
@NoArgsConstructor
public class MovieCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 영화 카테고리 id
    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;    // 영화 id
    @ManyToOne
    @JoinColumn(name = "CATEGORY_INFO_ID")
    private CategoryInfo categoryInfo;    // 영화 카테고리 정보 id

}

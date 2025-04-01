package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.fomatter.DefaultDateTimeFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** 추천 영화 정보 */
@Entity
@Getter
@NoArgsConstructor
// 영화에 대해 순위 & 추천 영화 unique 키 지정
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_movie_rank", columnNames = {"MOVIE_ID", "RECOMMEND_RANK"}),
        @UniqueConstraint(name = "unique_movie_recommend", columnNames = {"MOVIE_ID", "RECOMMEND_MOVIE_ID"})})
public class MovieRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;    // 기준 영화 id

    @ManyToOne
    @JoinColumn(name = "RECOMMEND_MOVIE_ID", nullable = false)
    private Movie recommendMovie;    // 추천 영화 id

    @Column(nullable = false)
    @Max(10) // 추천영화는 한 영화당 10등까지만 지정 (scope 가 작은 클론 코딩이라 임의로 10위까지만 지정)
    private int recommendRank; //추천 등수

    @DefaultDateTimeFormat
    @Column(nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime searchDate = LocalDateTime.now(); // 검색일자, 추천 영화는 검색일 기준으로 업데이트

    private MovieRecommend(Movie movie, Movie recommendMovie, int recommendRank){
        this.movie = movie;
        this.recommendMovie = recommendMovie;
        this.recommendRank = recommendRank;
    }

    public static MovieRecommend of(Movie movie, Movie recommendMovie, int recommendRank){
        return new MovieRecommend(movie, recommendMovie, recommendRank);
    }

    public void changeRecommendMovie(Movie recommendMovie){
        this.recommendMovie = recommendMovie;
        this.searchDate = LocalDateTime.now();
    }
}

package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.fomatter.DefaultDateTimeFormat;
import com.clone.rottentomato.domain.movie.component.entity.id.MovieRecommendId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/** 추천 영화 정보 */
@Entity
@Getter
@NoArgsConstructor
@IdClass(MovieRecommendId.class) // 복합 키 클래스 지정 (영화/카테고리 정보)
// 영화에 대해 순위 unique 키 지정
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_movie_rank", columnNames = {"MOVIE_ID", "RECOMMEND_RANK"})})
public class MovieRecommend extends TimeStamped {
    @Id
    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;    // 기준 영화 id

    @Id
    @ManyToOne
    @JoinColumn(name = "RECOMMEND_MOVIE_ID")
    private Movie recommendMovie;    // 추천 영화 id

    @Column(nullable = false)
    @Max(10) // 추천영화는 한 영화당 10등까지만 지정 (scope 가 작은 클론 코딩이라 임의로 10위까지만 지정)
    private int recommendRank; //추천 등수

    @DefaultDateTimeFormat
    @CreationTimestamp
   // @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime searchDate;

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
    }
}

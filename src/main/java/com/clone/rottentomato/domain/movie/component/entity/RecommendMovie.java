package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.domain.movie.component.entity.id.RecommendMovieId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

/** 추천 영화 정보 */
@Entity
@Getter
@NoArgsConstructor
@IdClass(RecommendMovieId.class) // 복합 키 클래스 지정 (영화/카테고리 정보)
// 영화에 대해 순위 unique 키 지정
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_movie_rank", columnNames = {"MOVIE_ID", "RECOMMEND_RANK"})})
public class RecommendMovie extends TimeStamped {
    @Id
    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;    // 영화 id

    @Id
    @ManyToOne
    @JoinColumn(name = "RECOMMEND_MOVIE_ID")
    private Movie recommendMovie;    // 추천 영화 id

    @Column(nullable = false)
    @Max(10) // 추천영화는 한 영화당 10등까지만 지정 (scope 가 작은 클론 코딩이라 임의로 10위까지만 지정)
    private int recommendRank; //추천 등수

    private RecommendMovie(Movie movie, Movie recommendMovie, int recommendRank){
        this.movie = movie;
        this.recommendMovie = recommendMovie;
        this.recommendRank = recommendRank;
    }

    public static RecommendMovie of(Movie movie, Movie recommendMovie, int recommendRank){
        return new RecommendMovie(movie, recommendMovie, recommendRank);
    }

    public void changeRecommendMovie(Movie recommendMovie){
        this.recommendMovie = recommendMovie;
    }
}

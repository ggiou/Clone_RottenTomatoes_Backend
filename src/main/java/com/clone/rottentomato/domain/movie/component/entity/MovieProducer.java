package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.domain.movie.component.entity.id.MovieProducerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 각 영화별 제작자 정보 (영화-제작자 연결 정보) */
@Entity
@Getter
@NoArgsConstructor
@IdClass(MovieProducerId.class) // 복합 키 클래스 지정 (영화/카테고리 정보)
public class MovieProducer {
    @Id
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;    // 영화 id

    @Id
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "PRODUCER_ID")
    private Producer producer;    // 영화 카테고리 정보 id

    private MovieProducer(Movie movie, Producer producer){
        this.movie = movie;
        this.producer = producer;
    }

    public static MovieProducer of(Movie movie, Producer producer){
        return new MovieProducer(movie, producer);
    }
}

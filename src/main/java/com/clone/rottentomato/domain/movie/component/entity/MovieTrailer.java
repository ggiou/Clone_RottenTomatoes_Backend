package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
public class MovieTrailer extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 영화 예고편 id
    @Column(nullable = false)
    @Max(value = 10, message = "영화 예고편은 최대 10개 까지 등록 가능합니다.")
    private int displayOrder;   // 예고편 노출 순서
    // TODO 영화 예고편 노출을 url로만 전달할거라면, url 값 컬럼 추가.. 이처럼 노출해주는 방식에 따라 컬럼 추가 필요

    @OneToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;    // 영화 정보
}

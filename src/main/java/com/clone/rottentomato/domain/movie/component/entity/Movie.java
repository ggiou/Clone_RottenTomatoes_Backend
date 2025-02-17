package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Movie extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 영화 id
    @Column(nullable = false)
    private String name;    // 영화명
    @Column(columnDefinition = "decimal(2, 1) default 0.0 max 5.0")
    private BigDecimal rating;  // 영화 평점 (리뷰 별점 기준)
    @ColumnDefault("")  // FIXME 데이터 없을 경우 기본 이미지 URL 지정 후 세팅
    private String posterUrl;   // 영화 포스터 url
    private LocalDateTime releaseDate;  // 개봉일
}

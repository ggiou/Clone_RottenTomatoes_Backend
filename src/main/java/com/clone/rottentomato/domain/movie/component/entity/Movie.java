package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.fomatter.DefaultDateFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
public class Movie extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 영화 id
    @Column(nullable = false)
    @Size(max = 100, message = "영화 제목은 최대 100자까지 입력가능합니다.")
    private String name;    // 영화명
    @Column(columnDefinition = "decimal(2, 1) default 0.0 check (rating >= 0.0 and rating <= 5.0)")
    private BigDecimal rating;  // 영화 평점 (리뷰 별점 기준 -> 0.0 ~ 5.0)
    @Column(columnDefinition = "varchar(255) default ''")
    private String posterUrl;   // 영화 포스터 url       // FIXME 데이터 없을 경우 기본 이미지 URL 지정 후 세팅
    @DefaultDateFormat
    private LocalDateTime releaseDate;  // 개봉일
}

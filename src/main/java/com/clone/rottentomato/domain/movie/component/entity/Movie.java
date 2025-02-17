package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.fomatter.DefaultDateFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(columnDefinition = "decimal(2, 1) default 0.0 max 5.0")
    private BigDecimal rating;  // 영화 평점 (리뷰 별점 기준)
    @ColumnDefault("")  // FIXME 데이터 없을 경우 기본 이미지 URL 지정 후 세팅
    private String posterUrl;   // 영화 포스터 url
    @DefaultDateFormat
    private LocalDateTime releaseDate;  // 개봉일

    @OneToOne(mappedBy = "movieDetail", fetch = FetchType.LAZY)
    private Movie movieDetail;  // 영화 상세 정보 (lazy 방식으로, 필요할때 따로 쿼리를 통해 조회)
}

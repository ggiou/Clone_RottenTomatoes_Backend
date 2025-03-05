package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.fomatter.DefaultDateFormat;
import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.exception.MovieException;
import com.clone.rottentomato.util.UtilDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Movie extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 영화 id
    @Column(nullable = false)
    @Size(max = 100, message = "영화 제목은 최대 100자까지 입력가능합니다.")
    private String name;    // 영화명
    @Column(precision = 2, scale = 1, nullable = false)
    @DecimalMax(value = "5.0", message = "영화 평점 값은 5 이하이어야 합니다.")
    @DecimalMin(value = "0.0", message = "영화 평점 값은 0 이상이어야 합니다.")
    private BigDecimal rating;  // 영화 평점 (리뷰 별점 기준 -> 0.0 ~ 5.0)
    @Column(columnDefinition = "varchar(255) default ''")
    private String posterUrl;   // 영화 포스터 url       // FIXME 데이터 없을 경우 기본 이미지 URL 지정 후 세팅
    @DefaultDateFormat
    private LocalDateTime releaseDate;  // 개봉일

    @PrePersist
    public void prePersist() {
        if (rating == null) {
            rating = BigDecimal.valueOf(0.0);
        }
    }

    private Movie(Long id, String name, BigDecimal rating, String posterUrl, LocalDateTime releaseDate){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    public static Movie fromDto(MovieDto dto){
        if(Objects.isNull(dto)) throw new MovieException("영화 정보가 없습니다.", CommonError.BAD_REQUEST);
        LocalDateTime releaseDate = UtilDate.getLocalDateTimeOrEls(dto.getReleaseDate(), null);
        if(Objects.isNull(releaseDate)) throw new MovieException("유효한 개봉일자가 아닙니다.", CommonError.DATE_FORMAL_ERROR);
        return new Movie(dto.getId(), dto.getName(), dto.getRating(), dto.getPosterUrl(), releaseDate);
    }
}

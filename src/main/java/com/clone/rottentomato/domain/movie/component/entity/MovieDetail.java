package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.converter.CommaStringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Getter
@Entity
public class MovieDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 영화 id
    @ColumnDefault(StringUtils.EMPTY)
    @Size(max = 1000, message = "영화 줄거리는 최대 1000자까지 입력 가능합니다.")
    private String story;   // 영화 줄거리
    @ColumnDefault(StringUtils.EMPTY)
    @Size(min = 500, message = "영화 배우 이름들은 최대 500자까지 입력 가능합니다.")
    @Convert(converter = CommaStringListConverter.class)
    private List<String> actorNames;  // 영화 배우 이름들
    @ColumnDefault(StringUtils.EMPTY)
    @Size(min = 500, message = "영화 감독 이름들은 최대 500자까지 입력 가능합니다.")
    @Convert(converter = CommaStringListConverter.class)
    private String directorNames;   // 영화 감독 이름들

    @OneToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;    // 영화 정보
}

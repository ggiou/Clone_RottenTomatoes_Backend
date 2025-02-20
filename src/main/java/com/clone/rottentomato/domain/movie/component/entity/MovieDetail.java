package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.converter.CommaStringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class MovieDetail extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // 영화 상세정보 id
    @Column(columnDefinition = "varchar(1000) default ''")
    @Size(max = 1000, message = "영화 줄거리는 최대 1000자까지 입력 가능합니다.")
    private String story;   // 영화 줄거리
    @Column(columnDefinition = "varchar(500) default ''")
    @Size(min = 500, message = "영화 배우 이름들은 최대 500자까지 입력 가능합니다.")
    @Convert(converter = CommaStringListConverter.class)
    private List<String> actorNames;  // 영화 배우 이름들
    @Column(columnDefinition = "varchar(500) default ''")
    @Size(min = 500, message = "영화 감독 이름들은 최대 500자까지 입력 가능합니다.")
    @Convert(converter = CommaStringListConverter.class)
    private String directorNames;   // 영화 감독 이름들

    @OneToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;    // 영화 정보
}

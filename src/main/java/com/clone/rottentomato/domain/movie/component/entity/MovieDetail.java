package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.converter.CommaStringListConverter;
import com.clone.rottentomato.domain.movie.component.dto.MovieDetailDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.exception.MovieException;
import com.clone.rottentomato.util.UtilDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private List<String> directorNames;   // 영화 감독 이름들

    @OneToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;    // 영화 정보

    private MovieDetail(Long id, String story, List<String> actorNames, List<String> directorNames, Movie movie){
        this.id = id;
        this.story = story;
        this.actorNames = actorNames;
        this.directorNames = directorNames;
        this.movie = movie;
    }

    public static MovieDetail fromDto(MovieDetailDto dto, Movie movie){
        if(Objects.isNull(dto)) throw new MovieException("영화 상세 정보가 없습니다.", CommonError.BAD_REQUEST);
        if(Objects.isNull(movie)) throw new MovieException("영화 정보가 없습니다.", CommonError.DATE_FORMAL_ERROR);
        return new MovieDetail(dto.getId(), dto.getStory(), dto.getActorNames(), dto.getDirectorNames(), movie);
    }
}

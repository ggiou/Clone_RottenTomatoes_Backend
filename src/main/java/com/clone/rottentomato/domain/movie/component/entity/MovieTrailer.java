package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.domain.movie.component.dto.MovieTrailerDto;
import com.clone.rottentomato.exception.MovieException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
                @UniqueConstraint(columnNames = {"movie_id", "display_order"})})
public class MovieTrailer extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 영화 예고편 id
    @Column(nullable = false)
    @Max(value = 20, message = "영화 예고편은 최대 20개 까지 등록 가능합니다.")
    private int displayOrder;   // 예고편 노출 순서
    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String playUrl;     // 예고편 재생 url
    @Column(nullable = false)
    private String playName;    // 예고편 지정 이름
    @Column(nullable = false)
    @Pattern(regexp = "(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9])", message = "유효한 예고편 재생 시간이 아닙니다.(mm:ss)")
    private String playTime;    // 예고편 총 재생 시간 (mm:ss)


    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "MOVIE_ID", nullable = false, unique = false)
    private Movie movie;    // 영화 정보

    private MovieTrailer(Long id, int displayOrder, String playUrl, String playName, String playTime, Movie movie){
        this.id = id;
        this.displayOrder = displayOrder;
        this.playUrl = playUrl;
        this.playName = playName;
        this.playTime = playTime;
        this.movie = movie;
    }

    public static MovieTrailer fromDto(MovieTrailerDto dto, Movie movie){
        if(Objects.isNull(dto)) throw new MovieException("영화 예고편 정보가 없습니다.", CommonError.BAD_REQUEST);
        if(Objects.isNull(movie)) throw new MovieException("영화 정보가 없습니다.", CommonError.DATE_FORMAL_ERROR);
        return new MovieTrailer(dto.getId(), dto.getDisplayOrder(), dto.getPlayUrl(), dto.getPlayName(), dto.getPlayTime(), movie);
    }
}

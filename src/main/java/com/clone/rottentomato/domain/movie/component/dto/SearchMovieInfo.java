package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.util.UtilString;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchMovieInfo {
    private MovieDto movieDto;  // 영화 정보
    private List<String> actorNames;    // 영화 배우
    private List<String> directorNames; // 영화 감독
    private String actorNamesStr;   // 영화 배우 이름 문자
    private String directorNamesStr;    // 영화 감독 이름 문자

    public SearchMovieInfo(Long id, String name, BigDecimal rating, String posterUrl, LocalDateTime releaseDate, String actorNames, String directorNames) {
        this.movieDto = new MovieDto(id, name, rating, posterUrl, releaseDate);  // MovieDto 인스턴스 생성
        this.actorNames = UtilString.makeListByDelimiter(actorNames, ",");
        this.directorNames = UtilString.makeListByDelimiter(directorNames, ",");
        this.actorNamesStr = actorNames;
        this.directorNamesStr = directorNames;
    }

    public SearchMovieInfo(Movie movie, String actorNames, String directorNames) {
        this.movieDto = Objects.isNull(movie) ? null : MovieDto.fromEntity(movie);  // MovieDto 인스턴스 생성
        this.actorNames = UtilString.makeListByDelimiter(actorNames, ",");
        this.directorNames = UtilString.makeListByDelimiter(directorNames, ",");
        this.actorNamesStr = actorNames;
        this.directorNamesStr = directorNames;
    }

    @SqlResultSetMapping(name = "SearchMovieInfoMapping", classes = {@ConstructorResult(targetClass = SearchMovieInfo.class,
            columns = {@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "rating", type = Double.class), @ColumnResult(name = "poster_url", type = String.class),
                    @ColumnResult(name = "release_date", type = String.class), @ColumnResult(name = "actor_names", type = String.class),
                    @ColumnResult(name = "director_names", type = String.class)})})
    public class SearchMovieInfoMapping {
        // This class is used for mapping native query results to SearchMovieInfo
    }
}

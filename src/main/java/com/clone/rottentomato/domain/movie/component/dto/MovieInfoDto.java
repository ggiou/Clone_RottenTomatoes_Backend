package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.component.entity.MovieDetail;
import com.clone.rottentomato.domain.movie.component.entity.MovieTrailer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoDto {
    private MovieDto movieDto;
    private MovieDetailDto movieDetailDto;
    private List<MovieTrailerDto> movieTrailerDtoList = new ArrayList<>();
    private List<CategoryInfoDto> categoryList = new ArrayList<>();
    private String categoryStr;

    /** 영화 정보 dto 의 모든 엔티티를 통해 dto 로 변환해 생성*/
    public static MovieInfoDto fromEntity(Movie movie, MovieDetail movieDetail, List<MovieTrailer> movieTrailers, List<CategoryInfo> categoryInfos) {
        MovieDto movieDto = MovieDto.fromEntity(movie);
        MovieDetailDto movieDetailDto = MovieDetailDto.fromEntity(movieDetail);
        List<MovieTrailerDto> movieTrailerDtoList = movieTrailers.stream().map(MovieTrailerDto::fromEntity).toList();
        List<CategoryInfoDto> categoryInfoDtoList = new ArrayList<>();
        StringBuilder categoryStr = new StringBuilder(StringUtils.EMPTY);
        for(CategoryInfo categoryInfo : categoryInfos) {
            categoryInfoDtoList.add(CategoryInfoDto.fromEntity(categoryInfo));
            categoryStr.append(categoryInfo.getName()).append(",");
        }
        return new MovieInfoDto(movieDto, movieDetailDto, movieTrailerDtoList, categoryInfoDtoList, categoryStr.toString().replaceFirst(",$", StringUtils.EMPTY));
    }
}

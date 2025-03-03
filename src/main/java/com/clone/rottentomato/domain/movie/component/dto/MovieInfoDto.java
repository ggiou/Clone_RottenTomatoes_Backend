package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.crawling.constant.CrawlingSite;
import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

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
}

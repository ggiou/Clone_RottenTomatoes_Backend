package com.clone.rottentomato.domain.movie.service;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.crawling.service.WebDriverService;
import com.clone.rottentomato.crawling.service.WebElementService;
import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final WebDriver webDriver = WebDriverService.getInstance().getChromeDriver();
    private final WebElementService webElementService = new WebElementService(webDriver);

    /** 특정 영화 정보를 저장하는 함수 */
    public CommonResponse saveMovieProcess(List<MovieSaveRequest> request){

        //List<MovieDto>

        //List<MovieDto>
        return new CommonResponse();
    }

    private List<MovieDto> getMovieInfoOfNamuWikiByCrawling(){

        return new ArrayList<>();
    }
}

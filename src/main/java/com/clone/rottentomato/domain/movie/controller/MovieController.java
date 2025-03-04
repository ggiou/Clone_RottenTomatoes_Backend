package com.clone.rottentomato.domain.movie.controller;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
import com.clone.rottentomato.domain.movie.constant.MovieError;
import com.clone.rottentomato.domain.movie.service.MovieService;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.exception.MovieException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    /** 특정 영화의 전체 정보 반환 (상세보기) */
    @GetMapping("{movieId}")
    public CommonResponse getMovieInfo(@PathVariable final Long movieId) {
        if(Objects.isNull(movieId) || movieId <= 0) {
            throw new MovieException("잘못된 요청입니다.", MovieError.BAD_REQUEST_MOVIE_ID);
        }
        return movieService.getMovieInfo(movieId);
    }

    /** 영화 리스트 반환 */
    @GetMapping("/list")
    public CommonResponse getMovieList(){
        return new CommonResponse();
    }

    /** 특정 영화에 대한 추천 영화 리스트 반환 */
    @GetMapping("/recommend")
    public CommonResponse getMovieRecommendList(){
        return new CommonResponse();
    }

    /** 영화 리스트 검색 반환 */
    @PostMapping("/search")
    public CommonResponse getMovieSearchList(){
        return new CommonResponse();
    }

    /** 비디오 정보 저장 api */
    @PostMapping("/save")
    public CommonResponse saveMovie(@RequestBody(required = false) @Valid List<MovieSaveRequest> request, BindingResult bindingResult){
        if(Objects.isNull(request)) throw new MovieException("입력한 정보가 없습니다. 영화 저장 요청 정보를 입력해주세요.", CommonError.BAD_REQUEST);
        if(bindingResult.hasErrors()) {
            throw new CommonException(bindingResult.getAllErrors().get(0).getDefaultMessage(), CommonError.INVALID_REQUEST);
        }
        CommonResponse response = movieService.saveMovieProcess(request);;
        return response;
    }
}

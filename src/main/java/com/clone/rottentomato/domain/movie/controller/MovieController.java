package com.clone.rottentomato.domain.movie.controller;

import com.amazonaws.util.StringUtils;
import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
import com.clone.rottentomato.domain.movie.constant.MovieError;
import com.clone.rottentomato.domain.movie.service.MovieService;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.exception.MovieException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /** 영화 리스트 검색 반환
     * @param searchValue : 검색할 문자열 값
     * @param pageNo : n 번째 페이지 정보
     * @param pageSize : 1페이지당 돌려줄 개수
     * */
    @PostMapping("/search")
    public CommonResponse getMovieSearchList(@RequestParam(value = "value", required = false) String searchValue,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize){
        if(StringUtils.isNullOrEmpty(searchValue)){
            throw new MovieException("검색을 위한 value의 pathValue가 없습니다. 검색이 불가합니다.", MovieError.BAD_REQUEST_SEARCH_VALUE);
        }

        return movieService.searchMovieList(searchValue, pageNo, pageSize);
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

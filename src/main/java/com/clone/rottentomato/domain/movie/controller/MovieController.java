package com.clone.rottentomato.domain.movie.controller;

import com.amazonaws.util.StringUtils;
import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.domain.movie.component.dto.*;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.constant.MovieError;
import com.clone.rottentomato.domain.movie.service.MovieService;
import com.clone.rottentomato.exception.CommonException;
import com.clone.rottentomato.exception.MovieException;
import com.clone.rottentomato.util.UtilMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.clone.rottentomato.common.constant.CommonConst.DATA_NAME.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    /** 특정 영화의 전체 정보 반환 (상세보기)
     * @param movieId - pathValue, 영화 pk
     * @return CommonResponse.Movie*/
    @GetMapping("{movieId}")
    public CommonResponse getMovieInfo(@PathVariable final Long movieId) {
        if(Objects.isNull(movieId) || movieId <= 0) {
            throw new MovieException("잘못된 요청입니다.", MovieError.BAD_REQUEST_MOVIE_ID);
        }
        return movieService.getMovieInfo(movieId);
    }

    /** 카테고리별 영화 리스트 반환
     * */
    @PostMapping("/list/category")
    public CommonResponse getMovieListByCategory(@RequestBody(required = false) final List<MovieFindRequest> request, BindingResult bindingResult){
        if(Objects.isNull(request) || request.isEmpty()) throw new MovieException("입력한 정보가 없습니다. 카테고리별 영화 리스트 요청 정보를 입력해주세요.", CommonError.BAD_REQUEST);
        if(bindingResult.hasErrors()) {
            throw new CommonException(bindingResult.getAllErrors().get(0).getDefaultMessage(), CommonError.INVALID_REQUEST);
        }
        List<MovieFindResponse> findResponses = movieService.getMovieListByCategory(request);
        if (findResponses.isEmpty()) return CommonResponse.fail("카테고리별 영화 리스트 요청에 실패했습니다. 존재하는 카테고리 영화가 없습니다.", UtilMap.makeMap(FIND_MAP_NAME));
        return CommonResponse.success("카테고리별 영화 리스트 요청에 성공했습니다.", UtilMap.makeMap(FIND_MAP_NAME, findResponses));
    }

    /** 영화 리스트 반환 (전체 영화에서, 정렬 기준으로 pageable 해 반환)*/
    @PostMapping("/list")
    public CommonResponse getMovieList(@RequestBody(required = false) final List<MovieFindRequest> request){
        if(Objects.isNull(request) || request.isEmpty()) throw new MovieException("입력한 정보가 없습니다. 영화 리스트 요청 정보를 입력해주세요.", CommonError.BAD_REQUEST);
        List<MovieFindResponse> findResponses = movieService.getMovieListBySort(request);
        if(findResponses.isEmpty()) return CommonResponse.fail("영화 리스트 요청에 실패했습니다. 존재하는 영화가 없습니다.", UtilMap.makeMap(FIND_MAP_NAME));
        return CommonResponse.success("영화 리스트 요청에 성공했습니다.", UtilMap.makeMap(FIND_MAP_NAME, findResponses));
    }



    /** 특정 영화에 대한 추천 영화 리스트 반환 */
    @GetMapping("/recommend")
    public CommonResponse getMovieRecommendList(@RequestParam(value = "movieId") Long movieId, @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        if(Objects.isNull(movieId) || movieId <= 0) throw new MovieException("추천 영화 리스트를 반환 받으실 영화 id 값을 입력해주세요.", MovieError.BAD_REQUEST_MOVIE_ID);
        List<MovieDto> recommendMovies = movieService.searchRecommendMovieListByMovieId(movieId, size);
        return new CommonResponse();
    }

    /** 영화 리스트 검색 반환
     * @param searchValue : 검색할 문자열 값
     * @param pageNo : n 번째 페이지 정보
     * @param pageSize : 1페이지당 돌려줄 개수
     * @return CommonResponse.List<SearchResponse>
     * */
    @GetMapping("/search")
    public CommonResponse getMovieSearchList(@RequestParam(value = "value", required = false) String searchValue,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize){
        if(StringUtils.isNullOrEmpty(searchValue)){
            throw new MovieException("검색을 위한 value의 pathValue가 없습니다. 검색이 불가합니다.", MovieError.BAD_REQUEST_SEARCH_VALUE);
        }

        SearchResponse searchResponse = movieService.searchMovieList(searchValue, pageNo, pageSize);
        if(!searchResponse.isSuccess()){
            return CommonResponse.fail("검색 결과를 찾는데 일부 실패했습니다.", searchResponse);
        }
        return CommonResponse.success("검색 결과를 찾는데 성공했습니다.", searchResponse);
    }


    /** 비디오 정보 저장 api
     * @param request - 비디오 정보 저장 request
     * @return CommonResponse.List<MovieInfo> */
    @PostMapping("/save")
    public CommonResponse saveMovie(@RequestBody(required = false) @Valid List<MovieSaveRequest> request, BindingResult bindingResult){
        if(Objects.isNull(request)) throw new MovieException("입력한 정보가 없습니다. 영화 저장 요청 정보를 입력해주세요.", CommonError.BAD_REQUEST);
        if(bindingResult.hasErrors()) {
            throw new CommonException(bindingResult.getAllErrors().get(0).getDefaultMessage(), CommonError.INVALID_REQUEST);
        }
        return movieService.saveMovieProcess(request);
    }
}

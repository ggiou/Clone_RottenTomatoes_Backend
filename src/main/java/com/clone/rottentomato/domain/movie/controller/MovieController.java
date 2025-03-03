package com.clone.rottentomato.domain.movie.controller;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
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

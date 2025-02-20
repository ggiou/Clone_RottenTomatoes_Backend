package com.clone.rottentomato.domain.movie.controller;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/save")
    public CommonResponse saveMovie(BindingResult bindingResult){
        if(bindingResult.hasErrors()){

        }
        CommonResponse response = CommonResponse.Success("");
        return response;
    }
}

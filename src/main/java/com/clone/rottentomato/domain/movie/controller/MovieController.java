package com.clone.rottentomato.domain.movie.controller;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.movie.component.dto.MovieSaveRequest;
import com.clone.rottentomato.domain.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/save")
    public CommonResponse saveMovie(@Valid @RequestBody MovieSaveRequest request, BindingResult bindingResult){
        CommonResponse response = CommonResponse.success("");
        return response;
    }
}

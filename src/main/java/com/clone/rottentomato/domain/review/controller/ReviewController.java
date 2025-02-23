package com.clone.rottentomato.domain.review.controller;


import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewResponseDto;
import com.clone.rottentomato.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/{movie_id}")
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDto>create(@PathVariable Long movie_id, @RequestBody ReviewRequestDto reviewRequestDto) {
        return reviewService.create(movie_id,reviewRequestDto);
    }

}

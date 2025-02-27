package com.clone.rottentomato.domain.review.controller;


import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewResponseDto;
import com.clone.rottentomato.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/{movie_id}")
public class ReviewController {

    private final ReviewService reviewService;


    //  리뷰작성
    @PostMapping("/review")
    public ResponseEntity<ReviewResponseDto>createReview(@PathVariable Long movie_id, @RequestBody ReviewRequestDto reviewRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(movie_id,reviewRequestDto,userDetails.getMember());
    }

}

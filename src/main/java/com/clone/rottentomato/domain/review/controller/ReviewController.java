package com.clone.rottentomato.domain.review.controller;


import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.common.component.dto.SortRequestDto;
import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.mypage.component.dto.MypageReviewResponseDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberRepository memberRepository;


    //  리뷰작성
    @PostMapping("/{movie_id}/review")
    public CommonResponse createReview(@PathVariable Long movie_id, @RequestBody ReviewRequestDto reviewRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(movie_id,reviewRequestDto,userDetails.getMember());
    }


    //  리뷰 전체 조회
    @PostMapping("/reviews")
    public ResponseEntity<List<MypageReviewResponseDto>> getReviews(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                    @RequestParam(value = "page")int page,
                                                                    @RequestParam(value = "size")int size,
                                                                    @RequestBody SortRequestDto requestDto) {
        return reviewService.getReviews(userDetails.getMember(),page,size,requestDto);
    }


    //  리뷰 상세 조회
    @GetMapping("/review/{review_id}")
    public CommonResponse getReview(@PathVariable Long review_id,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.getreview(review_id,userDetails.getMember());
    }


    //  리뷰 수정
    @PutMapping("/review/{review_id}")
    public CommonResponse updateReview(@PathVariable Long review_id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody ReviewRequestDto reviewRequestDto) {
        return reviewService.updateReview(review_id,userDetails.getMember(),reviewRequestDto);
    }


    //  리뷰 삭제
    @DeleteMapping("/review/{review_id}")
    public CommonResponse deleteReview(@PathVariable Long review_id,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.deleteReview(review_id,userDetails.getMember());
    }


}

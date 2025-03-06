package com.clone.rottentomato.domain.review.service;


import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewResponseDto;
import com.clone.rottentomato.domain.review.component.dto.SuccessResponse;
import com.clone.rottentomato.domain.review.component.entity.Review;
import com.clone.rottentomato.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;


    //  리뷰 작성
    public ResponseEntity<ReviewResponseDto> createReview(Long movieId, ReviewRequestDto reviewRequestDto, Member member) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new IllegalArgumentException("Movie not found")
        );
        Optional<Review> review = reviewRepository.findTopByMemberAndMovieOrderByRegDateDesc(member,movie);
        if(review.isPresent()) {
            throw new IllegalArgumentException("이미 리뷰를 등록하셨습니다.");
        }
            Review saveReview = reviewRepository.save(new Review(reviewRequestDto,member,movie));
            return ResponseEntity.ok(ReviewResponseDto.of(saveReview,member,movie));
    }


    //  리뷰 수정
    public ResponseEntity<ReviewResponseDto> updateReview(Long reviewId, UserDetailsImpl userDetails, ReviewRequestDto reviewRequestDto) {
        Review review = getReview(reviewId);
        Optional<Review> esxit = reviewRepository.findByIdAndMember(reviewId,userDetails.getMember());
        if(esxit.isEmpty()){
            throw new IllegalArgumentException("작성자만 수정 / 삭제가 가능합니다.");
        }
        review.update(reviewRequestDto);
        return ResponseEntity.ok(ReviewResponseDto.update_from(review));
    }


    //  리뷰 삭제
    public ResponseEntity<SuccessResponse> deleteReview(Long reviewId, UserDetailsImpl userDetails) {
        Review review = getReview(reviewId);
        Optional<Review> esxit = reviewRepository.findByIdAndMember(reviewId,userDetails.getMember());
        if(esxit.isEmpty()){
            throw new IllegalArgumentException("작성자만 수정 / 삭제가 가능합니다.");
        }
        reviewRepository.delete(review);
        return ResponseEntity.ok(SuccessResponse.of(HttpStatus.OK,"삭제가 완료되었습니다."));
    }


//              ---         메서드             ---
    private Review getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다.")
        );
        return review;
    }
}
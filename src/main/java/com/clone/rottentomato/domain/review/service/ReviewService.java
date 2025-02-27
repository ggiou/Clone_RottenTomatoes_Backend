package com.clone.rottentomato.domain.review.service;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieRepository;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewResponseDto;
import com.clone.rottentomato.domain.review.component.entity.Review;
import com.clone.rottentomato.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}

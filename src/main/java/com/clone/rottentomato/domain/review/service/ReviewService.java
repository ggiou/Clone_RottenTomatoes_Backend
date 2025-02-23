package com.clone.rottentomato.domain.review.service;


import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import com.clone.rottentomato.domain.review.component.dto.ReviewResponseDto;
import com.clone.rottentomato.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;



    public ResponseEntity<ReviewResponseDto> create(Long movieId, ReviewRequestDto reviewRequestDto) {
        return null;
    }
}

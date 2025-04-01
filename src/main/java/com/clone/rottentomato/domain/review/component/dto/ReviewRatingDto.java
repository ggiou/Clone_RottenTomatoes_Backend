package com.clone.rottentomato.domain.review.component.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class ReviewRatingDto {
    private Long reviewCnt;      // 현재 리뷰 개수
    private Long reviewSum;      // 현재 리뷰 별점 총 합

    public ReviewRatingDto(Long reviewCnt, Long reviewSum){
        this.reviewCnt = reviewCnt;
        this.reviewSum = reviewSum;
    }
}

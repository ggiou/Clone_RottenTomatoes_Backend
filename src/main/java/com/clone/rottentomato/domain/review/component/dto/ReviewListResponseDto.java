package com.clone.rottentomato.domain.review.component.dto;


import com.clone.rottentomato.domain.review.component.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewListResponseDto {
    private Long id;
    private String memberName;
    private String memberEmail;
    private Integer rating;
    private String reviewContent;


    @Builder
    public ReviewListResponseDto(Review review) {
        this.id = review.getReview_id();
        this.memberName = review.getMember().getMemberName();
        this.memberEmail = review.getMember().getMemberEmail();
        this.rating = review.getRating();
        this.reviewContent = review.getReviewContent();
    }


    public static ReviewListResponseDto from(Review review) {
        return ReviewListResponseDto.builder()
                .review(review)
                .build();
    }
}

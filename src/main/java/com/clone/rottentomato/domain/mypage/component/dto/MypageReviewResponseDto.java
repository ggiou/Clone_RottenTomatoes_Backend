package com.clone.rottentomato.domain.mypage.component.dto;

import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.review.component.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MypageReviewResponseDto {
    private Long id;
    private String content;
    private String memberName;
    private String reviewContent;
    private String posterUrl;
    private BigDecimal rating;
    private LocalDateTime createTime;


    @Builder
    public MypageReviewResponseDto(Review review, Member member, Movie movie) {
        this.id = review.getReview_id();
        this.content = movie.getName();
        this.reviewContent = review.getReviewContent();
        this.posterUrl = movie.getPosterUrl();
        this.rating = movie.getRating();
        this.memberName = member.getMemberName();
        this.createTime = getCreateTime();
    }


    public static MypageReviewResponseDto from(Review review,Member member, Movie movie) {
        return MypageReviewResponseDto.builder()
                .review(review)
                .member(member)
                .movie(movie)
                .build();
    }
}

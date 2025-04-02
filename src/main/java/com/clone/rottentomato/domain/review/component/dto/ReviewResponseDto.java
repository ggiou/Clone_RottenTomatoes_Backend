package com.clone.rottentomato.domain.review.component.dto;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.review.component.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Integer rating;
    private String reviewContent;
    private String memberEmail;
    private String memberName;
    private String movieName;
    private String posterUrl;

    @Builder
    public ReviewResponseDto(Review review, Member member, Movie movie) {
        this.id = review.getReview_id();
        this.rating = review.getRating();
        this.reviewContent = review.getReviewContent();
        this.memberEmail = member.getMemberEmail();
        this.memberName = member.getMemberName();
        this.movieName = movie.getName();
        this.posterUrl = movie.getPosterUrl();
    }

    public static ReviewResponseDto of(Review review,Member member, Movie movie) {
        return ReviewResponseDto.builder()
                .review(review)
                .member(member)
                .review(review)
                .movie(movie)
                .build();
    }


    public static ReviewResponseDto update_from(Review review) {
        return ReviewResponseDto.builder()
                .review(review)
                .member(review.getMember())
                .movie(review.getMovie())
                .build();
    }
}

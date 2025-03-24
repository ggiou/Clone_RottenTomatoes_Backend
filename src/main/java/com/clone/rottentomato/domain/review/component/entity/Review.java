package com.clone.rottentomato.domain.review.component.entity;


import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.constant.SortType;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class Review extends TimeStamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id;
    @Column(nullable = false)
    private Integer rating;
    @Column(nullable = false)
    @Size(max = 1000, message = "리뷰는 최대 1000자까지 입력 가능합니다.")
    private String reviewContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID",nullable = false)
    private Movie movie;

    public Review(Member member, Movie movie, String reviewContent, Integer rating) {
        this.member = member;
        this.movie = movie;
        this.reviewContent = reviewContent;
        this.rating = rating;
    }


    public static Review of(ReviewRequestDto reviewRequestDto, Member member, Movie movie) {
        return new Review(member, movie, reviewRequestDto.getReviewContent(), reviewRequestDto.getRating());
    }


    public void update(ReviewRequestDto reviewRequestDto) {
        this.rating = reviewRequestDto.getRating();
        this.reviewContent = reviewRequestDto.getReviewContent();
    }
}

package com.clone.rottentomato.domain.review.component.entity;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.review.component.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id;
    @Column(nullable = false)
    private Integer field;
    @Column(nullable = false,length = 5000)
    private String reviewContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID",nullable = false)
    private Movie movie;


    @Builder
    public Review(ReviewRequestDto reviewRequestDto, Member member, Movie movie) {
        this.field = reviewRequestDto.getField();
        this.reviewContent = reviewRequestDto.getReviewContent();
        this.member = member;
        this.movie = movie;
    }


    public static Review of(ReviewRequestDto reviewRequestDto, Member member, Movie movie) {
        return Review.builder()
                .reviewRequestDto(reviewRequestDto)
                .member(member)
                .movie(movie)
                .build();
    }
}

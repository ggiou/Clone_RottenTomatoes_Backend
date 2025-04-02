package com.clone.rottentomato.domain.mypage.component.dto;

import com.clone.rottentomato.domain.likes.component.entity.Likes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class MypageMovieResponseDto {
    private Long id;
    private String name;    // 영화명
    private String posterUrl;
    private BigDecimal rating;


    @Builder
    public MypageMovieResponseDto(Likes likes) {
        this.id = likes.getMovie().getId();
        this.name = likes.getMovie().getName();
        this.posterUrl = likes.getMovie().getPosterUrl();
        this.rating = likes.getMovie().getRating();
    }


    public static MypageMovieResponseDto from(Likes likes) {
        return MypageMovieResponseDto.builder()
                .likes(likes)
                .build();
    }
}

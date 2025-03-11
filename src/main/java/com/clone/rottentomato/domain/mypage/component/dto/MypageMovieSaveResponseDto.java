package com.clone.rottentomato.domain.mypage.component.dto;

import com.clone.rottentomato.domain.saved.component.entity.Saved;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class MypageMovieSaveResponseDto {
    private Long id;
    private String name;    // 영화명
    private String posterUrl;
    private BigDecimal rating;


    @Builder
    public MypageMovieSaveResponseDto(Saved saved) {
        this.id = saved.getMovie().getId();
        this.name = saved.getMovie().getName();
        this.posterUrl = saved.getMovie().getPosterUrl();
        this.rating = saved.getMovie().getRating();
    }


    public static MypageMovieSaveResponseDto from(Saved saved) {
        return MypageMovieSaveResponseDto.builder()
                .saved(saved)
                .build();
    }
}

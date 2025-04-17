package com.clone.rottentomato.domain.likes.component.dto;

import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LikesPageResponseDto {
    private List<MypageMovieResponseDto> content;
    private int totalPage;


    @Builder
    public LikesPageResponseDto(List<MypageMovieResponseDto> content, int totalPage) {
        this.content = content;
        this.totalPage = totalPage;
    }

    public static LikesPageResponseDto from(List<MypageMovieResponseDto> content,int totalPage) {
        return LikesPageResponseDto.builder()
                .content(content)
                .totalPage(totalPage)
                .build();
    }
}

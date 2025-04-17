package com.clone.rottentomato.domain.saved.component.dto;

import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieSaveResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SavedPageResponseDto {
    private List<MypageMovieSaveResponseDto> content;
    private int totalPage;


    @Builder
    public SavedPageResponseDto(List<MypageMovieSaveResponseDto> content, int totalPage) {
        this.content = content;
        this.totalPage = totalPage;
    }

    public static SavedPageResponseDto from(List<MypageMovieSaveResponseDto> content, int totalPage) {
        return SavedPageResponseDto.builder()
                .content(content)
                .totalPage(totalPage)
                .build();
    }
}

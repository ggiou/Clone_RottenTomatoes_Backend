package com.clone.rottentomato.domain.saved.component.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class SavedResponseDto {
    private HttpStatus status;
    private boolean isLiked;
    private int count;


    @Builder
    public SavedResponseDto(HttpStatus status, boolean isLiked, int count) {
        this.status = status;
        this.isLiked = isLiked;
        this.count = count;
    }


    public static SavedResponseDto of(HttpStatus status, boolean isLiked, int count) {
        return SavedResponseDto.builder()
                .status(status)
                .isLiked(isLiked)
                .count(count)
                .build();
    }


}

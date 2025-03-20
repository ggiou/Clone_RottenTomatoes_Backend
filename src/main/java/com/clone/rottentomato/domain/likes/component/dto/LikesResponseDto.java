package com.clone.rottentomato.domain.likes.component.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class LikesResponseDto {
    private HttpStatus status;
    private boolean isLiked;
    private int isStatus;
    private int count;


    @Builder
    public LikesResponseDto(HttpStatus status, boolean isLiked, int count, int isStatus) {
        this.status = status;
        this.isLiked = isLiked;
        this.count = count;
        this.isStatus = isStatus;
    }


    public static LikesResponseDto of(HttpStatus status, boolean isLiked, int count,int isStatus) {
        return LikesResponseDto.builder()
                .status(status)
                .isLiked(isLiked)
                .count(count)
                .isStatus(isStatus)
                .build();
    }


}

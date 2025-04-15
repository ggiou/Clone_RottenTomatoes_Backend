package com.clone.rottentomato.domain.saved.component.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SavedStatusResponseDto {
    private Long movieid;
    private boolean liked;


    @Builder
    public SavedStatusResponseDto(Long movieid, boolean liked) {
        this.movieid = movieid;
        this.liked = liked;
    }


    public static SavedStatusResponseDto from(Long movieid, boolean liked) {
        return SavedStatusResponseDto.builder()
                .movieid(movieid)
                .liked(liked)
                .build();
    }
}

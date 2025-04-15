package com.clone.rottentomato.domain.likes.component.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikesStatusResponseDto {
    private Long movieid;
    private boolean liked;


    @Builder
    public LikesStatusResponseDto(Long movieid, boolean liked) {
        this.movieid = movieid;
        this.liked = liked;
    }


    public static LikesStatusResponseDto from(Long movieid,boolean liked) {
        return LikesStatusResponseDto.builder()
                .movieid(movieid)
                .liked(liked)
                .build();
    }
}

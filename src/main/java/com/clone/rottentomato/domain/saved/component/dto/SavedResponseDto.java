package com.clone.rottentomato.domain.saved.component.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class SavedResponseDto {
    private HttpStatus status;
    private boolean isSaved;
    private int isStatus;
    private int count;


    @Builder
    public SavedResponseDto(HttpStatus status, boolean isSaved, int count, int isStatus) {
        this.status = status;
        this.isSaved = isSaved;
        this.count = count;
        this.isStatus = isStatus;
    }


    public static SavedResponseDto of(HttpStatus status, boolean isSaved, int count,int isStatus) {
        return SavedResponseDto.builder()
                .status(status)
                .isSaved(isSaved)
                .count(count)
                .isStatus(isStatus)
                .build();
    }


}

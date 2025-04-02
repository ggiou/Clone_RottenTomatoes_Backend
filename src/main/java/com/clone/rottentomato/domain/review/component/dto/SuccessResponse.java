package com.clone.rottentomato.domain.review.component.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SuccessResponse {
    private int status;

    @Builder
    private SuccessResponse(int status) {
        this.status = status;
    }

    public static SuccessResponse of(HttpStatus status){
        return SuccessResponse.builder()
                .status(status.value())
                .build();

    }

}
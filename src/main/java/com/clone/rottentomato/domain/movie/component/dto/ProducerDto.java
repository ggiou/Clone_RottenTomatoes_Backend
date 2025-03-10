package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.Producer;
import com.clone.rottentomato.domain.movie.constant.ProducerType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProducerDto {
    private Long id;
    private String name;
    private ProducerType producerType;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)   // 0 일 경우 응답 값 미반환
    private Long movieCnt = 0L;   // 영화 참여 횟수

    private ProducerDto(Long id, String name, ProducerType type, Long movieCnt){
        this.id = id;
        this.name = name;
        this.producerType = type;
        this.movieCnt = movieCnt;
    }

    private ProducerDto(Long id, String name, ProducerType type){
        this.id = id;
        this.name = name;
        this.producerType = type;
    }

    public static ProducerDto fromEntity(Producer entity){
        if(Objects.isNull(entity)) return null;
        return new ProducerDto(entity.getId(), entity.getName(), entity.getRoleType());
    }
}

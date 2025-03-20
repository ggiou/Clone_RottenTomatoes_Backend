package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.Producer;
import com.clone.rottentomato.domain.movie.constant.ProducerType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProducerDto {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int index;  // 인덱스 필요할때 사용

    private Long id;
    private String name;
    private ProducerType producerType;

    @JsonIgnore
    private Long movieCnt = 0L;   // 영화 참여 횟수

    private ProducerDto(Long id, String name, ProducerType type, Long movieCnt){
        this.id = id;
        this.name = name;
        this.producerType = type;
        this.movieCnt = movieCnt;
    }

    private ProducerDto(int index, Long id, String name, ProducerType type, Long movieCnt){
        this.id = id;
        this.name = name;
        this.producerType = type;
        this.movieCnt = movieCnt;
        this.index = index;
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

    public static ProducerDto of(int index, Producer entity){
        ProducerDto dto = ProducerDto.fromEntity(entity);
        if(!Objects.isNull(dto)) dto.setIndex(index);
        return dto;
    }
}

package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.domain.movie.component.dto.CategoryInfoDto;
import com.clone.rottentomato.domain.movie.component.dto.MovieDto;
import com.clone.rottentomato.exception.MovieException;
import com.clone.rottentomato.util.UtilDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/** 영화 카테고리 정보 */
@Getter
@Entity
@NoArgsConstructor
public class CategoryInfo extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 영화 카테고리 id
    @Column(nullable = false, unique = true)
    private String name;    // 카테고리 이름

    private CategoryInfo(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public static CategoryInfo fromDto(CategoryInfoDto dto){
        if(Objects.isNull(dto)) throw new MovieException("카테고리 정보가 없습니다.", CommonError.BAD_REQUEST);
        return new CategoryInfo(dto.getId(), dto.getName());
    }
}

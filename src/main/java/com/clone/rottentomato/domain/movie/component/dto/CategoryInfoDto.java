package com.clone.rottentomato.domain.movie.component.dto;

import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {
    private Long id;
    private String name;

    public static CategoryInfoDto fromEntity(CategoryInfo entity) {
        if(Objects.isNull(entity)) return null;
        return new CategoryInfoDto(entity.getId(), entity.getName());
    }

}

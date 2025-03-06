package com.clone.rottentomato.domain.movie.repository.custom;

import com.clone.rottentomato.domain.movie.component.dto.CategoryInfoDto;
import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;

import java.util.List;

public interface CategoryInfoCustomRepository {
    // 카테고리 정보 저장
    List<CategoryInfo> saveCategoryInfoList(List<CategoryInfo> entityList);
    List<CategoryInfoDto> returnSaveCategoryInfoList(List<CategoryInfo> entityList);


}

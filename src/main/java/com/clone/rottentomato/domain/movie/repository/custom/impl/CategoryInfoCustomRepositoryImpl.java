package com.clone.rottentomato.domain.movie.repository.custom.impl;

import com.clone.rottentomato.domain.movie.component.dto.CategoryInfoDto;
import com.clone.rottentomato.domain.movie.component.entity.CategoryInfo;
import com.clone.rottentomato.domain.movie.repository.CategoryInfoRepository;
import com.clone.rottentomato.domain.movie.repository.custom.CategoryInfoCustomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Transactional
@Repository
@RequiredArgsConstructor
public class CategoryInfoCustomRepositoryImpl implements CategoryInfoCustomRepository {
    private final CategoryInfoRepository categoryInfoRepository;

    /** 카테고리 정보 리스트가 없다면 저장, 있으면 업데이트
     * @return 저장한 CategoryInfo 리스트 */
    @Override
    public List<CategoryInfo> saveCategoryInfoList(List<CategoryInfo> entityList) {
        // db에 해당 이름과 동일한 카테고리 정보들을 조회
        List<String> names = entityList.stream().map(CategoryInfo::getName).filter(StringUtils::isNotBlank).toList();
        List<CategoryInfo> searchNameList = categoryInfoRepository.findByNames(names);

        // db에 없는 카테고리 정보만 저장
        List<CategoryInfo> saveTargetList = entityList.stream()
                .filter(s-> searchNameList.stream().noneMatch(t->t.getName().equals(s.getName()))).toList();
        categoryInfoRepository.saveAll(saveTargetList);

        // db에 저장된 정보를 list 반환
        searchNameList.addAll(saveTargetList);
        return searchNameList;
    }

    /** 카테고리 정보 리스트가 없다면 저장, 있으면 업데이트
     * @return 저장한 CategoryInfoDto 리스트 */
    @Override
    public List<CategoryInfoDto> returnSaveCategoryInfoList(List<CategoryInfo> entityList) {
        // db에 저장된 정보를 dto list로 변환해 반환
        List<CategoryInfo> searchNameList = saveCategoryInfoList(entityList);
        return searchNameList.stream().map(CategoryInfoDto::fromEntity).toList();
    }

}

package com.clone.rottentomato.common.component.dto;

import com.clone.rottentomato.common.constant.SortType;
import lombok.Getter;

/** 정렬 Request */
@Getter
public class SortRequestDto {
    private SortType sortType;  // 정렬 타입
    private boolean isAsc = false;      // 오름차/내림차 순 (default, desc)

    public String getIsAscSql(){
        return isAsc ? " ASC" : " DESC";
    }

    public String getSortStr(){
        return String.format("%s_%s", sortType.name(), getIsAscSql());
    }
}

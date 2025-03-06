package com.clone.rottentomato.common.component.dto;

import com.clone.rottentomato.common.constant.SortType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 정렬 Request */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortRequestDto {
    private SortType sortType;  // 정렬 타입
    @JsonProperty("isAsc")
    private boolean isAsc = false;      // 오름차/내림차 순 (default, desc)
}

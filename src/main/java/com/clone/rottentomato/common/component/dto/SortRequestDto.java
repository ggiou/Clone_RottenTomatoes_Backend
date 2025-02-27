package com.clone.rottentomato.common.component.dto;

import com.clone.rottentomato.common.constant.SortType;
import lombok.Getter;

@Getter
public class SortRequestDto {
    private SortType sortType;
    private boolean isAsc;
}

package com.clone.rottentomato.common.component.dto;

import com.clone.rottentomato.common.constant.SortType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortRequestDto {
    private SortType sortType;
    private boolean isAsc;
}

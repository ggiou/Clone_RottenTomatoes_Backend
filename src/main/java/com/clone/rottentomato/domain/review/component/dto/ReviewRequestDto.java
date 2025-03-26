package com.clone.rottentomato.domain.review.component.dto;

import com.clone.rottentomato.common.constant.SortType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto{
    private Integer rating = 0;
    private String reviewContent;
    private SortType sortType;
}

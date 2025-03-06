package com.clone.rottentomato.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/** jpa 관련 util class */
@Slf4j
@Service
public class UtilJpa<T> {
    /** null 이 아닌 요청된 기존값과 다른 필드만 기존 entity 객체에 set 해주는 함수
     * @param entity  (db에 저장되어 있던 원래 객체) -> 다른 값을 set 할 객체
     * @param excludeProperties (같지 않은 값을 업데이트 하는 기능에서 제외할 필드 명)
     * @param request (요청된 request 객체)
     * @return isAllEquals (모든 요소가 동일한지 확인)*/
    public T setNotEqualsProperties(T entity, T request, List<String> excludeProperties) {
        boolean isAllEquals = true;
        List<String> excludes = new ArrayList<>(Arrays.asList("regDate", "modDate"));
        if(Objects.nonNull(excludeProperties) && !excludeProperties.isEmpty()) {
            excludes.addAll(excludeProperties);
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                // 특정 필드 제외
                if (excludes.contains(field.getName())) {
                    continue;
                }
                Object entityValue = field.get(entity);
                Object requestValue = field.get(request);

                // 요청 객체 필드의 값이 null 이 아니고, 기존 엔티티 객체 값과 다른 경우에만 업데이트
                if (!Objects.isNull(requestValue) && !Objects.equals(entityValue, requestValue)) {
                    field.set(entity, requestValue);
                }
            } catch (IllegalAccessException e) {
                log.error(String.format("[setNotEqualsProperties Error - %s] %s", entity.getClass().getName(),e.getMessage()), e);
            }
        }
        return entity;
    }

    public T setNotEqualsProperties(T entity, T request) {
        return setNotEqualsProperties(entity, request, null);
    }

    public String getOrderBySql(Pageable pageable, String as){
        StringBuilder jpql = new StringBuilder();
        jpql.append("ORDER BY ");
        Iterator<Sort.Order> iterator = pageable.getSort().iterator();
        
        boolean isMultiSort = iterator.hasNext() && iterator.next() != null && iterator.hasNext();
        // 다중 정렬일 경우
        if(isMultiSort){
            String sortClause = pageable.getSort().stream()
                    .map(order -> String.format("%s.%s %s", as, order.getProperty(), (order.isAscending() ? "ASC" : "DESC")))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse(""); // 여러 정렬 조건을 쉼표로 구분
            jpql.append(sortClause);
            return jpql.toString();
        }

        // 단일 정렬일 경우
        Sort.Order order = pageable.getSort().iterator().next(); // 첫 번째 정렬 조건만 가져옴
        jpql.append(String.format("%s.%s %s", as, order.getProperty(), (order.isAscending() ? "ASC" : "DESC")));
        return jpql.toString();
    }
}

package com.clone.rottentomato.util;

import com.clone.rottentomato.common.valid.validation.AllowRepositoryCustomImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/** jpa 관련 util class */
@Slf4j
@Service
public class UtilJpa<T> {
    /** null 이 아닌 요청된 기존값과 다른 필드만 기존 entity 객체에 set 해주는 함수
     * @param entity  (db에 저장되어 있던 원래 객체) -> 다른 값을 set 할 객체
     * @param request (요청된 request 객체)*/
    // 해당 기능은, setAccessible(true) 로 인해 private 에서도 setter 에 접근 가능해, custom repository 에서만 사용할 수 있도록 접근 제한을 설정해 줬다.
    @AllowRepositoryCustomImpl
    public void setNotEqualsProperties(T entity, T request) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object entityValue = field.get(entity);
                Object requestValue = field.get(request);

                // 요청 객체 필드의 값이 null 이 아니고, 기존 엔티티 객체 값과 다른 경우에만 업데이트
                if (!Objects.isNull(requestValue) && !Objects.equals(entityValue, requestValue)) {
                    field.set(request, requestValue);
                }
            } catch (IllegalAccessException e) {
                log.error(String.format("[setNotEqualsProperties Error - %s] %s", entity.getClass().getName(),e.getMessage()), e);
            }
        }
    }
}

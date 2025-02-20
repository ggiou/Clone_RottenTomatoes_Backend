package com.clone.rottentomato.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class UtilJpa<T, ID>{
    @PersistenceContext
    private EntityManager entityManager;

    private final JpaRepository<T, ID> repository;

    public Optional<T> findByColumn(String columnName, Object value, Class<T> clazz) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> root = query.from(clazz);

        Predicate predicate = cb.equal(root.get(columnName), value);
        query.where(predicate);

        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    public T saveOrUpdateByColumn(String columnName, Object columnValue, T req) {
        // 엔티티 지정 컬럽 값으로 조회
        Class<T> clazz = (Class<T>) req.getClass();
        Optional<T> columnEntity = findByColumn(columnName, columnValue, clazz);
        return saveOrUpdate(columnEntity, req);
    }

    public T saveOrUpdateByPk(ID pk, T req) {
        // 엔티티 pk로 조회
        Optional<T> pkEntity = repository.findById(pk);
        return saveOrUpdate(pkEntity, req);
    }

    private T saveOrUpdate(Optional<T> existEntity, T req) {
        T resultEntity;
        if (existEntity.isPresent()) {
            // 존재하면 null이 아닌 값만 업데이트
            T updateReq = existEntity.get();
            copyNonNullProperties(req, updateReq);
            resultEntity = repository.save(updateReq); // 업데이트
        } else {
            // 존재하지 않으면 새로 저장
            resultEntity = repository.save((T)req);
        }
        return resultEntity;
    }

    private void copyNonNullProperties(T source, T target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (!Objects.isNull(value)) { // source 필드 값이 null이 아닐 경우만 set
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

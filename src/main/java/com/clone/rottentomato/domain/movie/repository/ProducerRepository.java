package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.dto.ProducerDto;
import com.clone.rottentomato.domain.movie.component.entity.Producer;
import com.clone.rottentomato.domain.movie.constant.ProducerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    @Query("SELECT p FROM Producer p WHERE p.name IN :names")
    List<Producer> findByNames(List<String> names);

    @Query(" SELECT new com.clone.rottentomato.domain.movie.component.dto.ProducerDto(p.id, p.name, p.roleType, COUNT(mp))"
        +" FROM Producer p LEFT JOIN MovieProducer mp ON mp.producer.id = p.id"
        +" WHERE p.roleType =:role AND p.name LIKE CONCAT('%', :name, '%')"
        +" GROUP BY p.id"
        +" ORDER BY CASE WHEN p.name LIKE :name || '%' THEN 1 WHEN p.name LIKE '%' || :name || '%' THEN 2 ELSE 3 END, COUNT(mp) DESC")
    Page<ProducerDto> findByNameLikeWithPageable(@Param("role") ProducerType role, @Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT p.id) FROM Producer p WHERE p.roleType =:role AND p.name LIKE CONCAT('%', :name, '%') ")
    int countByName(@Param("role") ProducerType role, @Param("name") String name);
}

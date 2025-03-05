package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    @Query("SELECT p FROM Producer p WHERE p.name IN :names")
    List<Producer> findByNames(List<String> names);
}

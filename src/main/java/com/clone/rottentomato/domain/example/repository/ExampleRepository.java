package com.clone.rottentomato.domain.example.repository;

import com.clone.rottentomato.domain.example.component.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Example, Long>, ExampleCustomRepository {
}

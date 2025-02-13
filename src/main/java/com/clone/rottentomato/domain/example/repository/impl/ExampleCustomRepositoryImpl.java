package com.clone.rottentomato.domain.example.repository.impl;

import com.clone.rottentomato.domain.example.repository.ExampleCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class ExampleCustomRepositoryImpl implements ExampleCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
}

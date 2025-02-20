package com.clone.rottentomato.domain.movie.repository.impl;

import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.movie.repository.MovieCustomRepository;
import com.clone.rottentomato.util.UtilJpa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class MovieCustomRepositoryImpl implements MovieCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private UtilJpa<Movie, Long> utilJpaForMovie;

    @Override
    public Movie saveOrUpdate(Movie entity) {
        return utilJpaForMovie.saveOrUpdateByPk(entity.getId(), entity);
    }
}

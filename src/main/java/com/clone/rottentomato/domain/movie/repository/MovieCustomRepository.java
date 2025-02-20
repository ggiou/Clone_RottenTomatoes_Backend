package com.clone.rottentomato.domain.movie.repository;

import com.clone.rottentomato.domain.movie.component.entity.Movie;

public interface MovieCustomRepository {
    public Movie saveOrUpdate(Movie entity);
}

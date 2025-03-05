package com.clone.rottentomato.domain.movie.component.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MovieProducerId implements Serializable{
    private Long movie;
    private Long producer;


    public MovieProducerId(Long movieId, Long producerId) {
        this.movie = movieId;
        this.producer = producerId;
    }

    public MovieProducerId(Movie movie, Producer producer) {
        this.movie = movie.getId();
        this.producer = producer.getId();
    }

    public static List<MovieProducerId> of(List<MovieProducer> movieProducers){
        return movieProducers.stream().map(t->new MovieProducerId(t.getMovie(), t.getProducer())).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieProducerId that = (MovieProducerId) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(producer, that.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, producer);
    }
}

package com.clone.rottentomato.domain.review.repository;


import com.clone.rottentomato.domain.review.component.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
}

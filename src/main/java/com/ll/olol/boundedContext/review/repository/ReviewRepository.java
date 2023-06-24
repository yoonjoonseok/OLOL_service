package com.ll.olol.boundedContext.review.repository;

import com.ll.olol.boundedContext.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
